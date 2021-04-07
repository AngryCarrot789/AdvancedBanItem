package reghzy.advbanitem.command;

import reghzy.advbanitem.logs.ChatLogger;

import java.util.HashMap;

/**
 * An abstract class that should be extended by a main command (which has sub-commands)
 */
public abstract class ExecutableSubCommands implements ExecutableCommand {
    /**
     * The map of sub commands, key'd to the subcommand name, e.g. help
     */
    public HashMap<String, ExecutableCommand> subCommandMap;

    /**
     * Displays the helpful information for all of the subcommands
     * @param logger
     */
    public abstract void displayHelp(ChatLogger logger, int page);
}
