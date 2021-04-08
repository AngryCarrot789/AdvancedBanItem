package reghzy.advbanitem.limit;

import org.bukkit.configuration.ConfigurationSection;
import reghzy.advbanitem.AdvancedBanItem;
import reghzy.advbanitem.config.Config;
import reghzy.advbanitem.logs.ChatFormat;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

/**
 * A class which contains information about blacklisting a specific block
 * from people without permission (or with, if inverted), and disallowing
 * the block in specific worlds (or allowing, if inverted)
 */
public class BlockLimiter {
    public int id;
    public final HashMap<Integer, MetaLimit> metadata;
    public boolean defaultInvertPermission;
    public boolean defaultInvertWorld;
    public List<String> defaultDisallowedWorlds;
    public String defaultPlacePermission;
    public String defaultBreakPermission;
    public String defaultInteractPermission;
    public String defaultNoPlaceMessage;
    public String defaultNoBreakMessage;
    public String defaultNoInteractMessage;

    public BlockLimiter(int id, HashMap<Integer, MetaLimit> metadata,
                        List<String> defaultDisallowedWorlds,
                        boolean defaultInvertPermission,
                        boolean defaultInvertWorld,
                        String defaultPlacePermission,
                        String defaultBreakPermission,
                        String defaultInteractPermission,
                        String defaultNoPlaceMessage,
                        String defaultNoBreakMessage,
                        String defaultNoInteractMessage) {
        this.id                      = id;
        this.metadata                = metadata;
        this.defaultDisallowedWorlds = defaultDisallowedWorlds;
        this.defaultInvertPermission = defaultInvertPermission;
        this.defaultInvertWorld      = defaultInvertWorld;
        this.defaultPlacePermission  = defaultPlacePermission;
        this.defaultBreakPermission  = defaultBreakPermission;
        this.defaultInteractPermission = defaultInteractPermission;
        this.defaultNoPlaceMessage    = defaultNoPlaceMessage;
        this.defaultNoBreakMessage    = defaultNoBreakMessage;
        this.defaultNoInteractMessage = defaultNoInteractMessage;
    }

    public static void reloadMainConfigInfo(Config config) {
        LimitConfigHelper.FallbackNoPlaceMessage = config.getString(LimitConfigHelper.DefaultPlaceMessageName, "&4You don't have permission to place this block!");
        LimitConfigHelper.FallbackNoBreakMessage = config.getString(LimitConfigHelper.DefaultBreakMessageName, "&4You don't have permission to break this block!");
        LimitConfigHelper.FallbackNoInteractMessage = config.getString(LimitConfigHelper.DefaultInteractMessageName, "&4You don't have permission to interact with this block!");
    }

