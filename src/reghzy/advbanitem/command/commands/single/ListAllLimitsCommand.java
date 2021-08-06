package reghzy.advbanitem.command.commands.single;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import reghzy.advbanitem.AdvancedBanItem;
import reghzy.advbanitem.command.ABIPermission;
import reghzy.advbanitem.limit.BlockLimiter;
import reghzy.mfunclagfind.command.ExecutableCommand;
import reghzy.mfunclagfind.command.utils.CommandArgs;
import reghzy.mfunclagfind.command.utils.REghZyLogger;
import reghzy.mfunclagfind.permissions.IPermission;
import reghzy.mfunclagfind.utils.text.StringJoiner;

public class ListAllLimitsCommand extends ExecutableCommand {
    public ListAllLimitsCommand() {
        super("abi", null, "listall", null, "Displays all of the limited IDs and metadatas");
    }

    public IPermission getPermission() {
        return ABIPermission.listAll;
    }

    @Override
    public void execute(CommandSender sender, REghZyLogger logger, CommandArgs args) {
        logger.logPrefix("List of limited/banned IDs:");
        StringJoiner joiner = new StringJoiner(", ");
        for (BlockLimiter limiter : AdvancedBanItem.getInstance().getLimitManager().getBlockLimiters().values()) {
            joiner.append(ChatColor.GREEN.toString() + limiter.id);
        }
        logger.logPrefix(joiner.toString());
    }
}
