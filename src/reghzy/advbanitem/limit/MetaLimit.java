package reghzy.advbanitem.limit;

import org.bukkit.ChatColor;
import org.bukkit.World;
import org.bukkit.entity.Player;
import reghzy.advbanitem.command.commands.single.DisplayLimiterCommand;
import reghzy.advbanitem.helpers.StringHelper;
import reghzy.advbanitem.logs.ChatFormat;
import reghzy.advbanitem.permissions.PermissionsHelper;

import java.util.List;

public class MetaLimit {
    public final int id;
    public final int metadata;
    public final List<String> disallowedWorlds;
    public final boolean invertPermissions;
    public final boolean invertWorlds;
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
                     String placePermission, String breakPermission, String interactPermission,
                     String pickupPermission, String invClickPermission,
                     String noPlaceMessage, String noBreakMessage, String noInteractMessage,
                     String noPickupMessage, String noInvClickMessage) {
        this.id                 = id;
        this.metadata           = metadata;
        this.disallowedWorlds   = disallowedWorlds;
        this.invertPermissions  = invertPermissions;
        this.invertWorlds       = invertWorlds;
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

        WorldLookup.addDisallowed(this.id, this.metadata, this.disallowedWorlds);
    }

    public boolean canPlace(Player player) {
        if (allowsWorld(player.getWorld())) {
            return hasPermission(player, placePermission);
        }

        return false;
    }

    public boolean canBreak(Player player) {
        if (allowsWorld(player.getWorld())) {
            return hasPermission(player, breakPermission);
        }

        return false;
    }

    public boolean canInteract(Player player) {
        if (allowsWorld(player.getWorld())) {
            return hasPermission(player, interactPermission);
        }

        return false;
    }

    public boolean canClickInventory(Player player) {
        if (allowsWorld(player.getWorld())) {
            return hasPermission(player, invClickPermission);
        }
        return false;
    }

    public boolean canPickupItem(Player player) {
        if (allowsWorld(player.getWorld())) {
            return hasPermission(player, pickupPermission);
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
