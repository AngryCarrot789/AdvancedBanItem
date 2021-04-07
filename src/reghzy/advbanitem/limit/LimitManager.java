package reghzy.advbanitem.limit;

import org.bukkit.block.Block;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import reghzy.advbanitem.AdvancedBanItem;
import reghzy.advbanitem.config.Config;
import reghzy.advbanitem.helpers.PermissionsHelper;
import reghzy.advbanitem.logs.ChatFormat;
import reghzy.advbanitem.logs.ChatLogger;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

public class LimitManager {
    private final HashMap<Integer, BlockLimiter> blockLimiters;
    public boolean allowWorldBypass = false;
    public boolean allowPermsBypass = false;
    public String worldBypassPermission = null;
    public String permsBypassPermission = null;
    public final Config config;

    public static final String AllowWorldBypassName = "AllowWorldPermissionBypass";
    public static final String AllowPermsBypassName = "AllowPermsPermissionBypass";
    public static final String WorldBypassName = "BypassWorldPermission";
    public static final String PermsBypassName = "BypassPermsPermission";

    public LimitManager(Config config) {
        this.config = config;
        blockLimiters = new HashMap<Integer, BlockLimiter>(16);
    }

    public void loadLimits() {
        WorldLookup.clearBlockWorlds();
        blockLimiters.clear();

        for(String key : this.config.getKeys(false)) {
            try {
                int id = Integer.parseInt(key);
                ConfigurationSection limitSection = this.config.getConfigurationSection(key);

                List<String> metadataStrings = limitSection.getStringList(BlockLimiter.MetaDataName);
                HashSet<Integer> metadata;
                if (metadataStrings == null || metadataStrings.size() == 0) {
                    metadata = new HashSet<Integer>(1);
                    metadata.add(-1);
                }
                else {
                    metadata = new HashSet<Integer>(metadataStrings.size());
                    for (String metadataString : metadataStrings) {
                        try {
                            Integer data = Integer.parseInt(metadataString);
                            metadata.add(data);
                        }
                        catch (Exception e) {
                            AdvancedBanItem.getInstance().getChatLogger().logPlugin(
                                    "Failed to parse integer for metadata list: " + ChatFormat.apostrophise(metadataString) +
                                    "for ID: " + ChatFormat.apostrophise(key));
                        }
                    }
                }

                addLimiter(
                        id,
                        new BlockLimiter(
                                id,
                                metadata,
                                limitSection.getBoolean(BlockLimiter.InvertMetadataName),
                                limitSection.getBoolean(BlockLimiter.InvertPermissionsName),
                                limitSection.getBoolean(BlockLimiter.InvertWorldName),
                                limitSection.getStringList(BlockLimiter.DisallowedWorldsName),
                                limitSection.getString(BlockLimiter.PlacePermissionName),
                                limitSection.getString(BlockLimiter.BreakPermissionName),
                                limitSection.getString(BlockLimiter.InteractPermissionName),
                                limitSection.getString(BlockLimiter.NoPlaceMessageName),
                                limitSection.getString(BlockLimiter.NoBreakMessageName),
                                limitSection.getString(BlockLimiter.NoInteractMessageName)));
            }
            catch (Exception e) {
                AdvancedBanItem.getInstance().getChatLogger().logPlugin("Failed to load block limiter. Block ID: " + key);
                e.printStackTrace();
            }
        }
    }

    public HashMap<Integer, BlockLimiter> getBlockLimiters() {
        return this.blockLimiters;
    }

    public void addLimiter(int id, BlockLimiter limiter) {
        blockLimiters.put(id, limiter);
    }

    public BlockLimiter removeLimiter(int id) {
        return blockLimiters.remove(id);
    }

    public BlockLimiter getLimiter(int id) {
        return blockLimiters.get(id);
    }

    public void saveDataToConfig() {
        Config config = this.config;

        for(BlockLimiter limiter : this.blockLimiters.values()) {
            ConfigurationSection section = config.getConfigurationSection(String.valueOf(limiter.id));
            if (section == null) {
                section = config.createSection(String.valueOf(limiter.id));
            }

            config.set("", null);
            ArrayList<String> worldNames = WorldLookup.getWorldNamesFromId(limiter.id);
            if (worldNames == null)
                worldNames = new ArrayList<String>(0);
            List<String> metadata = new ArrayList<String>(limiter.metadata.size());
            for(Integer data : limiter.metadata) {
                metadata.add(data.toString());
            }
            section.set(BlockLimiter.MetaDataName, metadata);
            section.set(BlockLimiter.InvertPermissionsName, limiter.invertPermission);
            section.set(BlockLimiter.InvertWorldName, limiter.invertWorld);
            section.set(BlockLimiter.DisallowedWorldsName, worldNames);
            section.set(BlockLimiter.PlacePermissionName, limiter.placePermission);
            section.set(BlockLimiter.BreakPermissionName, limiter.breakPermission);
            section.set(BlockLimiter.InteractPermissionName, limiter.interactPermission);
            section.set(BlockLimiter.NoPlaceMessageName, limiter.noPlaceMessage);
            section.set(BlockLimiter.NoBreakMessageName, limiter.noBreakMessage);
            section.set(BlockLimiter.NoInteractMessageName, limiter.noInteractMessage);
        }

        if (config.trySaveYaml()) {
            ChatLogger.logConsole("Saved 'limits.yml' config");
        }
        else {
            ChatLogger.logConsole("Failed to save 'limits.yml' config");
        }
    }

