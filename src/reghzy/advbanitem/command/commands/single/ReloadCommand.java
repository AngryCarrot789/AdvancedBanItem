package reghzy.advbanitem.command.commands.single;

import org.bukkit.command.CommandSender;
import reghzy.advbanitem.AdvancedBanItem;
import reghzy.advbanitem.command.ABIPermission;
import reghzy.mfunclagfind.command.ExecutableCommand;
import reghzy.mfunclagfind.command.ITabCompletable;
import reghzy.mfunclagfind.command.utils.CommandArgs;
import reghzy.mfunclagfind.command.utils.REghZyLogger;
import reghzy.mfunclagfind.config.Config;
import reghzy.mfunclagfind.permissions.IPermission;
import reghzy.mfunclagfind.utils.memory.Lists;

import java.util.List;

public class ReloadCommand extends ExecutableCommand implements ITabCompletable {
    public ReloadCommand() {
        super("abi", null, "reload", "[config name] or [all]", "Reloads a given config (aka, loads it into ram)");
    }

    @Override
    public IPermission getPermission() {
        return ABIPermission.reload;
    }

    @Override
    public void execute(CommandSender sender, REghZyLogger logger, CommandArgs args) {
        if (args.getArgsLength() == 0) {
            logger.logPrefix("You haven't provided a config name!");
            logger.logPrefix("Available configs: ");
            for (String name : AdvancedBanItem.getInstance().getConfigManager().getConfigNames()) {
                logger.logPrefixTranslate("  &3" + name);
            }

            logger.logPrefixTranslate("&6(could also do &3/abi reload all&6)");
            return;
        }

        String configName = args.getString(0);
        if (configName == null || configName.isEmpty()) {
            logger.logPrefix("You haven't provided a config name!");
            logger.logPrefix("Available configs: ");
            for (String name : AdvancedBanItem.getInstance().getConfigManager().getConfigNames()) {
                logger.logPrefixTranslate("  &3" + name);
            }

            logger.logPrefixTranslate("&6(could also do &3/abi reload all&6)");
            return;
        }

        if (configName.equalsIgnoreCase("all")) {
            for (String name : AdvancedBanItem.getInstance().getConfigManager().getConfigNames()) {
                if (AdvancedBanItem.getInstance().getConfigManager().loadConfig(AdvancedBanItem.getInstance().getConfigManager().getConfig(name))) {
                    logger.logPrefixTranslate("&6Loaded '&3" + name + "&6'");
                }
                else {
                    logger.logPrefixTranslate("&6Failed to load '&3" + configName + "&6'");
                }
            }

            logger.logPrefix("Reloaded all configs!");
            return;
        }

        Config config = AdvancedBanItem.getInstance().getConfigManager().getConfig(configName);
        if (config == null) {
            logger.logPrefix("That config doesn't exist!");
            return;
        }

        if (AdvancedBanItem.getInstance().getConfigManager().loadConfig(config)) {
            logger.logPrefixTranslate("&6Loaded '&3" + configName + "&6'");
        }
        else {
            logger.logPrefixTranslate("&6Failed to load '&3" + configName + "&6'");
        }
    }

    @Override
    public List<String> doTabComplete(CommandSender sender, REghZyLogger logger, CommandArgs args, int argsIndex) {
        return Lists.stringStartsWith(AdvancedBanItem.getInstance().getConfigManager().getConfigNames(), args.getString(0, ""));
    }
}
