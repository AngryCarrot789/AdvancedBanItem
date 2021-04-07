package reghzy.advbanitem;

import reghzy.advbanitem.logs.ChatLogger;

/**
 * An interface that every plugin should inherit in order to use the ChatLogger and other things too.
 * <p>
 *     Contains functions to get prefixes, chat loggers, etc
 * </p>
 */
public interface REghZyBasePlugin {
    /**
     * returns the chat logger for this plugin
     * @return
     */
    ChatLogger getChatLogger();

    /**
     * The prefix for the plugin name (e.g. MyPlugin)
     * @return
     */
    String getChatPrefix();

    /**
     * The command prefix for this plugin, e.g. /myplugin (without the /)
     * @return
     */
    String getCommandPrefix();
}
