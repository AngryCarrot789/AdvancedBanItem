package reghzy.advbanitem.command.commands.single;

import org.bukkit.ChatColor;
import org.bukkit.block.Block;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import reghzy.advbanitem.AdvancedBanItem;
import reghzy.advbanitem.command.CommandDescriptor;
import reghzy.advbanitem.command.ExecutableCommand;
import reghzy.advbanitem.command.helpers.ArgsParser;
import reghzy.advbanitem.command.helpers.ParsedValue;
import reghzy.advbanitem.permissions.PermissionsHelper;
import reghzy.advbanitem.logs.ChatLogger;

public class LookBlockInfoCommand implements ExecutableCommand {
    public static final int DefaultDistance = 100;
    public static final CommandDescriptor descriptor =
            new CommandDescriptor(
                    "look",
                    "[distance]",
                    "Gets the ID and Data of the block you're looking at");

    @Override
    public void execute(CommandSender sender, ChatLogger logger, String[] args) {
        if (!(sender instanceof Player)) {
            logger.logInfoPrefix("You're not a player... mr console -_-");
            return;
        }
        if (!PermissionsHelper.hasPermissionOrOp((Player) sender, AdvancedBanItem.LookBlockInfoPermission)) {
            logger.logInfoPrefix("You dont have permissiont to use this command!");
            return;
        }

        ParsedValue<Integer> distance = ArgsParser.parseInteger(args, 0);
        if (distance.failed) {
            distance.value = DefaultDistance;
        }

        Player player = (Player) sender;
        Block block = player.getTargetBlock(null, distance.value);
        if (block.isEmpty()) {
            logger.logInfo("You're not looking at anything, just air. Add a distant on the end of the command to look further");
        }
        else {
            logger.logInfo("Block ID: " + ChatColor.GREEN + block.getType().getId() + ChatColor.GOLD +
                           ", Block Data: " + ChatColor.LIGHT_PURPLE + block.getData() + ChatColor.GOLD + ". " +
                           ChatColor.GREEN + block.getType().getId() + ":" + block.getData());
        }
    }
}
