package reghzy.advbanitem;

import org.bukkit.event.server.PluginEnableEvent;
import reghzy.advbanitem.command.ABICommandExecutor;
import reghzy.api.commands.CommandManager;
import org.bukkit.plugin.java.JavaPlugin;
import reghzy.advbanitem.limit.LimitConfigHelper;
import reghzy.advbanitem.limit.LimitManager;
import reghzy.advbanitem.listeners.BlockListeners;
import reghzy.advbanitem.listeners.PlayerListeners;
import reghzy.api.commands.utils.RZLogger;
import reghzy.api.config.Config;
import reghzy.api.config.ConfigLoadHandler;
import reghzy.api.config.ConfigManager;
import reghzy.api.config.ConfigPreSaveHandler;
import reghzy.api.utils.ExceptionHelper;

public class AdvancedBanItem extends JavaPlugin {
    public static final RZLogger LOGGER = new RZLogger("§6[§4Advanced§cBanItem§6]§r");

    private static AdvancedBanItem INSTANCE;
    private LimitManager limitManager;
    private BlockListeners blockListener;
    private PlayerListeners playerListeners;

    private ConfigManager configManager;

    @Override
    public void onEnable() {
        INSTANCE = this;
        this.configManager = new ConfigManager(this);
        this.configManager.registerResourceConfig("config", new ConfigLoadHandler() {
            @Override
            public void onLoaded(Config config) {
                PlayerListeners.reloadInfoFromConfig(config);
                LimitConfigHelper.reloadMainConfigInfo(config);
                AdvancedBanItem.this.limitManager.loadInfoFromMainConfig(config);
            }
        }, (ConfigPreSaveHandler) null);

        this.configManager.registerResourceConfig("limits", new ConfigLoadHandler() {
            @Override
            public void onLoaded(Config config) {
                AdvancedBanItem.this.limitManager.loadLimits(config);
            }
        }, (ConfigPreSaveHandler) null);

        LOGGER.logFormat("Starting Limit Manager");
        limitManager = new LimitManager();
        LOGGER.logFormat("Starting Block Listener");
        blockListener = new BlockListeners(limitManager, this);
        playerListeners = new PlayerListeners(limitManager, this);

        for (Config config : this.configManager.getConfigs()) {
            try {
                this.configManager.loadConfig(config);
            }
            catch (RuntimeException e) {
                LOGGER.logFormat("Failed to load config '{0}'", config.getConfigName());
                ExceptionHelper.printException(e, LOGGER, true);
            }
        }

        CommandManager.registerMainClass(this, "advbanitem", ABICommandExecutor.class);
    }

    @Override
    public void onDisable() {
        LOGGER.logFormat("Disabled :(");
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
