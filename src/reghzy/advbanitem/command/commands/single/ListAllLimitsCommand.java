package reghzy.advbanitem.command.commands.single;

import org.bukkit.command.CommandSender;
import reghzy.advbanitem.AdvancedBanItem;
import reghzy.advbanitem.command.CommandDescriptor;
import reghzy.advbanitem.command.ExecutableCommand;
import reghzy.advbanitem.permissions.PermissionsHelper;
import reghzy.advbanitem.limit.BlockLimiter;
import reghzy.advbanitem.logs.ChatFormat;
import reghzy.advbanitem.logs.ChatLogger;

public class ListAllLimitsCommand implements ExecutableCommand {
    public static final CommandDescriptor descriptor =
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

        logger.logInfoPrefix("List of limited/banned IDs:");
        StringBuilder ids = new StringBuilder(64);
        for(BlockLimiter limiter : AdvancedBanItem.getInstance().getLimitManager().getBlockLimiters().values()) {
            ids.append(ChatFormat.green(String.valueOf(limiter.id)));
        }
        logger.logInfo(ids.toString());
    }
}
