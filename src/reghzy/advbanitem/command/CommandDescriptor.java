package reghzy.advbanitem.command;

import org.bukkit.ChatColor;

/**
 * A simple class for giving a description on how to use a command, or a main command
 */
public class CommandDescriptor {
    /**
     * The name of the command
     */
    public final String name;
    /**
     * The parameters of the command (with brackets, e.g. &lt;param1> [param2]
     */
    public final String parameters;
    /**
     * The description of the command
     */
    public final String description;

    private final boolean isMainCommand;

    /**
     * Sets up this command descriptor as a sub command, e.g. /myplugin maincommand [subcommand]
     */
    public CommandDescriptor(String name, String parameters, String description) {
        this.name = name;
        this.parameters = parameters;
        this.description = description;
        isMainCommand = false;
    }

    /**
     * Sets up this command descriptor as a main command, e.g. /myplugin maincommand
     */
    public CommandDescriptor(String name, String description) {
        this.name = name;
        this.parameters = "";
        this.description = description;
        isMainCommand = true;
    }

    /**
     * Gets a formatted block of text for this command description, including chat colours, etc
     * @param mainCommand Only needs to be specified if this is a description for a sub command. Otherwise leave it as empty
     * @return text
     */
    public String getFormatted(String pluginPrefix, String mainCommand) {
        if (isMainCommand) {
            return CommandDescriptor.getMainFormatted(pluginPrefix, this);
        }
        else {
            return CommandDescriptor.getSubFormatted(pluginPrefix, this, mainCommand);
        }
    }

    /**
     * Formats this descriptor as a main command, like so:
     * <p>
     *     /[pluginPrefix] mainCommand (this command has sub-commands) \n description
     * </p>
     */
    public static String getMainFormatted(String pluginPrefix, CommandDescriptor descriptor) {
        return ChatColor.AQUA + "/" + pluginPrefix + " " +
               ChatColor.DARK_AQUA + descriptor.name + " " +
               ChatColor.BLUE + "(This command has sub-commands)" + '\n' +
               ChatColor.GREEN + descriptor.description;
    }

    /**
     * Formats this descriptor as a sub command, like so:
     * <p>
     * /[pluginPrefix] mainCommand subCommand \n description
     * </p>
     */
    public static String getSubFormatted(String pluginPrefix, CommandDescriptor descriptor, String mainCommand) {
        return ChatColor.AQUA + "/" + pluginPrefix + " " + (mainCommand.isEmpty() ? "" : mainCommand + " ") +
               ChatColor.DARK_AQUA + descriptor.name + " " +
               ChatColor.BLUE + descriptor.parameters + '\n' +
               ChatColor.GREEN + descriptor.description;
    }
}