    public void reloadInfoFromConfig(Config config) {
        this.allowWorldBypass = config.getBoolean(LimitManager.AllowWorldBypassName);
        this.allowPermsBypass = config.getBoolean(LimitManager.AllowPermsBypassName);
        this.worldBypassPermission = config.getString(LimitManager.WorldBypassName);
        this.permsBypassPermission = config.getString(LimitManager.PermsBypassName);

        if (this.allowWorldBypass && (this.worldBypassPermission == null))
            this.allowWorldBypass = false;
        if (this.allowPermsBypass && (this.permsBypassPermission == null))
            this.allowPermsBypass = false;
    }

    // returns true if the event should be cancelled
    public boolean shouldCancelBlockBreak(Player player, Block block) {
        if (playerBypassesChecks(player))
            return false;

        BlockLimiter limiter = blockLimiters.get(block.getTypeId());
        if (limiter == null)
            return false;
        if (limiter.hasMetadata(-1) || limiter.hasMetadata(block.getData())) {
            if (limiter.canBreak(player))
                return false;

            sendDenyMessage(player, limiter.noBreakMessage, limiter);
            return true;
        }
        return false;
    }

    // returns true if the event should be cancelled
    public boolean shouldCancelBlockPlace(Player player, Block block) {
        if (playerBypassesChecks(player))
            return false;

        BlockLimiter limiter = blockLimiters.get(block.getTypeId());
        if (limiter == null)
            return false;

        if (limiter.hasMetadata(-1) || limiter.hasMetadata(block.getData())) {
            if (limiter.canPlace(player))
                return false;

            sendDenyMessage(player, limiter.noPlaceMessage, limiter);
            return true;
        }
        return false;
    }

    // returns true if the event should be cancelled
    public boolean shouldCancelBlockInteract(Player player, Block block) {
        return shouldCancelBlockInteract(player, block.getTypeId(), block.getData());
    }

    // returns true if the event should be cancelled
    public boolean shouldCancelBlockInteract(Player player, int id, byte data) {
        if (playerBypassesChecks(player))
            return false;

        BlockLimiter limiter = blockLimiters.get(id);
        if (limiter == null)
            return false;

        if (limiter.hasMetadata(-1) || limiter.hasMetadata(data)) {
            if (limiter.canInteract(player))
                return false;

            sendDenyMessage(player, limiter.noInteractMessage, limiter);
            return true;
        }
        return false;
    }

    public boolean playerBypassesChecks(Player player) {
        if (allowWorldBypass && PermissionsHelper.hasPermission(player, worldBypassPermission))
            return true;
        return allowPermsBypass && PermissionsHelper.hasPermission(player, permsBypassPermission);
    }

    public int limitsCount() {
        return this.blockLimiters.size();
    }

    public static void sendDenyMessage(Player player, String message, BlockLimiter limiter) {
        player.sendMessage(AdvancedBanItem.getInstance().getChatPrefix() + " " + translateWildcards(message, limiter));
    }

    public static String translateWildcards(String message, BlockLimiter limiter) {
        StringBuilder newMessage = new StringBuilder(message.length() * 4);
        for(int i = 0; i < message.length(); i++) {
            char c = message.charAt(i);
            if (c == '%') {
                newMessage.append(getLimiterWildcard(message.charAt(++i), limiter));
            }
            else if (c == '&') {
                newMessage.append((char) 167).append(message.charAt(++i))
            }
            else {
                newMessage.append(c);
            }
        }
        return newMessage.toString();
    }

    public static String getLimiterWildcard(char wildcard, BlockLimiter limiter) {
        if (wildcard == 'p')
            return (limiter.placePermission == null) ? "[none]" : limiter.placePermission;
        if (wildcard == 'b')
            return (limiter.breakPermission == null) ? "[none]" : limiter.breakPermission;
        if (wildcard == 'i')
            return (limiter.interactPermission == null) ? "[none]" : limiter.interactPermission;
        return "";
    }
}
