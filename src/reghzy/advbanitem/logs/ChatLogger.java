package reghzy.advbanitem.logs;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import reghzy.advbanitem.REghZyBasePlugin;

/**
 * A class for sending messages to a sender, or console too. not really "logging" but eh...
 */
public class ChatLogger {
    private final REghZyBasePlugin plugin;
    private CommandSender sender;

    public ChatLogger(REghZyBasePlugin plugin, CommandSender sender) {
        this.plugin = plugin;
        updateSender(sender);
    }

    /**
     * Sets the active sender to the given sender
     * @param sender
     */
    public void updateSender(CommandSender sender) {
        this.sender = sender;
    }

    /**
     * Logs the text to the sender that this logger contains. it can be changed with the updateSender() function
     * @param text
     */
    public void logSender(String text) {
        if (sender != null) {
            sender.sendMessage(text);
        }
    }

    /**
     * Logs the text to the sender this logger contains. if the sender is null, it logs the console instead.
     * <p>
     *     All instance log functions in this class call this function, e.g. logWarning(), logInfo(), etc
     * </p>
     * @param text
     */
    public void logAny(String text) {
        if (sender == null) {
            logConsole(text);
        }
        else {
            logSender(text);
        }
    }

    /**
     * Logs gold text, with a red [WARNING] prefix at the start (to the active sender or console)
     * @param text
     */
    public void logWarning(String text) {
        logAny(ChatColor.RED + "[WARNING] " + ChatColor.GOLD + text);
    }

    /**
     * Logs the text in gold/orange (to the active sender or console)
     * @param text
     */
    public void logInfo(String text) {
        logAny(ChatColor.GOLD + text);
    }

    /**
     * Logs the text in gold, with the plugin's prefix at the start (e.g. [PluginPrefix] text) (to the active sender or console)
     * @param text
     */
    public void logInfoPrefix(String text) {
        logAny(this.plugin.getChatPrefix() + " " + ChatColor.GOLD + text);
    }

    /**
     * Logs the text in green (to the active sender or console)
     * @param text
     */
    public void logSuccess(String text) {
        logAny(ChatColor.GREEN + text);
    }

    /**
     * Logs golden text to the console with the plugin's chat prefix at the start
     * (the plugin this logger contains can't be null otherwise NullPointerException)
     * @param text
     */
    public void logPlugin(String text) {
        logConsole(this.plugin.getChatPrefix() + " " + ChatColor.GOLD + text);
    }

    /**
     * same as logPlugin() but text is formatted like: header: info
     * @param header
     * @param info
     */
    public void logHeadInfo(String header, String info) {
        logConsole(this.plugin.getChatPrefix() + " " + ChatColor.GOLD + header + ": " + info);
    }

    /**
     * Logs information to the bukkit console sender, allowing colourful text
     * @param text
     */
    public static void logConsole(String text) {
        Bukkit.getConsoleSender().sendMessage(text);
    }
}
