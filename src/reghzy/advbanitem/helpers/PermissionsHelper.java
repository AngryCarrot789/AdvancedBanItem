package reghzy.advbanitem.helpers;

import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import ru.tehkode.permissions.bukkit.PermissionsEx;

public class PermissionsHelper {
    public static boolean hasPermission(Player player, String permission) {
        return PermissionsEx.getPermissionManager().has(player, permission);
    }

    public static boolean hasPermissionOrOp(Player player, String permission) {
        return hasPermission(player, permission) || player.isOp();
    }

    public static boolean isConsoleOrHasPermsOrOp(CommandSender sender, String permission) {
        if (sender instanceof ConsoleCommandSender)
            return true;
        return hasPermissionOrOp((Player) sender, permission);
    }
}
