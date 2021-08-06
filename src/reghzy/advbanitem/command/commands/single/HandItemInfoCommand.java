package reghzy.advbanitem.command.commands.single;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import reghzy.advbanitem.command.ABIPermission;
import reghzy.mfunclagfind.command.ExecutableCommand;
import reghzy.mfunclagfind.command.utils.CommandArgs;
import reghzy.mfunclagfind.command.utils.REghZyLogger;
import reghzy.mfunclagfind.permissions.IPermission;

public class HandItemInfoCommand extends ExecutableCommand {
    public HandItemInfoCommand() {
        super("abi", null, "hand", null, "Displays the item in your hand");
    }

    @Override
    public IPermission getPermission() {
        return ABIPermission.hand;
    }

    @Override
    public void execute(CommandSender sender, REghZyLogger logger, CommandArgs args) {
        if (!(sender instanceof Player)) {
            logger.logPrefix("You're not a player... mr console -_-");
            return;
        }

        ItemStack block = ((Player) sender).getItemInHand();
        if (block == null) {
            logger.logPrefix("You're not holding anything :/");
        }
        else {
            logger.logPrefix("ID: " + ChatColor.GREEN + block.getTypeId() + ChatColor.GOLD +
                             ", Data: " + ChatColor.LIGHT_PURPLE + block.getData().getData() + ChatColor.GOLD + ". " +
                           ChatColor.GREEN + block.getTypeId() + ":" + block.getData());
        }
    }
}