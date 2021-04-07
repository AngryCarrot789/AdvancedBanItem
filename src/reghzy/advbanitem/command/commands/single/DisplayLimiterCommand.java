package reghzy.advbanitem.command.commands.single;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import reghzy.advbanitem.AdvancedBanItem;
import reghzy.advbanitem.command.CommandDescriptor;
import reghzy.advbanitem.command.ExecutableCommand;
import reghzy.advbanitem.command.helpers.ArgsParser;
import reghzy.advbanitem.command.helpers.ParsedValue;
import reghzy.advbanitem.permissions.PermissionsHelper;
import reghzy.advbanitem.limit.BlockLimiter;
import reghzy.advbanitem.limit.WorldLookup;
import reghzy.advbanitem.logs.ChatLogger;

import java.util.ArrayList;

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
        if (countIndex < 0)
            metadataString.append("(no metadata)");
        else {
            for (Integer data : limiter.metadata) {
                if (in == countIndex) {
                    metadataString.append(data);
                }
                else {
                    metadataString.append(data).append(", ");
                    in++;
                }
            }
        }
        logger.logInfo("Data: " + ChatColor.DARK_AQUA + metadataString.toString());

        StringBuilder worldConcat = new StringBuilder(128);
        ArrayList<Integer> worldHashes = WorldLookup.getWorldsFromId(limiter.id);
        if (worldHashes == null || worldHashes.size() == 0) {
            logger.logInfo("Disallowed Worlds: " + ChatColor.RED + "There are no worlds");
        }
        else {
            logger.logInfo("Disallowed Worlds: ");
            worldConcat.append(ChatColor.DARK_AQUA.toString());
            for (int i = 0, size = worldHashes.size(), lastIndex = size - 1; i < size; i++) {
                String name = WorldLookup.getNameFromHash(worldHashes.get(i));
                name = (name == null) ? "[Unknown]" : name;
                if (i == lastIndex)
                    worldConcat.append(name);
                else
                    worldConcat.append(name).append(", ");
            }
            logger.logInfo(worldConcat.toString());
        }
        logger.logInfo("Place permission: " + ChatColor.DARK_AQUA + nullPermissionCheck(limiter.placePermission));
        logger.logInfo("Break permission: " + ChatColor.DARK_AQUA + nullPermissionCheck(limiter.breakPermission));
        logger.logInfo("Interact permission: " + ChatColor.DARK_AQUA + nullPermissionCheck(limiter.interactPermission));
        logger.logInfo("No Place Message: " + ChatColor.DARK_AQUA + limiter.noPlaceMessage);
        logger.logInfo("No Break Message: " + ChatColor.DARK_AQUA + limiter.noBreakMessage);
        logger.logInfo("No Interact Message: " + ChatColor.DARK_AQUA + limiter.noInteractMessage);
        logger.logInfo("Invert Worlds: " + ChatColor.DARK_AQUA + limiter.invertWorld);
        logger.logInfo("Invert Perms: " + ChatColor.DARK_AQUA + limiter.invertPermission);
    }

    private static String nullPermissionCheck(String permission) {
        if (permission == null)
            return "&4[No permission]";
        return permission;
    }
}
