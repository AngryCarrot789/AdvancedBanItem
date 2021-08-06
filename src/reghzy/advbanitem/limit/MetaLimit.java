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
import reghzy.advbanitem.command.ABICommandExecutor;
import reghzy.advbanitem.command.commands.single.DisplayLimiterCommand;
import reghzy.mfunclagfind.permissions.PermissionsHelper;
import reghzy.mfunclagfind.utils.text.StringHelper;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

public class MetaLimit {
    public final int id;
    public final int metadata;
    public final List<String> disallowedWorlds;
    public final List<Pattern> disallowedWorldFilters;
    public final boolean invertPermissions;
    public final boolean invertWorlds;
    public final boolean useNbt;
    public final boolean cancelOnNbtMatch;
    public final boolean destroyOnCancel;
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
                     boolean destroyOnCancel,
                     String placePermission, String breakPermission, String interactPermission,
                     String pickupPermission, String invClickPermission,
                     String noPlaceMessage, String noBreakMessage, String noInteractMessage,
                     String noPickupMessage, String noInvClickMessage) {
        this.id = id;
        this.metadata = metadata;
        this.disallowedWorlds = disallowedWorlds;
        this.invertPermissions = invertPermissions;
        this.invertWorlds = invertWorlds;
        this.useNbt = useNbt;
        this.nbtFiltersRaw = nbtFiltersRaw;
        this.cancelOnNbtMatch = cancelOnNbtMatch;
        this.destroyOnCancel = destroyOnCancel;
        this.placePermission = placePermission;
        this.breakPermission = breakPermission;
        this.interactPermission = interactPermission;
        this.pickupPermission = pickupPermission;
        this.invClickPermission = invClickPermission;
        this.noPlaceMessage = noPlaceMessage;
        this.noBreakMessage = noBreakMessage;
        this.noInteractMessage = noInteractMessage;
        this.noPickupMessage = noPickupMessage;
        this.noInvClickMessage = noInvClickMessage;

        this.nbtMatchers = new ArrayList<NBTNodeMatcher>();
        for (String nbtFilter : this.nbtFiltersRaw) {
            try {
                this.nbtMatchers.add(new NBTNodeMatcher(nbtFilter));
            }
            catch (Exception e) {
                ABICommandExecutor.ABILogger.logPrefix("Exception loading NBT Matcher '" + nbtFilter + "': " + e.getMessage());
            }
        }

        this.disallowedWorldFilters = new ArrayList<Pattern>(disallowedWorlds.size());
        for(String disallowed : disallowedWorlds) {
            Pattern pattern;
            try {
                pattern = Pattern.compile(disallowed);
            }
            catch (PatternSyntaxException e) {
                ABICommandExecutor.ABILogger.logPrefix("Exception loading RegEx pattern '" + disallowed + "': " + e.getMessage());
                continue;
            }

            this.disallowedWorldFilters.add(pattern);
        }

        // WorldLookup.addDisallowed(this.id, this.metadata, this.disallowedWorlds);
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
            return NBTMatchResult.NBT_SOURCE_NOT_FOUND;
        }

        NBTTagCompound nbt = new NBTTagCompound();
        tileEntity.b(nbt);

        return tryNbtMatchTree(nbt);
    }

    public NBTMatchResult tryNbtMatchTree(ItemStack bukkitItemStack) {
        net.minecraft.server.v1_6_R3.ItemStack nmsItemStack = CraftItemStack.asNMSCopy(bukkitItemStack);
        if (nmsItemStack == null) {
            return NBTMatchResult.NBT_SOURCE_NOT_FOUND;
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
        if (this.disallowedWorldFilters.isEmpty()) {
            return !this.invertWorlds;
        }

        for (Pattern pattern : this.disallowedWorldFilters) {
            if (pattern.matcher(world.getName()).find()) {
                return this.invertWorlds;
            }
        }

        return !this.invertWorlds;
    }

    public String formattedDescription() {
        return new StringBuilder().append(ChatColor.GREEN).append(metadata).append(':').append('\n').
                append(ChatColor.GOLD).append("  Invert Permissions: ").append(ChatColor.AQUA).append(invertPermissions).append('\n').
                append(ChatColor.GOLD).append("  Invert Worlds: ").append(ChatColor.AQUA).append(String.valueOf(invertWorlds)).append('\n').
                append(ChatColor.GOLD).append("  Disallowed Worlds: ").append(ChatColor.AQUA).append(StringHelper.joinArray(this.disallowedWorlds.toArray(new String[0]), 0, ", ")).append('\n').
                append(ChatColor.GOLD).append("  Place Permission: ").append(ChatColor.AQUA).append(DisplayLimiterCommand.nullPermsCheck(placePermission)).append('\n').
                append(ChatColor.GOLD).append("  Break Permissions: ").append(ChatColor.AQUA).append(DisplayLimiterCommand.nullPermsCheck(breakPermission)).append('\n').
                append(ChatColor.GOLD).append("  Interact Permissions: ").append(ChatColor.AQUA).append(DisplayLimiterCommand.nullPermsCheck(interactPermission)).append('\n').
                append(ChatColor.GOLD).append("  Pickup Permissions: ").append(ChatColor.AQUA).append(DisplayLimiterCommand.nullPermsCheck(pickupPermission)).append('\n').
                append(ChatColor.GOLD).append("  Inv Click Permissions: ").append(ChatColor.AQUA).append(DisplayLimiterCommand.nullPermsCheck(invClickPermission)).append('\n').
                append(ChatColor.GOLD).append("  No Pickup Message: ").append(ChatColor.AQUA).append(noPickupMessage).append('\n').
                append(ChatColor.GOLD).append("  No Inv Click Message: ").append(ChatColor.AQUA).append(noInvClickMessage).append('\n').
                append(ChatColor.GOLD).append("  No Place Message: ").append(ChatColor.AQUA).append(noPlaceMessage).append('\n').
                append(ChatColor.GOLD).append("  No Break Message: ").append(ChatColor.AQUA).append(noBreakMessage).append('\n').
                append(ChatColor.GOLD).append("  No Interact Message: ").append(ChatColor.AQUA).append(noInteractMessage).
                append(ChatColor.GOLD).toString();
    }
}
