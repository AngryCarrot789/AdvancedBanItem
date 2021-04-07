package reghzy.advbanitem.command;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import reghzy.advbanitem.AdvancedBanItem;
import reghzy.advbanitem.command.commands.single.*;
import reghzy.advbanitem.command.helpers.ArgsParser;
import reghzy.advbanitem.permissions.PermissionsHelper;
import reghzy.advbanitem.logs.ChatFormat;
import reghzy.advbanitem.logs.ChatLogger;

import java.util.HashMap;
import java.util.Map;

public class MainCommandExecutor implements CommandExecutor {
    public final Map<String, ExecutableCommand> commandMap;

    public MainCommandExecutor() {
        commandMap = new HashMap<String, ExecutableCommand>();
        commandMap.put("help", new HelpCommand());
        commandMap.put("look", new LookBlockInfoCommand());
        commandMap.put("add", new AddBlockCommand());
        commandMap.put("remove", new RemoveBlockCommand());
        commandMap.put("edit", new EditBlockLimitCommands());
        commandMap.put("display", new DisplayLimiterCommand());
        commandMap.put("list", new ListAllLimitsCommand());
        commandMap.put("reload", new ReloadConfigCommand());
    }

    public boolean onCommand(CommandSender sender, Command cmd, String s, String[] strings) {
        ChatLogger logger = AdvancedBanItem.getInstance().getChatLogger();
        logger.updateSender(sender);
        if (!PermissionsHelper.isConsoleOrHasPermsOrOp(sender, AdvancedBanItem.CommandsPermission)) {
            logger.logInfoPrefix("You dont have permission for AdvancedBanItem commands!");
            return true;
        }

        if (strings.length == 0) {
            logger.logInfo("------------------ AdvancedBanItem -----------------");
            logger.logInfo(ChatColor.GREEN + "  A plugin for restricting people from placing, ");
            logger.logInfo(ChatColor.GREEN + "  breaking and interacting with blocks ");
            logger.logInfo(ChatColor.BLUE + "  Version " + AdvancedBanItem.VERSION + " - made by Carrot/REghZy ;)");
            logger.logInfo("  Do " + ChatColor.GREEN + "/abi " + ChatColor.DARK_GREEN + "help" + ChatColor.GOLD + " to display help");
            logger.logInfo("----------------------------------------------------");
        }
        else {
            String command = strings[0];
            ExecutableCommand executableCommand = commandMap.get(command);
            if (executableCommand == null) {
                logger.logInfoPrefix("That command does not exist!");
            }
            else {
                try {
                    String[] newArgs = ArgsParser.extractCommandArgs(strings);
                    executableCommand.execute(sender, logger, newArgs == null ? new String[0] : newArgs);
                }
                catch (Exception e) {
                    logger.logInfoPrefix("Internal error while executing command " + ChatFormat.apostrophise(command));
                    logger.logInfoPrefix("Check console for more details");
                    logger.logPlugin("Exception while executing command " + ChatFormat.apostrophise(command));
                    e.printStackTrace();
                }
            }
        }

        logger.updateSender(null);
        return true;
    }
}
