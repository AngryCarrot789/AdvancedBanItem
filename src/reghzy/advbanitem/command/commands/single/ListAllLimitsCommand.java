package reghzy.advbanitem.command.commands.single;

import org.bukkit.command.CommandSender;
import reghzy.advbanitem.AdvancedBanItem;
import reghzy.advbanitem.command.CommandDescriptor;
import reghzy.advbanitem.command.ExecutableCommand;
import reghzy.advbanitem.helpers.PermissionsHelper;
import reghzy.advbanitem.limit.BlockLimiter;
import reghzy.advbanitem.logs.ChatFormat;
import reghzy.advbanitem.logs.ChatLogger;

public class ListAllLimitsCommand implements ExecutableCommand {
    public static CommandDescriptor descriptor =
            new CommandDescriptor(
                    "list",
                    "",
                    "Displays all of the block limiters");

    @Override
    public void execute(CommandSender sender, ChatLogger logger, String[] args) {
        if (!PermissionsHelper.isConsoleOrHasPermsOrOp(sender, AdvancedBanItem.DisplayLimiterPermission)) {
            logger.logInfoPrefix("You dont have permission for this command!");
            return;
        }

        for(BlockLimiter limiter : AdvancedBanItem.getInstance().getLimitManager().getBlockLimiters().values()) {
            logger.logInfoPrefix(ChatFormat.green(ChatFormat.bracketise(String.valueOf(limiter.id))));
        }
    }
}
