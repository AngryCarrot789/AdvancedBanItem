package reghzy.advbanitem.command.commands.single;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import reghzy.advbanitem.AdvancedBanItem;
import reghzy.advbanitem.command.ABIPermission;
import reghzy.advbanitem.limit.BlockLimiter;
import reghzy.advbanitem.limit.MetaLimit;
import reghzy.mfunclagfind.command.ExecutableCommand;
import reghzy.mfunclagfind.command.utils.CommandArgs;
import reghzy.mfunclagfind.command.utils.REghZyLogger;
import reghzy.mfunclagfind.permissions.IPermission;
import reghzy.mfunclagfind.utils.text.StringHelper;

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
        return ABIPermission.displayLimiter;
    }

    @Override
    public void execute(CommandSender sender, REghZyLogger logger, CommandArgs args) {
        Integer id = args.getInteger(0);
        if (id == null) {
            logger.logPrefix("Problem with the block id: it isn't a number!");
            return;
        }

        BlockLimiter limiter = AdvancedBanItem.getInstance().getLimitManager().getLimiter(id);
        if (limiter == null) {
            logger.logPrefix("There are no limiters for that ID!");
            return;
        }

        logger.logPrefix("ID: " + ChatColor.DARK_AQUA + limiter.id);
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
        logger.logPrefix("MetaData Limits:\n" + ChatColor.GOLD + metadataString.toString());
        if (limiter.defaultDisallowedWorlds.size() == 0) {
            logger.logPrefix("Default Disallowed Worlds: " + ChatColor.RED + "There are none");
        }
        else {
            logger.logPrefix("Default Disallowed Worlds: " + ChatColor.DARK_AQUA + StringHelper.joinArray(limiter.defaultDisallowedWorlds.toArray(new String[0]), 0, ", "));
        }
        logger.logPrefix("Default Place Permission: " + ChatColor.DARK_AQUA + nullPermsCheck(limiter.defaultPlacePermission));
        logger.logPrefix("Default Break Permission: " + ChatColor.DARK_AQUA + nullPermsCheck(limiter.defaultBreakPermission));
        logger.logPrefix("Default Interact Permission: " + ChatColor.DARK_AQUA + nullPermsCheck(limiter.defaultInteractPermission));
        logger.logPrefix("Default Place Message: " + ChatColor.DARK_AQUA + limiter.defaultNoPlaceMessage);
        logger.logPrefix("Default Break Message: " + ChatColor.DARK_AQUA + limiter.defaultNoBreakMessage);
        logger.logPrefix("Default Interact Message: " + ChatColor.DARK_AQUA + limiter.defaultNoInteractMessage);
        logger.logPrefix("Default Invert Worlds: " + ChatColor.DARK_AQUA + limiter.defaultInvertWorld);
        logger.logPrefix("Default Invert Perms: " + ChatColor.DARK_AQUA + limiter.defaultInvertPermission);
    }
}
