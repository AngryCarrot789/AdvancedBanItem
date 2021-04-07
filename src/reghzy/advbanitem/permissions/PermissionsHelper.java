package reghzy.advbanitem.permissions;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import reghzy.advbanitem.AdvancedBanItem;
import reghzy.advbanitem.logs.ChatFormat;
import reghzy.advbanitem.logs.ChatLogger;

public class PermissionsHelper {
    private static IPermsManager perms;

    public static void init() {
        try {
            perms = new PexPermsManager();
            perms.init();
            AdvancedBanItem.getInstance().getChatLogger().logInfoPrefix("Using PermissionsEx :)");
        }
        catch (NoPermissionManagerClassException e) {
            AdvancedBanItem.getInstance().getChatLogger().logInfoPrefix("Failed to find PermissionEx. Trying LuckPerms...");
            try {
                perms = new LuckPermsManager();
                perms.init();
                AdvancedBanItem.getInstance().getChatLogger().logInfoPrefix("Using LuckPerms :)");
            }
            catch (NoPermissionManagerClassException a) {
                AdvancedBanItem.getInstance().getChatLogger().logInfoPrefix("Failed to find LuckPerms. Using default bukkit permissions");
                try {
                    perms = new BukkitPermsManager();
                    perms.init();
                    AdvancedBanItem.getInstance().getChatLogger().logInfoPrefix("Using Bukkit Permissions :)) :(");
                }
                catch (NoPermissionManagerClassException aa) {
                    AdvancedBanItem.getInstance().getChatLogger().logInfoPrefix("What");
                }
            }
        }
    }

    public static boolean hasPermission(Player player, String permission) {
        return perms.has(player, permission);
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
