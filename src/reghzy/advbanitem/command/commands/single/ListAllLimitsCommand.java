package reghzy.advbanitem.command.commands.single;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import reghzy.advbanitem.AdvancedBanItem;
import reghzy.advbanitem.command.ABIPermission;
import reghzy.advbanitem.limit.BlockLimiter;
import reghzy.api.commands.ExecutableCommand;
import reghzy.api.commands.utils.CommandArgs;
import reghzy.api.commands.utils.RZLogger;
import reghzy.api.utils.text.StringJoiner;
import reghzy.api.permission.IPermission;

public class ListAllLimitsCommand extends ExecutableCommand {
    public ListAllLimitsCommand() {
        super("abi", null, "listall", null, "Displays all of the limited IDs and metadatas");
    }

    public IPermission getPermission() {
        return ABIPermission.LIST_BANNED_IDS;
    }

    @Override
    public void execute(CommandSender sender, RZLogger logger, CommandArgs args) {
        logger.logFormat("List of limited/banned IDs:");
        StringJoiner joiner = new StringJoiner(", ");
        for (BlockLimiter limiter : AdvancedBanItem.getInstance().getLimitManager().getBlockLimiters().values()) {
            joiner.append(ChatColor.GREEN.toString() + limiter.id);
        }

        logger.logFormat(joiner.toString());
    }
}
