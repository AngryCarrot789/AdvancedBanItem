package reghzy.advbanitem.command.commands.single;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import reghzy.advbanitem.AdvancedBanItem;
import reghzy.advbanitem.command.CommandDescriptor;
import reghzy.advbanitem.command.ExecutableCommand;
import reghzy.advbanitem.logs.ChatLogger;
import reghzy.advbanitem.permissions.PermissionsHelper;

public class HandItemInfoCommand implements ExecutableCommand {
    public static final CommandDescriptor descriptor =
            new CommandDescriptor(
                    "hand",
                    "",
                    "Gets the ID and Data of the item in your item");

    @Override
    public void execute(CommandSender sender, ChatLogger logger, String[] args) {
        if (!(sender instanceof Player)) {
            logger.logInfoPrefix("You're not a player... mr console -_-");
            return;
        }
        if (!PermissionsHelper.hasPermissionOrOp((Player) sender, AdvancedBanItem.LookHandInfoPermission)) {
            logger.logInfoPrefix("You dont have permissiont to use this command!");
            return;
        }

        Player player = (Player) sender;
        ItemStack block = player.getItemInHand();
        if (block == null) {
            logger.logInfoPrefix("You're not holding anything :/");
        }
        else {
            logger.logInfo("ID: " + ChatColor.GREEN + block.getTypeId() + ChatColor.GOLD +
                           ", Data: " + ChatColor.LIGHT_PURPLE + block.getData().getData() + ChatColor.GOLD + ". " +
                           ChatColor.GREEN + block.getTypeId() + ":" + block.getData());
        }
    }
}