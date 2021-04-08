package reghzy.advbanitem.limit;

import org.bukkit.ChatColor;
import org.bukkit.World;
import org.bukkit.entity.Player;
import reghzy.advbanitem.command.commands.single.DisplayLimiterCommand;
import reghzy.advbanitem.helpers.StringHelper;
import reghzy.advbanitem.permissions.PermissionsHelper;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MetaLimit {
    public int id;
    public int metadata;
    public List<String> disallowedWorlds;
    public boolean invertPermissions;
    public boolean invertWorlds;
    public String placePermission;
    public String breakPermission;
    public String interactPermission;
    public String noPlaceMessage;
    public String noBreakMessage;
    public String noInteractMessage;

    public MetaLimit(int id, int metadata, List<String> disallowedWorlds,
                     boolean invertPermissions, boolean invertWorlds,
                     String placePermission, String breakPermission, String interactPermission,
                     String noPlaceMessage, String noBreakMessage, String noInteractMessage) {
        this.id                 = id;
        this.metadata           = metadata;
        this.disallowedWorlds   = disallowedWorlds;
        this.invertPermissions  = invertPermissions;
        this.invertWorlds       = invertWorlds;
        this.placePermission    = placePermission;
        this.breakPermission    = breakPermission;
        this.interactPermission = interactPermission;
        this.noPlaceMessage     = noPlaceMessage;
        this.noBreakMessage     = noBreakMessage;
        this.noInteractMessage  = noInteractMessage;

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
        String worldName = world.getName();
        if (this.invertWorlds) {
            return WorldLookup.containsWorld(this.id, this.metadata, worldName);
        }
        else {
            // if it contains it means its NOT ALLOWED
            return !WorldLookup.containsWorld(this.id, this.metadata, worldName);
        }
    }

    @Override
    public String toString() {
        return new StringBuilder().append(ChatColor.GREEN).append(metadata).append(':').append('\n').append(ChatColor.GOLD).
                append("  Invert Permissions: ").append(ChatColor.DARK_AQUA).append(invertPermissions).append(ChatColor.GOLD).append('\n').
                append("  Invert Worlds: ").append(ChatColor.DARK_AQUA).append(invertWorlds).append(ChatColor.GOLD).append('\n').
                append("  Disallowed Worlds: ").append(ChatColor.DARK_AQUA).append(StringHelper.joinArray(this.disallowedWorlds.toArray(new String[0]), 0, ", ")).append(ChatColor.GOLD).append('\n').
                append("  Place Permission: ").append(ChatColor.DARK_AQUA).append(DisplayLimiterCommand.nullPermissionCheck(placePermission)).append(ChatColor.GOLD).append('\n').
                append("  Break Permissions: ").append(ChatColor.DARK_AQUA).append(DisplayLimiterCommand.nullPermissionCheck(breakPermission)).append(ChatColor.GOLD).append('\n').
                append("  Interact Permissions: ").append(ChatColor.DARK_AQUA).append(DisplayLimiterCommand.nullPermissionCheck(interactPermission)).append(ChatColor.GOLD).append('\n').
                append("  No Place Message: ").append(ChatColor.DARK_AQUA).append(noPlaceMessage).append(ChatColor.GOLD).append('\n').
                append("  No Break Message: ").append(ChatColor.DARK_AQUA).append(noBreakMessage).append(ChatColor.GOLD).append('\n').
                append("  No Interact Message: ").append(ChatColor.DARK_AQUA).append(noInteractMessage).append(ChatColor.GOLD).
                toString();
    }
}
