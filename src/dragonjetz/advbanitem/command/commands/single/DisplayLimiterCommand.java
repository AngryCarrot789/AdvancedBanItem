package dragonjetz.advbanitem.command.commands.single;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import dragonjetz.advbanitem.AdvancedBanItem;
import dragonjetz.advbanitem.command.ABIPermission;
import dragonjetz.advbanitem.limit.BlockLimiter;
import dragonjetz.advbanitem.limit.MetaLimit;
import dragonjetz.api.commands.ExecutableCommand;
import dragonjetz.api.commands.utils.CommandArgs;
import dragonjetz.api.commands.utils.DJLogger;
import dragonjetz.api.utils.text.StringHelper;
import dragonjetz.api.permission.IPermission;

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
    public void execute(CommandSender sender, DJLogger logger, CommandArgs args) {
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
        logger.logFormat("MetaData Limits:\n" + ChatColor.GOLD + metadataString.toString());
        if (limiter.defaultDisallowedWorlds.size() == 0) {
            logger.logFormat("Default Disallowed Worlds: " + ChatColor.RED + "There are none");
        }
        else {
            logger.logFormat("Default Disallowed Worlds: " + ChatColor.DARK_AQUA + StringHelper.joinArray(limiter.defaultDisallowedWorlds.toArray(new String[0]), 0, ", "));
        }
        logger.logFormat("Default Place Permission: " + ChatColor.DARK_AQUA + nullPermsCheck(limiter.defaultPlacePermission));
        logger.logFormat("Default Break Permission: " + ChatColor.DARK_AQUA + nullPermsCheck(limiter.defaultBreakPermission));
        logger.logFormat("Default Interact Permission: " + ChatColor.DARK_AQUA + nullPermsCheck(limiter.defaultInteractPermission));
        logger.logFormat("Default Place Message: " + ChatColor.DARK_AQUA + limiter.defaultNoPlaceMessage);
        logger.logFormat("Default Break Message: " + ChatColor.DARK_AQUA + limiter.defaultNoBreakMessage);
        logger.logFormat("Default Interact Message: " + ChatColor.DARK_AQUA + limiter.defaultNoInteractMessage);
        logger.logFormat("Default Invert Worlds: " + ChatColor.DARK_AQUA + limiter.defaultInvertWorld);
        logger.logFormat("Default Invert Perms: " + ChatColor.DARK_AQUA + limiter.defaultInvertPermission);
    }
}
