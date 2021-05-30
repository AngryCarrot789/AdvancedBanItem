package reghzy.advbanitem.limit;

import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.craftbukkit.v1_6_R3.inventory.CraftItemStack;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import reghzy.advbanitem.AdvancedBanItem;
import reghzy.advbanitem.config.Config;
import reghzy.advbanitem.helpers.StringHelper;
import reghzy.advbanitem.logs.ChatFormat;
import reghzy.advbanitem.logs.ChatLogger;
import reghzy.advbanitem.permissions.PermissionsHelper;

import java.util.HashMap;
import java.util.UnknownFormatConversionException;

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
        blockLimiters = new HashMap<Integer, BlockLimiter>(32);
    }

    public void loadLimits() {
        WorldLookup.clearDisallowedWorlds();
        blockLimiters.clear();

        ChatLogger logger = AdvancedBanItem.getInstance().getChatLogger();

        for(String key : this.config.getKeys(false)) {
            try {
                Integer id = StringHelper.parseInteger(key);
                if (id == null) {
                    logger.logPlugin("Failed to parse limiter key as an integer. Key: " + ChatFormat.apostrophise(key));
                    if (StringHelper.countChar(key, ':') > 1) {
                        logger.logPlugin("Make sure you dont put an ID and MetaData in the key, you must define the metadata within the block limiter section (see the example at the top of the config)");
                    }
                }
                else {
                    ConfigurationSection limitSection = this.config.getConfigurationSection(key);
                    if (limitSection == null) {
                        logger.logPlugin("ID " + key + " had nothing in it");
                    }
                    else {
                        try {
                            BlockLimiter limiter = BlockLimiter.createFromConfigSection(limitSection, id, logger);
                            if (limiter != null)
                                addLimiter(id, limiter);
                        }
                        catch (Exception e) {
                            logger.logPlugin("Failed to create a limit from the config");
                            e.printStackTrace();
                        }
                    }
                }
            }
            catch (Exception e) {
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

    //public void saveDataToConfig() {
    //    Config config = this.config;
    //    for(BlockLimiter limiter : this.blockLimiters.values()) {
    //        ConfigurationSection limitedIdSection = config.getConfigurationSection(String.valueOf(limiter.id));
    //        if (limitedIdSection == null) {
    //            limitedIdSection = config.createSection(String.valueOf(limiter.id));
    //        }
    //        limiter.saveToConfigSection(limitedIdSection);
    //    }
    //    if (config.trySaveYaml()) {
    //        ChatLogger.logConsole("Saved 'limits.yml' config");
    //    }
    //    else {
    //        ChatLogger.logConsole("Failed to save 'limits.yml' config");
    //    }
    //}

    public void loadInfoFromMainConfig(Config config) {
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
        BlockLimiter limiter = blockLimiters.get(block.getTypeId());
        if (limiter == null)
            return false;

        MetaLimit meta = limiter.getMetaLimit(block.getData());
        if (meta == null)
            return false;

        if (playerBypassesChecks(player))
            return false;

        if (meta.useNbt) {
            if (meta.tryNbtMatchTree(block).equals(NBTMatchResult.NBT_MATCH_SUCCESS)) {
                if (meta.cancelOnNbtMatch) {
                    sendDenyMessage(player, meta.noBreakMessage, meta);
                    return true;
                }
            }
        }

        if (meta.canBreak(player))
            return false;

        sendDenyMessage(player, meta.noBreakMessage, meta);
        return true;
    }

    // returns true if the event should be cancelled
    public boolean shouldCancelBlockPlace(Player player, Block block) {
        BlockLimiter limiter = blockLimiters.get(block.getTypeId());
        if (limiter == null)
            return false;

        MetaLimit meta = limiter.getMetaLimit(block.getData());
        if (meta == null)
            return false;

        if (playerBypassesChecks(player))
            return false;

        if (meta.useNbt) {
            if (meta.tryNbtMatchTree(block).equals(NBTMatchResult.NBT_MATCH_SUCCESS)) {
                if (meta.cancelOnNbtMatch) {
                    sendDenyMessage(player, meta.noBreakMessage, meta);
                    return true;
                }
            }
        }

        if (meta.canPlace(player))
            return false;

        sendDenyMessage(player, meta.noPlaceMessage, meta);
        return true;
    }

    // returns true if the event should be cancelled
    public boolean shouldCancelInteract(Player player, Block block) {
        BlockLimiter limiter = blockLimiters.get(block.getTypeId());
        if (limiter == null)
            return false;

        MetaLimit meta = limiter.getMetaLimit(block.getData());
        if (meta == null)
            return false;

        if (playerBypassesChecks(player))
            return false;

        if (meta.useNbt) {
            if (meta.tryNbtMatchTree(block).equals(NBTMatchResult.NBT_MATCH_SUCCESS)) {
                if (meta.cancelOnNbtMatch) {
                    sendDenyMessage(player, meta.noBreakMessage, meta);
                    return true;
                }
            }
        }

        if (meta.canInteract(player))
            return false;

        sendDenyMessage(player, meta.noInteractMessage, meta);
        return true;
    }

    // returns true if the event should be cancelled
    public boolean shouldCancelInteract(Player player, int id, byte data) {
        BlockLimiter limiter = blockLimiters.get(id);
        if (limiter == null)
            return false;

        MetaLimit meta = limiter.getMetaLimit(data);
        if (meta == null)
            return false;

        if (playerBypassesChecks(player))
            return false;

        if (meta.canInteract(player))
            return false;

        sendDenyMessage(player, meta.noInteractMessage, meta);
        return true;
    }

    public boolean shouldCancelInteract(Player player, ItemStack itemStack) {
        BlockLimiter limiter = blockLimiters.get(itemStack.getTypeId());
        if (limiter == null)
            return false;

        MetaLimit meta = limiter.getMetaLimit(itemStack.getData().getData());
        if (meta == null)
            return false;

        if (playerBypassesChecks(player))
            return false;

        if (meta.useNbt) {
            if (meta.tryNbtMatchTree(itemStack).equals(NBTMatchResult.NBT_MATCH_SUCCESS)) {
                if (meta.cancelOnNbtMatch) {
                    sendDenyMessage(player, meta.noBreakMessage, meta);
                    return true;
                }
            }
        }

        if (meta.canPickupItem(player))
            return false;

        sendDenyMessage(player, meta.noInteractMessage, meta);
        return true;
    }

    public boolean shouldCancelInventoryClick(Player player, ItemStack itemStack) {
        BlockLimiter limiter = blockLimiters.get(itemStack.getTypeId());
        if (limiter == null)
            return false;

        MetaLimit meta = limiter.getMetaLimit(itemStack.getData().getData());
        if (meta == null)
            return false;

        if (playerBypassesChecks(player))
            return false;

        if (meta.useNbt) {
            if (meta.tryNbtMatchTree(itemStack).equals(NBTMatchResult.NBT_MATCH_SUCCESS)) {
                if (meta.cancelOnNbtMatch) {
                    sendDenyMessage(player, meta.noBreakMessage, meta);
                    return true;
                }
            }
        }

        if (meta.canClickInventory(player))
            return false;

        sendDenyMessage(player, meta.noInvClickMessage, meta);
        return true;
    }

    public boolean shouldCancelPickup(Player player, Item item) {
        ItemStack stack = item.getItemStack();
        BlockLimiter limiter = blockLimiters.get(stack.getTypeId());
        if (limiter == null)
            return false;

        MetaLimit meta = limiter.getMetaLimit(stack.getData().getData());
        if (meta == null)
            return false;

        if (playerBypassesChecks(player))
            return false;

        if (meta.useNbt) {
            ItemStack itemStack = item.getItemStack();
            if (itemStack != null) {
                if (meta.tryNbtMatchTree(itemStack).equals(NBTMatchResult.NBT_MATCH_SUCCESS)) {
                    if (meta.cancelOnNbtMatch) {
                        sendDenyMessage(player, meta.noBreakMessage, meta);
                        return true;
                    }
                }
            }
        }

        if (meta.canPickupItem(player))
            return false;

        sendDenyMessage(player, meta.noPickupMessage, meta);
        return true;
    }

    public boolean playerBypassesChecks(Player player) {
        if (allowPermsBypass && PermissionsHelper.hasPermission(player, permsBypassPermission))
            return true;
        return allowWorldBypass && PermissionsHelper.hasPermission(player, worldBypassPermission);
    }

    public int limitsCount() {
        return this.blockLimiters.size();
    }

    public static void sendDenyMessage(Player player, String message, MetaLimit metaUsedForInformation) {
        player.sendMessage(AdvancedBanItem.getInstance().getChatPrefix() + " " + translateWildcards(message, metaUsedForInformation, player));
    }

    public static String translateWildcards(String message, MetaLimit meta, Player player) {
        StringBuilder newMessage = new StringBuilder(message.length() * 4);
        for(int i = 0; i < message.length(); i++) {
            char c = message.charAt(i);
            if (c == '%') {
                newMessage.append(getLimiterWildcard(message.charAt(++i), meta, player));
            }
            else if (c == '&') {
                newMessage.append((char) 167).append(message.charAt(++i));
            }
            else {
                newMessage.append(c);
            }
        }
        return newMessage.toString();
    }

    public static String getLimiterWildcard(char wildcard, MetaLimit meta, Player player) {
        if (wildcard == 'p')
            return nullCheckPermission(meta.placePermission);
        if (wildcard == 'b')
            return nullCheckPermission(meta.breakPermission);
        if (wildcard == 'i')
            return nullCheckPermission(meta.interactPermission);
        if (wildcard == 'u')
            return player.getName();
        if (wildcard == 'w') {
            World world = player.getWorld();
            if (world != null) {
                String name = world.getName();
                if (name != null)
                    return name;
            }
            return "[Unknown world]";
        }
        return "";
    }

    private static String nullCheckPermission(String permission) {
        if (permission == null)
            return "[No permission]";
        return permission;
    }
}
