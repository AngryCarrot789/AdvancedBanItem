package reghzy.advbanitem.command;

import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.craftbukkit.v1_6_R3.command.CraftBlockCommandSender;
import org.bukkit.entity.Player;
import reghzy.mfunclagfind.permissions.IPermission;
import reghzy.mfunclagfind.permissions.PermissionsHelper;

public enum ABIPermission implements IPermission {
    MAIN_COMMAND("advbanitem.maincommand"),
    displayLimiter("advbanitem.display"),
    hand("advbanitem.hand"),
    look("advbanitem.look"),
    help("advbanitem.help"),
    reload("advbanitem.reload"),
    listAll("advbanitem.listall"),
    ;

    private final String permission;

    private ABIPermission(String permission) {
        this.permission = permission;
    }

    public String getCodeName() {
        return this.name();
    }

    public String getPermission() {
        return this.permission;
    }

    public boolean isConsoleOrHasOpOrPerms(CommandSender sender) {
        return sender.isOp() || this.isConsoleOrHasPermission(sender);
    }

    public boolean isConsoleOrHasPermission(CommandSender sender) {
        if (sender instanceof ConsoleCommandSender || sender instanceof CraftBlockCommandSender) {
            return true;
        }
        else {
            return sender instanceof Player && this.hasPermission((Player) sender);
        }
    }

    public boolean isOpOrHasPermission(Player player) {
        return player.isOp() || this.hasPermission(player);
    }

    public boolean hasPermission(Player player) {
        return PermissionsHelper.hasPermission(player, this.permission);
    }
}
