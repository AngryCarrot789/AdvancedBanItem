package reghzy.advbanitem.command.commands.single;

import org.bukkit.block.Block;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import reghzy.advbanitem.command.ABIPermission;
import reghzy.mfunclagfind.command.ExecutableCommand;
import reghzy.mfunclagfind.command.utils.CommandArgs;
import reghzy.mfunclagfind.command.utils.REghZyLogger;
import reghzy.mfunclagfind.permissions.IPermission;

public class LookBlockInfoCommand extends ExecutableCommand {
    public static final int DefaultDistance = 100;

    public LookBlockInfoCommand() {
        super("abi", null, "look", "[distance]", "Gets the ID and Meta of the block you're looking at");
    }

    @Override
    public IPermission getPermission() {
        return ABIPermission.look;
    }

    @Override
    public void execute(CommandSender sender, REghZyLogger logger, CommandArgs args) {
        if (!(sender instanceof Player)) {
            logger.logPrefix("You're not a player... mr console -_-");
            return;
        }

        Block block = ((Player) sender).getTargetBlock(null, args.getInteger(0, DefaultDistance));
        if (block.isEmpty()) {
            logger.logPrefix("You're not looking at anything, just air. Add a distant on the end of the command to look further");
        }
        else {
            logger.logFormat("&6Block ID: &a{0}&6, Block Data: &2{1}&6. &a{2}&6:&2{3}", block.getTypeId(), block.getData(), block.getTypeId(), block.getData());
        }
    }
}
