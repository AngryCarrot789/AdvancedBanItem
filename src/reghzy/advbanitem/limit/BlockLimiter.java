package reghzy.advbanitem.limit;

import org.bukkit.World;
import org.bukkit.entity.Player;
import reghzy.advbanitem.permissions.PermissionsHelper;

import java.util.HashSet;
import java.util.List;

/**
 * A class which contains information about blacklisting a specific block
 * from people without permission (or with, if inverted), and disallowing
 * the block in specific worlds (or allowing, if inverted)
 */
public class BlockLimiter {
    public int id;
    public final HashSet<Integer> metadata;
    public boolean invertMetadata;
    public boolean invertPermission;
    public boolean invertWorld;
    public String placePermission;
    public String breakPermission;
    public String interactPermission;
    public String noPlaceMessage;
    public String noBreakMessage;
    public String noInteractMessage;

    public static final String MetaDataName           = "MetaData";
    public static final String DisallowedWorldsName   = "DisallowedWorlds";
    public static final String InvertWorldName        = "InvertWorlds";
    public static final String PlacePermissionName    = "PlacePermission";
    public static final String BreakPermissionName    = "BreakPermission";
    public static final String InteractPermissionName = "InteractPermission";
    public static final String InvertMetadataName     = "InvertMetaData";
    public static final String InvertPermissionsName  = "InvertPermissions";
    public static final String NoPlaceMessageName     = "NoPlaceMessage";
    public static final String NoBreakMessageName     = "NoBreakMessage";
    public static final String NoInteractMessageName  = "NoInteractMessage";

    public static final String DefaultNoPlaceMessage    = "&4You don't have permission to place this block!";
    public static final String DefaultNoBreakMessage    = "&4You don't have permission to break this block!";
    public static final String DefaultNoInteractMessage = "&4You don't have permission to interact with this block!";

    public BlockLimiter(int id, HashSet<Integer> metadata, boolean invertMetadata, boolean invertPermission, boolean invertWorld, List<String> disallowedWorlds,
                        String placePermission, String breakPermission, String interactPermission,
                        String noPlaceMessage, String noBreakMessage, String noInteractMessage) {
        this.id                 = id;
        this.metadata           = metadata;
        this.invertMetadata     = invertMetadata;
        this.invertPermission   = invertPermission;
        this.invertWorld        = invertWorld;
        this.placePermission    = placePermission;
        this.breakPermission    = breakPermission;
        this.interactPermission = interactPermission;
        this.noPlaceMessage     = (noPlaceMessage    == null) ? DefaultNoPlaceMessage    : noPlaceMessage;
        this.noBreakMessage     = (noBreakMessage    == null) ? DefaultNoBreakMessage    : noBreakMessage;
        this.noInteractMessage  = (noInteractMessage == null) ? DefaultNoInteractMessage : noInteractMessage;

        WorldLookup.addBlockDisallowedWorlds(this.id, disallowedWorlds);
    }

    public boolean canPlace(Player player) {
        if (allowsWorld(player.getWorld())) {
            return hasPermission(player, this.placePermission);
        }

        return false;
    }

    public boolean canBreak(Player player) {
        if (allowsWorld(player.getWorld())) {
            return hasPermission(player, this.breakPermission);
        }

        return false;
    }

    public boolean canInteract(Player player) {
        if (allowsWorld(player.getWorld())) {
            return hasPermission(player, this.interactPermission);
        }

        return false;
    }

    public boolean hasMetadata(int data) {
        if (invertMetadata) {
            if (ignoreMetadata())
                return false;
            return !this.metadata.contains(data);
        }
        else {
            if (ignoreMetadata())
                return true;
            return this.metadata.contains(data);
        }
    }

    public boolean ignoreMetadata() {
        return this.metadata.contains(-1);
    }

    public boolean hasPermission(Player player, String permission) {
        if (permission == null)
            return !invertPermission;

        if (invertPermission) {
            return !PermissionsHelper.hasPermission(player, permission);
        }
        else {
            return PermissionsHelper.hasPermission(player, permission);
        }
    }

    /**
     * Returns true if this BlockLimiter allows the given world (allows, yes)
     */
    public boolean allowsWorld(World world) {
        if (this.invertWorld) {
            return WorldLookup.containsDisallowedWorld(this.id, world.getName());
        }
        else {
            return !WorldLookup.containsDisallowedWorld(this.id, world.getName());
        }
    }
}
