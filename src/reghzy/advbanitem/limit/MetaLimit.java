package reghzy.advbanitem.limit;

import net.minecraft.server.v1_6_R3.NBTTagCompound;
import net.minecraft.server.v1_6_R3.TileEntity;
import org.bukkit.ChatColor;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.craftbukkit.v1_6_R3.CraftWorld;
import org.bukkit.craftbukkit.v1_6_R3.inventory.CraftItemStack;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import reghzy.advbanitem.command.commands.single.DisplayLimiterCommand;
import reghzy.advbanitem.helpers.StringHelper;
import reghzy.advbanitem.logs.ChatFormat;
import reghzy.advbanitem.logs.ChatLogger;
import reghzy.advbanitem.permissions.PermissionsHelper;

import javax.xml.parsers.DocumentBuilderFactory;
import java.util.ArrayList;
import java.util.List;

public class MetaLimit {
    public final int id;
    public final int metadata;
    public final List<String> disallowedWorlds;
    public final boolean invertPermissions;
    public final boolean invertWorlds;
    public final boolean useNbt;
    public final boolean cancelOnNbtMatch;
    public final List<String> nbtFiltersRaw;
    public final List<NBTNodeMatcher> nbtMatchers;
    public final String placePermission;
    public final String breakPermission;
    public final String interactPermission;
    public final String noPlaceMessage;
    public final String noBreakMessage;
    public final String noInteractMessage;
    public final String noPickupMessage;
    public final String noInvClickMessage;
    public final String pickupPermission;
    public final String invClickPermission;

    public MetaLimit(int id, int metadata, List<String> disallowedWorlds,
                     boolean invertPermissions, boolean invertWorlds,
                     boolean useNbt, List<String> nbtFiltersRaw, boolean cancelOnNbtMatch,
                     String placePermission, String breakPermission, String interactPermission,
                     String pickupPermission, String invClickPermission,
                     String noPlaceMessage, String noBreakMessage, String noInteractMessage,
                     String noPickupMessage, String noInvClickMessage) {
        this.id                 = id;
        this.metadata           = metadata;
        this.disallowedWorlds   = disallowedWorlds;
        this.invertPermissions  = invertPermissions;
        this.invertWorlds       = invertWorlds;
        this.useNbt = useNbt;
        this.nbtFiltersRaw = nbtFiltersRaw;
        this.cancelOnNbtMatch = cancelOnNbtMatch;
        this.placePermission    = placePermission;
        this.breakPermission    = breakPermission;
        this.interactPermission = interactPermission;
        this.pickupPermission = pickupPermission;
        this.invClickPermission = invClickPermission;
        this.noPlaceMessage     = noPlaceMessage;
        this.noBreakMessage     = noBreakMessage;
        this.noInteractMessage  = noInteractMessage;
        this.noPickupMessage = noPickupMessage;
        this.noInvClickMessage = noInvClickMessage;

        this.nbtMatchers = new ArrayList<NBTNodeMatcher>();
        for(String nbtFilter : this.nbtFiltersRaw) {
            try {
                this.nbtMatchers.add(new NBTNodeMatcher(nbtFilter));
            }
            catch (Exception e) {
                ChatLogger.logConsole("Exception loading NBT Matcher '" + nbtFilter + "': " + e.getMessage());
            }
        }

        WorldLookup.addDisallowed(this.id, this.metadata, this.disallowedWorlds);
    }

    public boolean canPlace(Player player) {
        return hasPermissionAndAllowsWorld(player, placePermission);
    }

    public boolean canBreak(Player player) {
        return hasPermissionAndAllowsWorld(player, breakPermission);
    }

    public boolean canInteract(Player player) {
        return hasPermissionAndAllowsWorld(player, interactPermission);
    }

    public boolean canClickInventory(Player player) {
        return hasPermissionAndAllowsWorld(player, invClickPermission);
    }

    public boolean canPickupItem(Player player) {
        return hasPermissionAndAllowsWorld(player, pickupPermission);
    }

    public NBTMatchResult tryNbtMatchTree(Block block) {
        TileEntity tileEntity = ((((CraftWorld) block.getWorld()).getHandle().getTileEntity(block.getX(), block.getY(), block.getZ())));
        if (tileEntity == null) {
            return NBTMatchResult.TILE_ENTITY_NOT_FOUND;
        }

        NBTTagCompound nbt = new NBTTagCompound();
        tileEntity.b(nbt);

        return tryNbtMatchTree(nbt);
    }

