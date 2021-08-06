package reghzy.advbanitem;

import org.bukkit.plugin.java.JavaPlugin;
import reghzy.advbanitem.command.ABICommandExecutor;
import reghzy.advbanitem.limit.LimitConfigHelper;
import reghzy.advbanitem.limit.LimitManager;
import reghzy.advbanitem.listeners.BlockListeners;
import reghzy.advbanitem.listeners.PlayerListeners;
import reghzy.mfunclagfind.command.CommandManager;
import reghzy.mfunclagfind.command.utils.REghZyLogger;
import reghzy.mfunclagfind.config.Config;
import reghzy.mfunclagfind.config.ConfigLoadHandler;
import reghzy.mfunclagfind.config.ConfigManager;

public class AdvancedBanItem extends JavaPlugin {
    public static final REghZyLogger LOGGER = new REghZyLogger("§6[§4Advanced§cBanItem§6]§r");

    private static AdvancedBanItem INSTANCE;
    private LimitManager limitManager;
    private BlockListeners blockListener;
    private PlayerListeners playerListeners;

    private ConfigManager configManager;

    @Override
    public void onEnable() {
        INSTANCE = this;

        this.configManager = new ConfigManager(this);
        this.configManager.registerDefaultConfig("config", new ConfigLoadHandler() {
            @Override
            public void onLoaded(Config config) {
                PlayerListeners.reloadInfoFromConfig(config);
                LimitConfigHelper.reloadMainConfigInfo(config);
                AdvancedBanItem.this.limitManager.loadInfoFromMainConfig(config);
            }
        }, null);

        this.configManager.registerDefaultConfig("limits", new ConfigLoadHandler() {
            @Override
            public void onLoaded(Config config) {
                AdvancedBanItem.this.limitManager.loadLimits(config);
            }
        }, null);

        LOGGER.logPrefix("Starting Limit Manager");
        limitManager = new LimitManager();
        LOGGER.logPrefix("Starting Block Listener");
        blockListener = new BlockListeners(limitManager, this);
        playerListeners = new PlayerListeners(limitManager, this);

        this.configManager.loadAllConfigs();

        CommandManager.registerMainClass(this, "advbanitem", ABICommandExecutor.class);
    }

    @Override
    public void onDisable() {
        LOGGER.logPrefix("Disabled :(");
    }

    public LimitManager getLimitManager() {
        return this.limitManager;
    }

    public static AdvancedBanItem getInstance() {
        return INSTANCE;
    }

    public ConfigManager getConfigManager() {
        return configManager;
    }
}
