package reghzy.advbanitem.command.commands.single;

import org.bukkit.command.CommandSender;
import reghzy.advbanitem.AdvancedBanItem;
import reghzy.advbanitem.config.Config;
import reghzy.advbanitem.config.ConfigManager;
import reghzy.advbanitem.limit.LimitManager;
import reghzy.advbanitem.listeners.BlockListener;
import reghzy.advbanitem.command.CommandDescriptor;
import reghzy.advbanitem.command.ExecutableCommand;
import reghzy.advbanitem.command.helpers.ArgsParser;
import reghzy.advbanitem.command.helpers.ParsedValue;
import reghzy.advbanitem.logs.ChatFormat;
import reghzy.advbanitem.logs.ChatLogger;

public class ReloadConfigCommand implements ExecutableCommand {
    public static CommandDescriptor descriptor =
            new CommandDescriptor(
                    "reload",
                    "<config name>",
                    "reloads the given config");

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
                BlockListener.reloadInfoFromConfig(ConfigManager.getMainConfig());
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
                limitManager.reloadInfoFromConfig(config);
                limitManager.loadLimits();
                logger.logInfoPrefix(ChatFormat.green("Loaded " + limitManager.limitsCount() + " Block Limits"));
            }
            else {
                logger.logInfoPrefix(ChatFormat.red("Failed to reload the limits config!"));
            }
        }
    }
}
