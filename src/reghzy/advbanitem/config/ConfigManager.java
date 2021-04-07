package reghzy.advbanitem.config;

import reghzy.advbanitem.AdvancedBanItem;

public class ConfigManager {
    private static Config mainConfig;
    private static Config limitConfig;

    public static void initialise() {
        try {
            FileHelper.ensurePluginFolderExists(AdvancedBanItem.getInstance());
            mainConfig = Config.createInPlugin(AdvancedBanItem.getInstance(), "config.yml");
            mainConfig.loadConfig();
            limitConfig = Config.createInPlugin(AdvancedBanItem.getInstance(), "limits.yml");
            limitConfig.loadConfig();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static Config getMainConfig() {
        return mainConfig;
    }

    public static Config getLimitConfig() {
        return limitConfig;
    }
}
