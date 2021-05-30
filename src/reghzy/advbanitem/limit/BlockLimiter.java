package reghzy.advbanitem.limit;

import org.bukkit.ChatColor;
import org.bukkit.configuration.ConfigurationSection;
import reghzy.advbanitem.helpers.StringHelper;
import reghzy.advbanitem.logs.ChatFormat;
import reghzy.advbanitem.logs.ChatLogger;

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
    public final int id;
    public final HashMap<Integer, MetaLimit> metadata;
    public final boolean defaultInvertPermission;
    public final boolean defaultInvertWorld;
    public final List<String> defaultDisallowedWorlds;
    public final String defaultPlacePermission;
    public final String defaultBreakPermission;
    public final String defaultInteractPermission;
    public final String pickupPermission;
    public final String clickInvPermission;
    public final String defaultNoPlaceMessage;
    public final String defaultNoBreakMessage;
    public final String defaultNoInteractMessage;
    public final String defaultNoPickupMessage;
    public final String defaultNoInvClickMessage;

    public BlockLimiter(int id, HashMap<Integer, MetaLimit> metadata,
                        List<String> defaultDisallowedWorlds,
                        boolean defaultInvertPermission,
                        boolean defaultInvertWorld,
                        String defaultPlacePermission,
                        String defaultBreakPermission,
                        String defaultInteractPermission,
                        String pickupPermission,
                        String clickInvPermission,
                        String defaultNoPlaceMessage,
                        String defaultNoBreakMessage,
                        String defaultNoInteractMessage,
                        String defaultNoPickupMessage, String defaultNoInvClickMessage) {
        this.id                        = id;
        this.metadata                  = metadata;
        this.defaultDisallowedWorlds   = defaultDisallowedWorlds;
        this.defaultInvertPermission   = defaultInvertPermission;
        this.defaultInvertWorld        = defaultInvertWorld;
        this.defaultPlacePermission    = defaultPlacePermission;
        this.defaultBreakPermission    = defaultBreakPermission;
        this.defaultInteractPermission = defaultInteractPermission;
        this.pickupPermission = pickupPermission;
        this.clickInvPermission = clickInvPermission;
        this.defaultNoPlaceMessage     = defaultNoPlaceMessage;
        this.defaultNoBreakMessage     = defaultNoBreakMessage;
        this.defaultNoInteractMessage  = defaultNoInteractMessage;
        this.defaultNoPickupMessage    = defaultNoPickupMessage;
        this.defaultNoInvClickMessage  = defaultNoInvClickMessage;
    }

    public static BlockLimiter createFromConfigSection(ConfigurationSection limitedIdSection, int id, ChatLogger logger) {
        List<String> defaultDisallowedWorlds = LimitConfigHelper.getDefaultDisallowedWorlds(limitedIdSection);
        boolean defaultInvertPermissions     = LimitConfigHelper.getDefaultInvertPermissions(limitedIdSection);
        boolean defaultInvertWorlds          = LimitConfigHelper.getDefaultInvertWorlds(limitedIdSection);
        boolean defaultUseNbt                = LimitConfigHelper.getDefaultUseNBT(limitedIdSection);
        List<String> defaultNbtFilters       = LimitConfigHelper.getDefaultNbtFilters(limitedIdSection);
        String defaultPlacePermission        = LimitConfigHelper.getDefaultPlacePermission(limitedIdSection);
        String defaultBreakPermission        = LimitConfigHelper.getDefaultBreakPermission(limitedIdSection);
        String defaultInteractPermission     = LimitConfigHelper.getDefaultInteractPermission(limitedIdSection);
        String defaultPickupPermission       = LimitConfigHelper.getDefaultPickupPermission(limitedIdSection);
        String defaultInvClickPermission     = LimitConfigHelper.getDefaultInvClickPermission(limitedIdSection);
        String defaultPlaceMessage           = LimitConfigHelper.getDefaultPlaceMessage(limitedIdSection);
        String defaultBreakMessage           = LimitConfigHelper.getDefaultBreakMessage(limitedIdSection);
        String defaultInteractMessage        = LimitConfigHelper.getDefaultInteractMessage(limitedIdSection);
        String defaultNoPickupMessage        = LimitConfigHelper.getDefaultNoPickupMessage(limitedIdSection);
        String defaultNoInvClickMessage      = LimitConfigHelper.getDefaultNoInvClickMessage(limitedIdSection);

        HashMap<Integer, MetaLimit> metaLimits;
        ConfigurationSection metaSections = LimitConfigHelper.getMetadataSection(limitedIdSection);
        if (metaSections == null) {
            metaLimits = new HashMap<Integer, MetaLimit>(1);
            metaLimits.put(-1, new MetaLimit(
                    id, -1, defaultDisallowedWorlds,
                    defaultInvertPermissions, defaultInvertWorlds, false, new ArrayList<String>(), false,
                    defaultPlacePermission, defaultBreakPermission, defaultInteractPermission,
                    defaultPickupPermission == null ? defaultInteractPermission : defaultPickupPermission,
                    defaultInvClickPermission == null ? defaultInteractPermission : defaultInvClickPermission,
                    defaultPlaceMessage, defaultBreakMessage, defaultInteractMessage,
                    defaultNoPickupMessage == null ? defaultInteractMessage : defaultNoPickupMessage,
                    defaultNoInvClickMessage == null ? defaultInteractMessage : defaultNoInvClickMessage));
        }
        else {
            Set<String> metaIds = metaSections.getKeys(false);
            if (metaIds == null || metaIds.size() == 0) {
                metaLimits = new HashMap<Integer, MetaLimit>(1);
                metaLimits.put(-1, new MetaLimit(
                        id, -1, defaultDisallowedWorlds,
                        defaultInvertPermissions, defaultInvertWorlds, false, new ArrayList<String>(), false,
                        defaultPlacePermission, defaultBreakPermission, defaultInteractPermission,
                        defaultPickupPermission == null ? defaultInteractPermission : defaultPickupPermission,
                        defaultInvClickPermission == null ? defaultInteractPermission : defaultInvClickPermission,
                        defaultPlaceMessage, defaultBreakMessage, defaultInteractMessage,
                        defaultNoPickupMessage == null ? defaultInteractMessage : defaultNoPickupMessage,
                        defaultNoInvClickMessage == null ? defaultInteractMessage : defaultNoInvClickMessage));
            }
            else {
                metaLimits = new HashMap<Integer, MetaLimit>(metaIds.size());
                for (String metaKey : metaIds) {
                    Integer meta = StringHelper.parseInteger(metaKey);
                    if (meta == null) {
                        logger.logPlugin(ChatColor.RED + "Failed to parse integer for metadata list: " + ChatFormat.apostrophise(metaKey) + " for ID: " + ChatFormat.apostrophise(String.valueOf(id)));
                        logger.logPlugin(ChatColor.RED + "Ignoring that limit");
                        return null;
                    }

                    ConfigurationSection metaSection = metaSections.getConfigurationSection(metaKey);
                    if (metaSection == null) {
                        metaLimits.put(meta, new MetaLimit(
                                id, meta, defaultDisallowedWorlds,
                                defaultInvertPermissions, defaultInvertWorlds, false, new ArrayList<String>(), false,
                                defaultPlacePermission, defaultBreakPermission, defaultInteractPermission,
                                defaultPickupPermission == null ? defaultInteractPermission : defaultPickupPermission,
                                defaultInvClickPermission == null ? defaultInteractPermission : defaultInvClickPermission,
                                defaultPlaceMessage, defaultBreakMessage, defaultInteractMessage,
                                defaultNoPickupMessage == null ? defaultInteractMessage : defaultNoPickupMessage,
                                defaultNoInvClickMessage == null ? defaultInteractMessage : defaultNoInvClickMessage));
                    }
                    else {
                        List<String> disallowedWorlds = LimitConfigHelper.getDisallowedWorlds(metaSection, defaultDisallowedWorlds);
                        boolean invertPermissions = LimitConfigHelper.getInvertPermissions(metaSection, defaultInvertPermissions);
                        boolean invertWorlds      = LimitConfigHelper.getInvertWorlds(metaSection, defaultInvertWorlds);
                        boolean useNbt            = LimitConfigHelper.getUseNbt(metaSection, defaultUseNbt);
                        boolean cancelOnNbtMatch  = LimitConfigHelper.getCancelEventOnNBTMatch(metaSection, false);
                        List<String> nbtFilters   = LimitConfigHelper.getNbtFilters(metaSection, defaultNbtFilters);
                        String placePermission    = LimitConfigHelper.getPlacePermission(metaSection, defaultPlacePermission);
                        String breakPermission    = LimitConfigHelper.getBreakPermission(metaSection, defaultBreakPermission);
                        String interactPermission = LimitConfigHelper.getInteractPermission(metaSection, defaultInteractPermission);
                        String pickupPermission   = LimitConfigHelper.getPickupPermission(metaSection, defaultPickupPermission == null ? interactPermission : defaultPickupPermission);
                        String invClickPermission = LimitConfigHelper.getInvClickPermission(metaSection, defaultInvClickPermission == null ? interactPermission : defaultInvClickPermission);
                        String noPlaceMessage     = LimitConfigHelper.getNoPlaceMessage(metaSection, defaultPlaceMessage);
                        String noBreakMessage     = LimitConfigHelper.getNoBreakMessage(metaSection, defaultBreakMessage);
                        String noInteractMessage  = LimitConfigHelper.getNoInteractMessage(metaSection, defaultInteractMessage);
                        String noPickupMessage    = LimitConfigHelper.getNoPickupMessage(metaSection, defaultNoPickupMessage == null ? noInteractMessage : defaultNoPickupMessage);
                        String noInvClickMessage  = LimitConfigHelper.getNoInvClickMessage(metaSection, defaultNoInvClickMessage == null ? noInteractMessage : defaultNoInvClickMessage);
                        metaLimits.put(meta, new MetaLimit(
                                id, meta, disallowedWorlds,
                                invertPermissions, invertWorlds,
                                useNbt, nbtFilters, cancelOnNbtMatch,
                                placePermission, breakPermission, interactPermission,
                                pickupPermission, invClickPermission,
                                noPlaceMessage, noBreakMessage, noInteractMessage,
                                noPickupMessage, noInvClickMessage));
                    }
                }
            }
        }

        return new BlockLimiter(
                id, metaLimits, defaultDisallowedWorlds,
                defaultInvertPermissions, defaultInvertWorlds,
                defaultPlacePermission, defaultBreakPermission, defaultInteractPermission,
                defaultPickupPermission == null ? defaultInteractPermission : defaultPickupPermission,
                defaultInvClickPermission == null ? defaultInteractPermission : defaultInvClickPermission,
                defaultPlaceMessage, defaultBreakMessage, defaultInteractMessage,
                defaultNoPickupMessage == null ? defaultInteractMessage : defaultNoPickupMessage,
                defaultNoInvClickMessage == null ? defaultInteractMessage : defaultNoInvClickMessage);
    }

    //public void saveToConfigSection(ConfigurationSection section) {
    //
    //}

    public MetaLimit getMetaLimit(int data) {
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
