package reghzy.advbanitem.limit;

import org.bukkit.configuration.ConfigurationSection;
import reghzy.advbanitem.config.Config;

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

    public static final String DefaultDisallowedWorldsName   = "DefaultDisallowedWorlds";
    public static final String DefaultInvertPermissionsName  = "DefaultInvertPermissions";
    public static final String DefaultInvertWorldName        = "DefaultInvertWorlds";
    public static final String DefaultPlacePermissionName    = "DefaultPlacePermission";
    public static final String DefaultBreakPermissionName    = "DefaultBreakPermission";
    public static final String DefaultInteractPermissionName = "DefaultInteractPermission";
    public static final String DefaultPlaceMessageName       = "DefaultPlaceMessage";
    public static final String DefaultBreakMessageName       = "DefaultBreakMessage";
    public static final String DefaultInteractMessageName    = "DefaultInteractMessage";

    public static final String MetaDataName           = "MetaData";
    public static final String DisallowedWorldsName   = "DisallowedWorlds";
    public static final String InvertPermissionsName  = "InvertPermissions";
    public static final String InvertWorldName        = "InvertWorlds";
    public static final String PlacePermissionName    = "PlacePermission";
    public static final String BreakPermissionName    = "BreakPermission";
    public static final String InteractPermissionName = "InteractPermission";
    public static final String NoPlaceMessageName     = "NoPlaceMessage";
    public static final String NoBreakMessageName     = "NoBreakMessage";
    public static final String NoInteractMessageName  = "NoInteractMessage";

    // technically not a constants but still
    public static String FallbackNoPlaceMessage = "&4You don't have permission to place this block!";
    public static String FallbackNoBreakMessage = "&4You don't have permission to break this block!";
    public static String FallbackNoInteractMessage = "&4You don't have permission to interact with this block!";

    // ##################################################################################################

    // --------------------------------------------------------------------------------------------------
    // ##################################### Configuration sections #####################################
    // --------------------------------------------------------------------------------------------------

    public static ConfigurationSection getMetadataSection(ConfigurationSection section) {
        return section.getConfigurationSection(MetaDataName);
    }

    // ##################################################################################################


    // --------------------------------------------------------------------------------------------------
    // ############################### MetaLimits (aka real limits stuff) ###############################
    // --------------------------------------------------------------------------------------------------

    // ---------------------------------------- Getters ----------------------------------------

    public static List<String> getDisallowedWorlds(ConfigurationSection section, List<String> defaultWorlds) {
        List<String> worlds = section.getStringList(DisallowedWorldsName);
        if (worlds == null)
            return defaultWorlds;
        return worlds;
    }

    public static boolean getInvertPermissions(ConfigurationSection section, boolean defaultValue) {
        return section.getBoolean(InvertPermissionsName, defaultValue);
    }

    public static boolean getInvertWorlds(ConfigurationSection section, boolean defaultValue) {
        return section.getBoolean(InvertWorldName, defaultValue);
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

    public static String getNoPlaceMessage(ConfigurationSection section, String defaultValue) {
        return section.getString(NoPlaceMessageName, defaultValue);
    }

    public static String getNoBreakMessage(ConfigurationSection section, String defaultValue) {
        return section.getString(NoBreakMessageName, defaultValue);
    }

    public static String getNoInteractMessage(ConfigurationSection section, String defaultValue) {
        return section.getString(NoInteractMessageName, defaultValue);
    }

    // ##############################################################################################

    // ----------------------------------------------------------------------------------------------
    // ############################ BlockLimiter (aka default stuff) ################################
    // ----------------------------------------------------------------------------------------------

    // ########################################## Getters ###########################################
    public static List<String> getDefaultDisallowedWorlds(ConfigurationSection section) {
        List<String> worlds = section.getStringList(DefaultDisallowedWorldsName);
        if (worlds == null)
            return new ArrayList<String>(0);
        return worlds;
    }

    public static boolean getDefaultInvertPermissions(ConfigurationSection section) {
        return section.getBoolean(DefaultInvertPermissionsName, false);
    }

    public static boolean getDefaultInvertWorlds(ConfigurationSection section) {
        return section.getBoolean(DefaultInvertWorldName, false);
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

    public static String getDefaultPlaceMessage(ConfigurationSection section) {
        return section.getString(DefaultPlaceMessageName, FallbackNoPlaceMessage);
    }

    public static String getDefaultBreakMessage(ConfigurationSection section) {
        return section.getString(DefaultBreakMessageName, FallbackNoBreakMessage);
    }

    public static String getDefaultInteractMessage(ConfigurationSection section) {
        return section.getString(DefaultInteractMessageName, FallbackNoInteractMessage);
    }

    // ##############################################################################################


    // ########################################## Setters ###########################################

    // no adding/removing will be done... only via the config not commands so setting wont be needed
    // because theres no saving involved, only loading from the config

    // adding/removing using commands will involve saving and... cant be botherd to do that lul

    // ##############################################################################################
}
