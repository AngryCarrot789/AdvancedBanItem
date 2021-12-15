package reghzy.advbanitem.command.commands.single;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import reghzy.advbanitem.command.ABIPermission;
import reghzy.api.commands.ExecutableCommand;
import reghzy.api.commands.utils.CommandArgs;
import reghzy.api.commands.utils.RZLogger;
import reghzy.api.permission.IPermission;

public class HandItemInfoCommand extends ExecutableCommand {
    public HandItemInfoCommand() {
        super("abi", null, "hand", null, "Displays the item in your hand");
    }

    @Override
    public IPermission getPermission() {
        return ABIPermission.SHOW_HAND_ITEM;
    }

    @Override
    public void execute(CommandSender sender, RZLogger logger, CommandArgs args) {
        if (!(sender instanceof Player)) {
            logger.logFormat("You're not a player... mr console -_-");
            return;
        }

        ItemStack block = ((Player) sender).getItemInHand();
        if (block == null) {
            logger.logFormat("You're not holding anything :/");
        }
        else {
            logger.logFormat("ID: " + ChatColor.GREEN + block.getTypeId() + ChatColor.GOLD +
                             ", Data: " + ChatColor.LIGHT_PURPLE + block.getData().getData() + ChatColor.GOLD + ". " +
                           ChatColor.GREEN + block.getTypeId() + ":" + block.getData());
        }
    }
}