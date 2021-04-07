package reghzy.advbanitem.config;

import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import reghzy.advbanitem.REghZyBasePlugin;
import reghzy.advbanitem.logs.ChatFormat;
import reghzy.advbanitem.logs.ChatLogger;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

public class Config extends YamlConfiguration {
    private File file;
    private final JavaPlugin plugin;
    private final ChatLogger logger;

    public static Config createInPlugin(REghZyBasePlugin plugin, String name) {
        return new Config(plugin, FileHelper.getFileInDataFolder((Plugin) plugin, name));
    }

    public Config(REghZyBasePlugin plugin, File configFile) {
        this.plugin = (JavaPlugin) plugin;
        this.logger = plugin.getChatLogger();
        this.file = configFile;
    }

    /**
     * Tries to load YAML data from this config's file
     */
    public void loadConfig() {
        String formatName = ChatFormat.apostrophise(this.file.getName());
        this.logger.logPlugin("Loading " + formatName);
        if (file.exists()) {
            try {
                this.map.clear();
                load(this.file);
                logger.logPlugin("Loaded!");
            }
            catch (Exception e) {
                this.logger.logPlugin("Failed to load " + formatName);
                e.printStackTrace();
            }
        }
        else {
            FileHelper.ensurePluginFolderExists(this.plugin);
            logger.logPlugin("Trying to find a default " + formatName);
            InputStream defaultConfig = FileHelper.getDefaultConfig(this.plugin, this.file.getName());
            if (defaultConfig == null) {
                logger.logPlugin("Default config file not found, creating an empty config file");
                try {
                    if (!file.createNewFile()) {
                        logger.logPlugin("Failed to create empty config file");
                    }
                }
                catch (Exception e) {
                    this.logger.logPlugin("Failed to create empty config file");
                    e.printStackTrace();
                }
            }
            else {
                logger.logPlugin("Default " + formatName + " found! Saving to the data folder");
                FileHelper.copyResourceTo(defaultConfig, this.file);
                if (tryLoadYaml()) {
                    logger.logPlugin("Loaded config!");
                }
                else {
                    logger.logPlugin("Failed to load config!");
                }
            }
        }
    }

    /**
     * Tries to save the YAML data to the config file
     */
    public void saveConfig() {
        if (this.trySaveYaml()) {
            logger.logPlugin("Saved " + ChatFormat.apostrophise(file.getName()) + "!");
        }
        else {
            logger.logPlugin("Failed to save " + ChatFormat.apostrophise(file.getName()) + "!");
        }
    }

    public boolean tryLoadYaml() {
        try {
            this.loadConfig();
            return true;
        }
        catch (Exception e) {
            return false;
        }
    }

    public boolean trySaveYaml() {
        try {
            super.save(this.file);
            return true;
        }
        catch (Exception e) {
            return false;
        }
    }
}
