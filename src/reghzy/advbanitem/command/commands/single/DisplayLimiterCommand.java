package reghzy.advbanitem.command.commands.single;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import reghzy.advbanitem.AdvancedBanItem;
import reghzy.advbanitem.command.CommandDescriptor;
import reghzy.advbanitem.command.ExecutableCommand;
import reghzy.advbanitem.command.helpers.ArgsParser;
import reghzy.advbanitem.command.helpers.ParsedValue;
import reghzy.advbanitem.helpers.StringHelper;
import reghzy.advbanitem.limit.MetaLimit;
import reghzy.advbanitem.permissions.PermissionsHelper;
import reghzy.advbanitem.limit.BlockLimiter;
import reghzy.advbanitem.logs.ChatLogger;

public class DisplayLimiterCommand implements ExecutableCommand {
    public static final CommandDescriptor descriptor =
            new CommandDescriptor(
                    "display",
                    "<id>",
                    "Displays all of the information for a specific block limiter");

    @Override
    public void execute(CommandSender sender, ChatLogger logger, String[] args) {
        if (!PermissionsHelper.isConsoleOrHasPermsOrOp(sender, AdvancedBanItem.DisplayLimiterPermission)) {
            logger.logInfoPrefix("You dont have permission for this command!");
            return;
        }

        ParsedValue<Integer> blockId = ArgsParser.parseInteger(args, 0);
        if (blockId.failed) {
            logger.logInfoPrefix("Error with the block ID");
            return;
        }

        BlockLimiter limiter = AdvancedBanItem.getInstance().getLimitManager().getLimiter(blockId.value);
        if (limiter == null) {
            logger.logInfoPrefix("There isnt a limiter for that block");
            return;
        }

        logger.logInfo("ID: " + ChatColor.DARK_AQUA + limiter.id);
        StringBuilder metadataString = new StringBuilder(32);
        int in = 0, countIndex = limiter.metadata.size() - 1;
        if (countIndex < 0) {
            metadataString.append("(no metadata)");
        }
        else {
            for (MetaLimit pair : limiter.metadata.values()) {
                if (in == countIndex) {
                    metadataString.append(pair.toString());
                }
                else {
                    metadataString.append(pair.toString()).append('\n').append(ChatColor.DARK_AQUA).append("---------------------------------------------\n").append(ChatColor.GOLD);
                    in++;
                }
            }
        }
        logger.logInfo("MetaData Limits:\n" + ChatColor.GOLD + metadataString.toString());
        if (limiter.defaultDisallowedWorlds.size() == 0) {
            logger.logInfo("Default Disallowed Worlds: " + ChatColor.RED + "There are none");
        }
        else {
            logger.logInfo("Default Disallowed Worlds: " + ChatColor.DARK_AQUA + StringHelper.joinArray(limiter.defaultDisallowedWorlds.toArray(new String[0]), 0, ", "));
        }
        logger.logInfo("Default Place Permission: " + ChatColor.DARK_AQUA + nullPermissionCheck(limiter.defaultPlacePermission));
        logger.logInfo("Default Break Permission: " + ChatColor.DARK_AQUA + nullPermissionCheck(limiter.defaultBreakPermission));
        logger.logInfo("Default Interact Permission: " + ChatColor.DARK_AQUA + nullPermissionCheck(limiter.defaultInteractPermission));
        logger.logInfo("Default Place Message: " + ChatColor.DARK_AQUA + limiter.defaultNoPlaceMessage);
        logger.logInfo("Default Break Message: " + ChatColor.DARK_AQUA + limiter.defaultNoBreakMessage);
        logger.logInfo("Default Interact Message: " + ChatColor.DARK_AQUA + limiter.defaultNoInteractMessage);
        logger.logInfo("Default Invert Worlds: " + ChatColor.DARK_AQUA + limiter.defaultInvertWorld);
        logger.logInfo("Default Invert Perms: " + ChatColor.DARK_AQUA + limiter.defaultInvertPermission);
    }

    public static String nullPermissionCheck(String permission) {
        if (permission == null)
            return ChatColor.RED + "(There is no permission)";
        return permission;
    }
}
