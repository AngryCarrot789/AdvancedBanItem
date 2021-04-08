package reghzy.advbanitem.command.commands.single;

import org.bukkit.command.CommandSender;
import reghzy.advbanitem.command.CommandDescriptor;
import reghzy.advbanitem.command.ExecutableCommand;
import reghzy.advbanitem.logs.ChatLogger;

public class RemoveBlockCommand implements ExecutableCommand {
    public static final CommandDescriptor descriptor =
            new CommandDescriptor(
                    "remove",
                    "<id>",
                    "Removes a block limiter for the given ID\n " +
                    "Example: /abi remove 12");

    @Override
    public void execute(CommandSender sender, ChatLogger logger, String[] args) {
        //if (!PermissionsHelper.isConsoleOrHasPermsOrOp(sender, AdvancedBanItem.RemoveBlockPermission)) {
        //    logger.logInfoPrefix("You dont have permission for this command!");
        //    return;
        //}

        //ParsedValue<Integer> blockId = ArgsParser.parseInteger(args, 0);
        //if (blockId.failed) {
        //    logger.logInfoPrefix("Error with the block ID");
        //    return;
        //}

        //BlockLimiter limiter = AdvancedBanItem.getInstance().getLimitManager().removeLimiter(blockId.value);
        //if (limiter == null) {
        //    logger.logInfoPrefix("There isn't a limiter for that block");
        //}
        //else {
        //    OldWorldLookup.removeBlockDisallowedWorld(blockId.value);
        //    AdvancedBanItem.getInstance().getLimitManager().saveDataToConfig();
        //    logger.logInfoPrefix("Removed the limiter for block ID: " + ChatFormat.apostrophise(blockId.value.toString()));
        //}
    }
}
