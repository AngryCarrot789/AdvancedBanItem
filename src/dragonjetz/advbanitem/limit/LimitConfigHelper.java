package dragonjetz.advbanitem.limit;

import org.bukkit.configuration.ConfigurationSection;

import java.util.ArrayList;
import java.util.List;

/**
 * <h2>
 *     A class for making config getting/setting easier
 * </h2>
 * <h3>
 *     Without having to worry about the correct config section names and stuff
 * </h3>
 */
public class LimitConfigHelper {
    // --------------------------------------------------------------------------------------------------
    // ------------------------------------------ Constants ---------------------------------------------
    // --------------------------------------------------------------------------------------------------
    public static final String DefaultProcessPermsFirstName  = "DefaultProcessPermissionsFirst";
    public static final String DefaultInvertPermissionsName  = "DefaultInvertPermissions";
    public static final String DefaultInvertWorldsName       = "DefaultInvertWorlds";
    public static final String DefaultUseNBTName             = "DefaultUseNBT";
    public static final String DefaultNBTFiltersName         = "DefaultNBTFilters";
    public static final String DefaultDestroyOnCancelName    = "DefaultDestroyOnCancel";
    public static final String DefaultDisallowedWorldsName   = "DefaultDisallowedWorlds";
    public static final String DefaultPlacePermissionName    = "DefaultPlacePermission";
    public static final String DefaultBreakPermissionName    = "DefaultBreakPermission";
    public static final String DefaultInteractPermissionName = "DefaultInteractPermission";
    public static final String DefaultPickupPermissionName   = "DefaultPickupPermission";
    public static final String DefaultInvClickPermissionName = "DefaultInvClickPermission";
    public static final String DefaultPlaceMessageName       = "DefaultPlaceMessage";
    public static final String DefaultBreakMessageName       = "DefaultBreakMessage";
    public static final String DefaultInteractMessageName    = "DefaultInteractMessage";
    public static final String DefaultNoPickupMessageName    = "DefaultNoPickupMessage";
    public static final String DefaultNoInvClickMessageName  = "DefaultNoInvClickMessage";

    public static final String MetaDataName           = "MetaData";
    public static final String ProcessPermsFirstName  = "ProcessPermissionsFirst";
    public static final String InvertPermissionsName  = "InvertPermissions";
    public static final String InvertWorldName        = "InvertWorlds";
    public static final String UseNBTName             = "UseNBT";
    public static final String NBTFiltersName         = "NBTFilters";
    public static final String DestroyOnCancelName    = "DestroyOnCancel";
    public static final String DisallowedWorldsName   = "DisallowedWorlds";
    public static final String CancelOnNBTMatchName   = "CancelEventOnNBTMatch";
    public static final String PlacePermissionName    = "PlacePermission";
    public static final String BreakPermissionName    = "BreakPermission";
    public static final String InteractPermissionName = "InteractPermission";
    public static final String PickupPermissionName   = "PickupPermission";
    public static final String InvClickPermissionName = "InvClickPermission";
    public static final String NoPlaceMessageName     = "NoPlaceMessage";
    public static final String NoBreakMessageName     = "NoBreakMessage";
    public static final String NoInteractMessageName  = "NoInteractMessage";
    public static final String NoPickupMessageName    = "NoPickupMessage";
    public static final String NoInvClickMessageName  = "NoInvClickMessage";

    public static final String FallbackPlaceMessageName = "FallbackPlaceMessage";
    public static final String FallbackBreakMessageName = "FallbackBreakMessage";
    public static final String FallbackInteractMessageName = "FallbackInteractMessage";
    public static final String FallbackNoPickupMessageName = "FallbackNoPickupMessage";
    public static final String FallbackNoInvClickMessageName = "FallbackNoClickInvMessage";

    // technically not a constants but still...
    public static String FallbackNoPlaceMessage = "&4You don't have permission to place this block!";
    public static String FallbackNoBreakMessage = "&4You don't have permission to break this block!";
    public static String FallbackNoInteractMessage = "&4You don't have permission to interact with this block!";

    // ##############################################################################################

    // ----------------------------------------------------------------------------------------------
    // ##################################### Configuration sections #################################
    // ----------------------------------------------------------------------------------------------

    public static ConfigurationSection getMetadataSection(ConfigurationSection section) {
        return section.getConfigurationSection(MetaDataName);
    }

    // ##############################################################################################

    // ----------------------------------------------------------------------------------------------
    // ############################ BlockLimiter (aka default stuff) ################################
    // ----------------------------------------------------------------------------------------------

    // ########################################## Getters ###########################################

    public static List<String> getDefaultDisallowedWorlds(ConfigurationSection section) {
        List<String> worlds = section.getStringList(DefaultDisallowedWorldsName);
        if (worlds == null || worlds.isEmpty())
            return new ArrayList<String>(0);
        return worlds;
    }

    public static List<String> getDefaultNbtFilters(ConfigurationSection section) {
        List<String> nbtFilters = section.getStringList(DefaultNBTFiltersName);
        if (nbtFilters == null || nbtFilters.isEmpty())
            return new ArrayList<String>(0);
        return nbtFilters;
    }

    public static boolean getDefaultInvertPermissions(ConfigurationSection section) {
        return section.getBoolean(DefaultInvertPermissionsName, false);
    }

    public static boolean getDefaultInvertWorlds(ConfigurationSection section) {
        return section.getBoolean(DefaultInvertWorldsName, false);
    }

    public static boolean getDefaultUseNBT(ConfigurationSection section) {
        return section.getBoolean(DefaultUseNBTName, false);
    }

