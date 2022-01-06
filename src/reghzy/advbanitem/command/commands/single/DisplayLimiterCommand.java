package reghzy.advbanitem.command.commands.single;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import reghzy.advbanitem.AdvancedBanItem;
import reghzy.advbanitem.command.ABIPermission;
import reghzy.advbanitem.limit.BlockLimiter;
import reghzy.advbanitem.limit.MetaLimit;
import reghzy.api.commands.ExecutableCommand;
import reghzy.api.commands.utils.CommandArgs;
import reghzy.api.commands.utils.RZLogger;
import reghzy.api.utils.text.StringHelper;
import reghzy.api.permission.IPermission;

public class DisplayLimiterCommand extends ExecutableCommand {
    public DisplayLimiterCommand() {
        super("abi", null, "display", "<id>", "Displays the limiters for the given item ID");
    }

    public static String nullPermsCheck(String permission) {
        if (permission == null)
            return ChatColor.RED + "(There is no permission)";
        return permission;
    }

    @Override
    public IPermission getPermission() {
        return ABIPermission.DISPLAY_LIMITERS;
    }

    @Override
    public void execute(CommandSender sender, RZLogger logger, CommandArgs args) {
        Integer id = args.getInteger(0);
        if (id == null) {
            logger.logFormat("Problem with the block id: it isn't a number!");
            return;
        }

        BlockLimiter limiter = AdvancedBanItem.getInstance().getLimitManager().getLimiter(id);
        if (limiter == null) {
            logger.logFormat("There are no limiters for that ID!");
            return;
        }

        logger.logFormat("ID: " + ChatColor.DARK_AQUA + limiter.id);
        StringBuilder metadataString = new StringBuilder(32);
        int in = 0, countIndex = limiter.metadata.size() - 1;
        if (countIndex < 0) {
            metadataString.append("(no metadata)");
        }
        else {
            for (MetaLimit pair : limiter.metadata.values()) {
                if (in == countIndex) {
                    metadataString.append(pair.formattedDescription());
                }
                else {
                    metadataString.append(pair.formattedDescription()).append('\n').append(ChatColor.DARK_AQUA).append("-----\n").append(ChatColor.GOLD);
                    in++;
                }
            }
        }
        logger.logFormat("MetaData Limits:\n&6{0}", metadataString.toString());
        if (limiter.defaultDisallowedWorlds.size() == 0) {
            logger.logFormat("Default Disallowed Worlds: &cThere are none");
        }
        else {
            logger.logFormat("Default Disallowed Worlds: &3{0}", StringHelper.joinArray(limiter.defaultDisallowedWorlds.toArray(new String[0]), 0, ", "));
        }

        logger.logFormat("Default Place Permission: &3{0}", nullPermsCheck(limiter.defaultPlacePermission));
        logger.logFormat("Default Break Permission: &3{0}", nullPermsCheck(limiter.defaultBreakPermission));
        logger.logFormat("Default Interact Permission: &3{0}", nullPermsCheck(limiter.defaultInteractPermission));
        logger.logFormat("Default Place Message: &3{0}", limiter.defaultNoPlaceMessage);
        logger.logFormat("Default Break Message: &3{0}", limiter.defaultNoBreakMessage);
        logger.logFormat("Default Interact Message: &3{0}", limiter.defaultNoInteractMessage);
        logger.logFormat("Default Invert Worlds: &3{0}", limiter.defaultInvertWorld);
        logger.logFormat("Default Invert Perms: &3{0}", limiter.defaultInvertPermission);
    }
}
