package reghzy.advbanitem;

import org.bukkit.ChatColor;
import org.bukkit.plugin.java.JavaPlugin;
import reghzy.advbanitem.command.MainCommandExecutor;
import reghzy.advbanitem.config.ConfigManager;
import reghzy.advbanitem.config.FileHelper;
import reghzy.advbanitem.limit.LimitConfigHelper;
import reghzy.advbanitem.limit.LimitManager;
import reghzy.advbanitem.listeners.BlockListeners;
import reghzy.advbanitem.listeners.PlayerListeners;
import reghzy.advbanitem.logs.ChatFormat;
import reghzy.advbanitem.logs.ChatLogger;
import reghzy.advbanitem.permissions.PermissionsHelper;

public class AdvancedBanItem extends JavaPlugin implements REghZyBasePlugin {
    private ChatLogger chatLogger = new ChatLogger(this, null);

    private static AdvancedBanItem instance;
    public static final String VERSION = "1.3.54";

    private LimitManager limitManager;
    private BlockListeners blockListener;
    private PlayerListeners playerListeners;

    public static final String CommandsPermission       = "advbanitem.perms.commands";
    public static final String AddBlockPermission       = "advbanitem.perms.commands.add";
    public static final String RemoveBlockPermission    = "advbanitem.perms.commands.remove";
    public static final String EditBlockPermission      = "advbanitem.perms.commands.edit";
    public static final String ReloadConfigPermission   = "advbanitem.perms.commands.reload";
    public static final String DisplayLimiterPermission = "advbanitem.perms.commands.display";
    public static final String LookHandInfoPermission   = "advbanitem.perms.iteminfo";

    @Override
    public void onEnable() {
        instance = this;
        this.chatLogger = new ChatLogger(this, null);

        PermissionsHelper.init();

        FileHelper.ensurePluginFolderExists(this);

        this.getCommand(getCommandPrefix()).setExecutor(new MainCommandExecutor());

        try {
            ConfigManager.initialise();
            LimitConfigHelper.reloadMainConfigInfo(ConfigManager.getMainConfig());
            chatLogger.logPlugin("Starting Limit Manager");
            limitManager = new LimitManager(ConfigManager.getLimitConfig());
            chatLogger.logPlugin("Starting Block Listener");
            blockListener = new BlockListeners(limitManager, this);
            playerListeners = new PlayerListeners(limitManager, this);

            try {
                chatLogger.logPlugin("Loading Limit Manager Limits...");
                init();
                chatLogger.logPlugin("Success!");
            }
            catch (Exception e) {
                chatLogger.logPlugin("Failed to initialise Limit Manager");
                e.printStackTrace();
            }

            PlayerListeners.reloadInfoFromConfig(ConfigManager.getMainConfig());
        }
        catch (Exception e) {
            this.chatLogger.logPlugin("Failed to initialise config manager. Cannot load plugin without it.\n" +
                                      "If you have plugman installed, try using that to load this plugin again");
            e.printStackTrace();
        }
    }

    public void init() {
        limitManager.loadInfoFromMainConfig(ConfigManager.getMainConfig());
        limitManager.loadLimits();
    }

    public LimitManager getLimitManager() {
        return this.limitManager;
    }

    @Override
    public void onDisable() {
        chatLogger.logPlugin(getChatPrefix() + ChatFormat.red(" Disabled"));
    }

    @Override
    public ChatLogger getChatLogger() {
        return this.chatLogger;
    }

    @Override
    public String getChatPrefix() {
        return ChatColor.GOLD + "[" + ChatColor.DARK_AQUA + "Advanced" + ChatColor.RED + "BanItem" + ChatColor.GOLD + "]" + ChatColor.RESET;
    }

    @Override
    public String getCommandPrefix() {
        return "abi";
    }

    public static AdvancedBanItem getInstance() {
        return instance;
    }
}