    public static BlockLimiter createFromConfigSection(ConfigurationSection limitedIdSection, int id) {
        List<String> defaultDisallowedWorlds = LimitConfigHelper.getDefaultDisallowedWorlds(limitedIdSection);
        boolean defaultInvertPermissions = LimitConfigHelper.getDefaultInvertPermissions(limitedIdSection);
        boolean defaultInvertWorlds      = LimitConfigHelper.getDefaultInvertWorlds(limitedIdSection);
        String defaultPlacePermission    = LimitConfigHelper.getDefaultPlacePermission(limitedIdSection);
        String defaultBreakPermission    = LimitConfigHelper.getDefaultBreakPermission(limitedIdSection);
        String defaultInteractPermission = LimitConfigHelper.getDefaultInteractPermission(limitedIdSection);
        String defaultPlaceMessage       = LimitConfigHelper.getDefaultPlaceMessage(limitedIdSection);
        String defaultBreakMessage       = LimitConfigHelper.getDefaultBreakMessage(limitedIdSection);
        String defaultInteractMessage    = LimitConfigHelper.getDefaultInteractMessage(limitedIdSection);

        HashMap<Integer, MetaLimit> metaLimits;
        ConfigurationSection metaSections = LimitConfigHelper.getMetadataSection(limitedIdSection);
        if (metaSections == null) {
            metaLimits = new HashMap<Integer, MetaLimit>(1);
            metaLimits.put(-1, new MetaLimit(
                    id, -1, defaultDisallowedWorlds, defaultInvertPermissions, defaultInvertWorlds,
                    defaultPlacePermission, defaultBreakPermission, defaultInteractPermission,
                    defaultPlaceMessage, defaultBreakMessage, defaultInteractMessage));
        }
        else {
            Set<String> metaIds = metaSections.getKeys(false);
            if (metaIds == null || metaIds.size() == 0) {
                metaLimits = new HashMap<Integer, MetaLimit>(1);
                metaLimits.put(-1, new MetaLimit(
                        id, -1, defaultDisallowedWorlds, defaultInvertPermissions, defaultInvertWorlds,
                        defaultPlacePermission, defaultBreakPermission, defaultInteractPermission,
                        defaultPlaceMessage, defaultBreakMessage, defaultInteractMessage));
            }
            else {
                metaLimits = new HashMap<Integer, MetaLimit>(metaIds.size());
                for(String metaKey : metaIds) {
                    try {
                        int meta = Integer.parseInt(metaKey);
                        ConfigurationSection metaSection = metaSections.getConfigurationSection(metaKey);
                        if (metaSection == null) {
                            metaLimits.put(meta, new MetaLimit(
                                    id, meta, defaultDisallowedWorlds,
                                    defaultInvertPermissions, defaultInvertWorlds,
                                    defaultPlacePermission, defaultBreakPermission, defaultInteractPermission,
                                    defaultPlaceMessage, defaultBreakMessage, defaultInteractMessage));
                        }
                        else {
                            List<String> disallowedWorlds = LimitConfigHelper.getDisallowedWorlds(metaSection, defaultDisallowedWorlds);
                            boolean invertPermissions = LimitConfigHelper.getInvertPermissions(metaSection, defaultInvertPermissions);
                            boolean invertWorlds      = LimitConfigHelper.getInvertWorlds(metaSection, defaultInvertWorlds);
                            String placePermission    = LimitConfigHelper.getPlacePermission(metaSection, defaultPlacePermission);
                            String breakPermission    = LimitConfigHelper.getBreakPermission(metaSection, defaultBreakPermission);
                            String interactPermission = LimitConfigHelper.getInteractPermission(metaSection, defaultInteractPermission);
                            String noPlaceMessage     = LimitConfigHelper.getNoPlaceMessage(metaSection, defaultPlaceMessage);
                            String noBreakMessage     = LimitConfigHelper.getNoBreakMessage(metaSection, defaultBreakMessage);
                            String noInteractMessage  = LimitConfigHelper.getNoInteractMessage(metaSection, defaultInteractMessage);
                            metaLimits.put(meta, new MetaLimit(
                                    id, meta, disallowedWorlds,
                                    invertPermissions, invertWorlds,
                                    placePermission, breakPermission, interactPermission,
                                    noPlaceMessage, noBreakMessage, noInteractMessage));
                            WorldLookup.addDisallowed(id, meta, disallowedWorlds);
                        }
                    }
                    catch (Exception e) {
                        AdvancedBanItem.getInstance().getChatLogger().logPlugin(
                                "Failed to parse integer for metadata list: " + ChatFormat.apostrophise(metaKey) +
                                "for ID: " + ChatFormat.apostrophise(String.valueOf(id)));
                    }
                }
            }
        }

        return new BlockLimiter(
                id, metaLimits, defaultDisallowedWorlds,
                defaultInvertPermissions, defaultInvertWorlds,
                defaultPlacePermission, defaultBreakPermission, defaultInteractPermission,
                defaultPlaceMessage, defaultBreakMessage, defaultInteractMessage);
    }

    //public void saveToConfigSection(ConfigurationSection section) {
    //
    //}

    public MetaLimit getMetaMessage(int data) {
        MetaLimit ignore = getIgnoreMeta();
        if (ignore == null) {
            return this.metadata.get(data);
        }
        return ignore;
    }

    public MetaLimit getIgnoreMeta() {
        return this.metadata.get(-1);
    }
}
