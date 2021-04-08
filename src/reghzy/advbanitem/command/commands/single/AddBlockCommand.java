package reghzy.advbanitem.command.commands.single;

import org.bukkit.command.CommandSender;
import reghzy.advbanitem.AdvancedBanItem;
import reghzy.advbanitem.command.CommandDescriptor;
import reghzy.advbanitem.command.ExecutableCommand;
import reghzy.advbanitem.command.helpers.ArgsParser;
import reghzy.advbanitem.command.helpers.ItemDataPair;
import reghzy.advbanitem.command.helpers.ParsedValue;
import reghzy.advbanitem.limit.MetaLimit;
import reghzy.advbanitem.permissions.PermissionsHelper;
import reghzy.advbanitem.limit.BlockLimiter;
import reghzy.advbanitem.logs.ChatFormat;
import reghzy.advbanitem.logs.ChatLogger;

import java.util.ArrayList;
import java.util.HashMap;

public class AddBlockCommand implements ExecutableCommand {
    public static final CommandDescriptor descriptor =
            new CommandDescriptor(
                    "add",
                    "<id:data> [place] [break] [interact]",
                    "Adds a block that should be limited. Optional parameters for permissions.\n" +
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

        HashMap<Integer, MetaLimit> map = new HashMap<Integer, MetaLimit>(1);
        //MetaLimit pair = BlockLimiter.defaultMessage(newBlock.value.data);
        //map.put(pair.metadata, pair);
        //BlockLimiter limiter = new BlockLimiter(
        //        newBlock.value.id,
        //        map,
        //        false, false,
        //        new ArrayList<String>(),
        //        placePermission.failed ? null : placePermission.value,
        //        breakPermission.failed ? null : breakPermission.value,
        //        interPermission.failed ? null : interPermission.value,
        //        BlockLimiter.FallbackNoPlaceMessage,
        //        BlockLimiter.FallbackNoBreakMessage,
        //        BlockLimiter.FallbackNoInteractMessage);
        //AdvancedBanItem.getInstance().getLimitManager().addLimiter(limiter.id, limiter);
        //AdvancedBanItem.getInstance().getLimitManager().saveDataToConfig();

        logger.logInfoPrefix("Added the block " + ChatFormat.apostrophise(newBlock.value.toString()));
    }
}
