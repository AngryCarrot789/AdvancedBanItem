package dragonjetz.advbanitem.command.commands.single;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import dragonjetz.advbanitem.AdvancedBanItem;
import dragonjetz.advbanitem.command.ABIPermission;
import dragonjetz.advbanitem.limit.BlockLimiter;
import dragonjetz.api.commands.ExecutableCommand;
import dragonjetz.api.commands.utils.CommandArgs;
import dragonjetz.api.commands.utils.DJLogger;
import dragonjetz.api.utils.text.StringJoiner;
import dragonjetz.api.permission.IPermission;

public class ListAllLimitsCommand extends ExecutableCommand {
    public ListAllLimitsCommand() {
        super("abi", null, "listall", null, "Displays all of the limited IDs and metadatas");
    }

    public IPermission getPermission() {
        return ABIPermission.LIST_BANNED_IDS;
    }

    @Override
    public void execute(CommandSender sender, DJLogger logger, CommandArgs args) {
        logger.logFormat("List of limited/banned IDs:");
        StringJoiner joiner = new StringJoiner(", ");
        for (BlockLimiter limiter : AdvancedBanItem.getInstance().getLimitManager().getBlockLimiters().values()) {
            joiner.append(ChatColor.GREEN.toString() + limiter.id);
        }

        logger.logFormat(joiner.toString());
    }
}
