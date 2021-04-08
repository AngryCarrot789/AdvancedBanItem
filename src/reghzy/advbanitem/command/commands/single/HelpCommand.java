package reghzy.advbanitem.command.commands.single;

import org.bukkit.command.CommandSender;
import reghzy.advbanitem.AdvancedBanItem;
import reghzy.advbanitem.command.CommandDescriptor;
import reghzy.advbanitem.command.ExecutableCommand;
import reghzy.advbanitem.permissions.PermissionsHelper;
import reghzy.advbanitem.logs.ChatLogger;

public class HelpCommand implements ExecutableCommand {
    public static final CommandDescriptor descriptor =
            new CommandDescriptor(
                    "help",
                    "",
                    "Shows help for AdvancedBanItem");

    @Override
    public void execute(CommandSender sender, ChatLogger logger, String[] strings) {
        displayIfPerms(sender, AdvancedBanItem.CommandsPermission, logger, HelpCommand.descriptor);
        displayIfPerms(sender, AdvancedBanItem.LookHandInfoPermission, logger, LookBlockInfoCommand.descriptor);
        displayIfPerms(sender, AdvancedBanItem.LookHandInfoPermission, logger, HandItemInfoCommand.descriptor);
        //displayIfPerms(sender, AdvancedBanItem.AddBlockPermission, logger, AddBlockCommand.descriptor);
        //displayIfPerms(sender, AdvancedBanItem.RemoveBlockPermission, logger, RemoveBlockCommand.descriptor);
        //displayIfPerms(sender, AdvancedBanItem.EditBlockPermission, logger, EditBlockLimitCommands.descriptor);
        displayIfPerms(sender, AdvancedBanItem.DisplayLimiterPermission, logger, DisplayLimiterCommand.descriptor);
        displayIfPerms(sender, AdvancedBanItem.DisplayLimiterPermission, logger, ListAllLimitsCommand.descriptor);
        displayIfPerms(sender, AdvancedBanItem.ReloadConfigPermission, logger, ReloadConfigCommand.descriptor);
    }

    private static void displayIfPerms(CommandSender sender, String perms, ChatLogger logger, CommandDescriptor descriptor) {
        if (PermissionsHelper.isConsoleOrHasPermsOrOp(sender, perms)) {
            logger.logInfo(descriptor.getFormatted(AdvancedBanItem.getInstance().getCommandPrefix(), ""));
        }
    }
}
