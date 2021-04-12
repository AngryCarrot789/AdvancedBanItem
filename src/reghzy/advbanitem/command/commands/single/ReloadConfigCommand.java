package reghzy.advbanitem.command.commands.single;

import org.bukkit.command.CommandSender;
import reghzy.advbanitem.AdvancedBanItem;
import reghzy.advbanitem.config.Config;
import reghzy.advbanitem.config.ConfigManager;
import reghzy.advbanitem.limit.LimitConfigHelper;
import reghzy.advbanitem.limit.LimitManager;
import reghzy.advbanitem.command.CommandDescriptor;
import reghzy.advbanitem.command.ExecutableCommand;
import reghzy.advbanitem.command.helpers.ArgsParser;
import reghzy.advbanitem.command.helpers.ParsedValue;
import reghzy.advbanitem.listeners.PlayerListeners;
import reghzy.advbanitem.logs.ChatFormat;
import reghzy.advbanitem.logs.ChatLogger;

public class ReloadConfigCommand implements ExecutableCommand {
    public static final CommandDescriptor descriptor =
            new CommandDescriptor(
                    "reload",
                    "<config name>",
                    "Reloads the given config. There's main and limits");

    @Override
    public void execute(CommandSender commandSender, ChatLogger logger, String[] args) {
        ParsedValue<String> configName = ArgsParser.parseString(args, 0);
        if (configName.failed) {
            logger.logInfoPrefix("Error with the config name. theres 'main' and 'limits'");
            return;
        }

        if (configName.value.equalsIgnoreCase("main")) {
            if (ConfigManager.getMainConfig().tryLoadYaml()) {
                logger.logInfoPrefix(ChatFormat.green("Successfully reloaded the main config!"));
                PlayerListeners.reloadInfoFromConfig(ConfigManager.getMainConfig());
                AdvancedBanItem.getInstance().getLimitManager().loadInfoFromMainConfig(ConfigManager.getMainConfig());
                LimitConfigHelper.reloadMainConfigInfo(ConfigManager.getMainConfig());
            }
            else {
                logger.logInfoPrefix(ChatFormat.red("Failed to reload the main config!"));
            }
        }

        else if (configName.value.equalsIgnoreCase("limits")) {
            Config config = ConfigManager.getLimitConfig();
            if (config.tryLoadYaml()) {
                logger.logInfoPrefix(ChatFormat.green("Successfully reloaded the limits config!"));
                LimitManager limitManager = AdvancedBanItem.getInstance().getLimitManager();
                limitManager.loadLimits();
                logger.logInfoPrefix(ChatFormat.green("Loaded " + limitManager.limitsCount() + " Block Limits"));
            }
            else {
                logger.logInfoPrefix(ChatFormat.red("Failed to reload the limits config!"));
            }
        }
    }
}
