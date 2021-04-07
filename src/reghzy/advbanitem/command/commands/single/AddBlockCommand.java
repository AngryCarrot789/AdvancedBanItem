package reghzy.advbanitem.command.commands.single;

import org.bukkit.command.CommandSender;
import reghzy.advbanitem.AdvancedBanItem;
import reghzy.advbanitem.command.CommandDescriptor;
import reghzy.advbanitem.command.ExecutableCommand;
import reghzy.advbanitem.command.helpers.ArgsParser;
import reghzy.advbanitem.command.helpers.ItemDataPair;
import reghzy.advbanitem.command.helpers.ParsedValue;
import reghzy.advbanitem.helpers.PermissionsHelper;
import reghzy.advbanitem.limit.BlockLimiter;
import reghzy.advbanitem.logs.ChatFormat;
import reghzy.advbanitem.logs.ChatLogger;

import java.util.ArrayList;
import java.util.HashSet;

public class AddBlockCommand implements ExecutableCommand {
    public static CommandDescriptor descriptor =
            new CommandDescriptor(
                    "add",
                    "<id:data> [place] [break] [interact]",
                    "Adds a block that should be limited. Optional parameters for place/break/interact permissions. " +
                    "By default, no permissions = everyone can use the block\n" +
                    "Example: /abi add 12:-1 bans.noplace.12 bans.nobreak.12");

    @Override
    public void execute(CommandSender sender, ChatLogger logger, String[] args) {
        if (!PermissionsHelper.isConsoleOrHasPermsOrOp(sender, AdvancedBanItem.AddBlockPermission)) {
            logger.logInfoPrefix("You dont have permission for this command!");
            return;
        }

        ParsedValue<ItemDataPair> newBlock = ArgsParser.parseItemData(args,0);
        if (newBlock.failed) {
            logger.logInfoPrefix("Error with the new block ID. Example: 420:69");
            return;
        }

        ParsedValue<String> placePermission = ArgsParser.parseString(args, 1);
        ParsedValue<String> breakPermission = ArgsParser.parseString(args, 2);
        ParsedValue<String> interPermission = ArgsParser.parseString(args, 3);

        HashSet<Integer> metadata = new HashSet<Integer>(1);
        metadata.add(newBlock.value.data);

        BlockLimiter limiter = new BlockLimiter(
                newBlock.value.id,
                metadata,
                false, false, false,
                new ArrayList<String>(),
                placePermission.failed ? null : placePermission.value,
                breakPermission.failed ? null : breakPermission.value,
                interPermission.failed ? null : interPermission.value,
                null, null, null);
        AdvancedBanItem.getInstance().getLimitManager().addLimiter(limiter.id, limiter);

        logger.logInfoPrefix("Added the block " + ChatFormat.apostrophise(newBlock.value.toString()));
    }
}