    public static boolean getDefaultDestroyOnCancel(ConfigurationSection section) {
        return section.getBoolean(DefaultDestroyOnCancelName, false);
    }

    public static String getDefaultPlacePermission(ConfigurationSection section) {
        return section.getString(DefaultPlacePermissionName, null);
    }

    public static String getDefaultBreakPermission(ConfigurationSection section) {
        return section.getString(DefaultBreakPermissionName, null);
    }

    public static String getDefaultInteractPermission(ConfigurationSection section) {
        return section.getString(DefaultInteractPermissionName, null);
    }

    public static String getDefaultPickupPermission(ConfigurationSection section) {
        return section.getString(DefaultPickupPermissionName, null);
    }

    public static String getDefaultInvClickPermission(ConfigurationSection section) {
        return section.getString(DefaultInvClickPermissionName, null);
    }

    public static String getDefaultPlaceMessage(ConfigurationSection section) {
        return section.getString(DefaultPlaceMessageName, FallbackNoPlaceMessage);
    }

    public static String getDefaultBreakMessage(ConfigurationSection section) {
        return section.getString(DefaultBreakMessageName, FallbackNoBreakMessage);
    }

    public static String getDefaultInteractMessage(ConfigurationSection section) {
        return section.getString(DefaultInteractMessageName, FallbackNoInteractMessage);
    }

    public static String getDefaultNoPickupMessage(ConfigurationSection section) {
        return section.getString(DefaultNoPickupMessageName, null);
    }

    public static String getDefaultNoInvClickMessage(ConfigurationSection section) {
        return section.getString(DefaultNoInvClickMessageName, null);
    }

    // ##############################################################################################

    // ----------------------------------------------------------------------------------------------
    // ############################### MetaLimits (aka real limits stuff) ###########################
    // ----------------------------------------------------------------------------------------------

    // ---------------------------------------- Getters ---------------------------------------------

    public static List<String> getDisallowedWorlds(ConfigurationSection section, List<String> defaultWorlds) {
        List<String> worlds = section.getStringList(DisallowedWorldsName);
        if (worlds == null || worlds.isEmpty())
            return defaultWorlds;
        return worlds;
    }

    public static List<String> getNbtFilters(ConfigurationSection section, List<String> defaultFilters) {
        List<String> filters = section.getStringList(NBTFiltersName);
        if (filters == null || filters.isEmpty())
            return defaultFilters;
        return filters;
    }

    public static boolean getInvertPermissions(ConfigurationSection section, boolean defaultValue) {
        return section.getBoolean(InvertPermissionsName, defaultValue);
    }

    public static boolean getInvertWorlds(ConfigurationSection section, boolean defaultValue) {
        return section.getBoolean(InvertWorldName, defaultValue);
    }

    public static boolean getUseNbt(ConfigurationSection section, boolean defaultValue) {
        return section.getBoolean(UseNBTName, defaultValue);
    }

    public static boolean getDestroyOnCancel(ConfigurationSection section, boolean defaultValue) {
        return section.getBoolean(DestroyOnCancelName, defaultValue);
    }

    public static boolean getCancelEventOnNBTMatch(ConfigurationSection section, boolean defaultValue) {
        return section.getBoolean(CancelOnNBTMatchName, defaultValue);
    }

    public static String getPlacePermission(ConfigurationSection section, String defaultValue) {
        return section.getString(PlacePermissionName, defaultValue);
    }

    public static String getBreakPermission(ConfigurationSection section, String defaultValue) {
        return section.getString(BreakPermissionName, defaultValue);
    }

    public static String getInteractPermission(ConfigurationSection section, String defaultValue) {
        return section.getString(InteractPermissionName, defaultValue);
    }

    public static String getPickupPermission(ConfigurationSection section, String defaultValue) {
        return section.getString(PickupPermissionName, defaultValue);
    }

    public static String getInvClickPermission(ConfigurationSection section, String defaultValue) {
        return section.getString(InvClickPermissionName, defaultValue);
    }

    public static String getNoPlaceMessage(ConfigurationSection section, String defaultValue) {
        return section.getString(NoPlaceMessageName, defaultValue);
    }

    public static String getNoBreakMessage(ConfigurationSection section, String defaultValue) {
        return section.getString(NoBreakMessageName, defaultValue);
    }

    public static String getNoInteractMessage(ConfigurationSection section, String defaultValue) {
        return section.getString(NoInteractMessageName, defaultValue);
    }

    public static String getNoPickupMessage(ConfigurationSection section, String defaultValue) {
        return section.getString(NoPickupMessageName, defaultValue);
    }

    public static String getNoInvClickMessage(ConfigurationSection section, String defaultValue) {
        return section.getString(NoInvClickMessageName, defaultValue);
    }

    // ##############################################################################################

    public static void reloadMainConfigInfo(ConfigurationSection section) {
        FallbackNoPlaceMessage = section.getString(FallbackPlaceMessageName, "&4You don't have permission to place this block!");
        FallbackNoBreakMessage = section.getString(FallbackBreakMessageName, "&4You don't have permission to break this block!");
        FallbackNoInteractMessage = section.getString(FallbackInteractMessageName, "&4You don't have permission to interact with this block!");
    }

    // ########################################## Setters ###########################################

    // no adding/removing will be done... only via the config not commands so setting wont be needed
    // because theres no saving involved, only loading from the config

    // adding/removing using commands will involve saving and... cant be botherd to do that lul

    // ##############################################################################################
}