    public NBTMatchResult tryNbtMatchTree(ItemStack bukkitItemStack) {
        net.minecraft.server.v1_6_R3.ItemStack nmsItemStack = CraftItemStack.asNMSCopy(bukkitItemStack);
        if (nmsItemStack == null) {
            return NBTMatchResult.TILE_ENTITY_NOT_FOUND;
        }

        if (!nmsItemStack.hasTag()) {
            return NBTMatchResult.NBT_TREE_NOT_FOUND;
        }

        return tryNbtMatchTree(nmsItemStack.getTag());
    }

    public NBTMatchResult tryNbtMatchTree(NBTTagCompound nbt) {
        for (NBTNodeMatcher matcher : this.nbtMatchers) {
            if (matcher.matchNbtTree(nbt).equals(NBTMatchResult.NBT_MATCH_SUCCESS)) {
                return NBTMatchResult.NBT_MATCH_SUCCESS;
            }
        }

        return NBTMatchResult.NBT_MATCH_FAILED;
    }

    public boolean hasPermissionAndAllowsWorld(Player player, String permission) {
        if (allowsWorld(player.getWorld())) {
            return hasPermission(player, permission);
        }
        return false;
    }

    public boolean hasPermission(Player player, String permission) {
        if (permission == null)
            return !this.invertPermissions;

        if (this.invertPermissions) {
            return !PermissionsHelper.hasPermission(player, permission);
        }
        else {
            return PermissionsHelper.hasPermission(player, permission);
        }
    }

    public boolean allowsWorld(World world) {
        if (this.invertWorlds) {
            return WorldLookup.containsWorld(this.id, this.metadata, world.getName());
        }
        else {
            // if it contains it means its NOT ALLOWED
            return !WorldLookup.containsWorld(this.id, this.metadata, world.getName());
        }
    }

    public String formattedDescription() {
        return new StringBuilder().append(ChatColor.GREEN).append(metadata).append(':').append('\n').
                append(ChatColor.GOLD).append("  Invert Permissions: ").append(ChatFormat.aqua(String.valueOf(invertPermissions))).append('\n').
                append(ChatColor.GOLD).append("  Invert Worlds: ").append(ChatFormat.aqua(String.valueOf(invertWorlds))).append('\n').
                append(ChatColor.GOLD).append("  Disallowed Worlds: ").append(ChatFormat.aqua(StringHelper.joinArray(this.disallowedWorlds.toArray(new String[0]), 0, ", "))).append('\n').
                append(ChatColor.GOLD).append("  Place Permission: ").append(ChatFormat.aqua(DisplayLimiterCommand.nullPermsCheck(placePermission))).append('\n').
                append(ChatColor.GOLD).append("  Break Permissions: ").append(ChatFormat.aqua(DisplayLimiterCommand.nullPermsCheck(breakPermission))).append('\n').
                append(ChatColor.GOLD).append("  Interact Permissions: ").append(ChatFormat.aqua(DisplayLimiterCommand.nullPermsCheck(interactPermission))).append('\n').
                append(ChatColor.GOLD).append("  Pickup Permissions: ").append(ChatFormat.aqua(DisplayLimiterCommand.nullPermsCheck(pickupPermission))).append('\n').
                append(ChatColor.GOLD).append("  Inv Click Permissions: ").append(ChatFormat.aqua(DisplayLimiterCommand.nullPermsCheck(invClickPermission))).append('\n').
                append(ChatColor.GOLD).append("  No Pickup Message: ").append(ChatFormat.aqua(noPickupMessage)).append('\n').
                append(ChatColor.GOLD).append("  No Inv Click Message: ").append(ChatFormat.aqua(noInvClickMessage)).append('\n').
                append(ChatColor.GOLD).append("  No Place Message: ").append(ChatFormat.aqua(noPlaceMessage)).append('\n').
                append(ChatColor.GOLD).append("  No Break Message: ").append(ChatFormat.aqua(noBreakMessage)).append('\n').
                append(ChatColor.GOLD).append("  No Interact Message: ").append(ChatFormat.aqua(noInteractMessage)).
                append(ChatColor.GOLD).toString();
    }
}
