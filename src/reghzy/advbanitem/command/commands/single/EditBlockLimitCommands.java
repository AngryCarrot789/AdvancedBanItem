package reghzy.advbanitem.command.commands.single;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import reghzy.advbanitem.AdvancedBanItem;
import reghzy.advbanitem.command.CommandDescriptor;
import reghzy.advbanitem.command.ExecutableCommand;
import reghzy.advbanitem.command.helpers.ArgsParser;
import reghzy.advbanitem.command.helpers.ParsedValue;
import reghzy.advbanitem.permissions.PermissionsHelper;
import reghzy.advbanitem.limit.BlockLimiter;
import reghzy.advbanitem.limit.LimitManager;
import reghzy.advbanitem.limit.WorldLookup;
import reghzy.advbanitem.logs.ChatLogger;

import java.util.ArrayList;

public class EditBlockLimitCommands implements ExecutableCommand {
    public static final CommandDescriptor descriptor =
            new CommandDescriptor(
                    "edit",
                    "<id> <variable or addworld/removeworld> <value>",
                    "Allows you to edit anything in the given block limiter. To remove permissions use !\n" +
                    "Example: /abi edit 12 addworld w-mining");

    @Override
    public void execute(CommandSender sender, ChatLogger logger, String[] args) {
        if (!PermissionsHelper.isConsoleOrHasPermsOrOp(sender, "advbanitem.perms.commands.edit")) {
            logger.logInfoPrefix("You dont have permission to edit block limits");
            return;
        }

        if (args == null || args.length < 3) {
            logger.logInfoPrefix("You haven't provided enough arguments");
            logger.logInfoPrefix("Its: /abi edit <id> <variable/action> <new value>");
            logger.logInfoPrefix("These are all of the avaliable commands: ");
            logger.logInfoPrefix("/abi edit <id> id <new id>");
            logger.logInfoPrefix("/abi edit <id> addmeta <metadata>");
            logger.logInfoPrefix("/abi edit <id> removemeta <metadata>");
            logger.logInfoPrefix("/abi edit <id> addworld <world name>");
            logger.logInfoPrefix("/abi edit <id> removeworld <world name>");
            logger.logInfoPrefix("/abi edit <id> place <new permission>");
            logger.logInfoPrefix("/abi edit <id> break <new permission>");
            logger.logInfoPrefix("/abi edit <id> interact <new permission>");
            logger.logInfoPrefix("/abi edit <id> placemsg <new message>");
            logger.logInfoPrefix("/abi edit <id> breakmsg <new message>");
            logger.logInfoPrefix("/abi edit <id> interactmsg <new message>");
            logger.logInfoPrefix("/abi edit <id> invertworlds <true/false>");
            logger.logInfoPrefix("/abi edit <id> invertperms <true/false>");
            return;
        }

        ParsedValue<Integer> parsedId = ArgsParser.parseInteger(args, 0);
        if (parsedId.failed) {
            logger.logInfoPrefix("You haven't supplied the ID of the block to edit");
            return;
        }

        LimitManager manager = AdvancedBanItem.getInstance().getLimitManager();
        BlockLimiter limiter = manager.getLimiter(parsedId.value);
        if (limiter == null) {
            logger.logInfoPrefix("That block isn't limited");
            return;
        }

        ParsedValue<String> parsedVariable = ArgsParser.parseString(args, 1);
        if (parsedVariable.failed) {
            logger.logInfoPrefix("You haven't provided a variable to edit");
            return;
        }

        if (parsedVariable.value.equalsIgnoreCase("id")) {
            ParsedValue<Integer> newId = ArgsParser.parseInteger(args, 2);
            if (newId.failed) {
                logger.logInfoPrefix("Error with the new block ID, it probably isn't a number");
            }
            else {
                if (newId.value.equals(parsedId.value)) {
                    logger.logInfoPrefix("The IDs are the same...");
                }
                else {
                    logger.logInfoPrefix("Changing the ID from " + keyword(limiter.id) + " to " + keyword(newId.value));
                    limiter.id = newId.value;
                    ArrayList<Integer> worldsHashes = WorldLookup.removeBlockDisallowedWorld(parsedId.value);
                    // this should never be null, but it could have no elements
                    if (worldsHashes != null) {
                        WorldLookup.addBlockDisallowedWorldHashes(limiter.id, worldsHashes);
                    }
                    manager.removeLimiter(parsedId.value);
                    manager.addLimiter(limiter.id, limiter);
                    AdvancedBanItem.getInstance().getLimitManager().saveDataToConfig();
                }
            }
        }
        else if (parsedVariable.value.equalsIgnoreCase("addmeta")) {
            ParsedValue<Integer> metadata = ArgsParser.parseInteger(args, 2);
            if (metadata.failed) {
                logger.logInfoPrefix("Error with the metadata to add. Must be a number");
            }
            else {
                if (limiter.metadata.add(metadata.value)) {
                    logger.logInfoPrefix("Added metadata: " + keyword(metadata.value));
                }
                else {
                    logger.logInfoPrefix("Metadata " + keyword(metadata.value) + " is already added!");
                }
            }
        }
        else if (parsedVariable.value.equalsIgnoreCase("removemeta")) {
            ParsedValue<Integer> metadata = ArgsParser.parseInteger(args, 2);
            if (metadata.failed) {
                logger.logInfoPrefix("Error with the metadata to remove. Must be a number");
            }
            else {
                if (limiter.metadata.remove(metadata.value)) {
                    logger.logInfoPrefix("Removed metadata: " + keyword(metadata.value));
                }
                else {
                    logger.logInfoPrefix("This block limit doesn't contain the metadata " + keyword(metadata.value) + ChatColor.GOLD + "!");
                }
            }
        }
        else if (parsedVariable.value.equalsIgnoreCase("addworld")) {
            ParsedValue<String> world = ArgsParser.parseString(args, 2);
            if (world.failed) {
                logger.logInfoPrefix("Error with the world to add");
            }
            else {
                WorldLookup.addBlockDisallowedWorld(limiter.id, world.value);
                logger.logInfoPrefix("Added world: " + keyword(world.value));
                AdvancedBanItem.getInstance().getLimitManager().saveDataToConfig();
            }
        }
        else if (parsedVariable.value.equalsIgnoreCase("removeworld")) {
            ParsedValue<String> world = ArgsParser.parseString(args, 2);
            if (world.failed) {
                logger.logInfoPrefix("Error with the world to add");
            }
            else {
                WorldLookup.removeBlockDisallowedWorld(limiter.id, world.value);
                logger.logInfoPrefix("Removed world: " + keyword(world.value));
                AdvancedBanItem.getInstance().getLimitManager().saveDataToConfig();
            }
        }
        else if (parsedVariable.value.equalsIgnoreCase("place")) {
            if (args[2].equals("!")) {
                logger.logInfoPrefix("Removing the place permissions. It was originally " + keyword(limiter.placePermission));
                limiter.placePermission = null;
            }
            else {
                logger.logInfoPrefix("Changing the permission required to place the " +
                                     "block from: " + keyword(limiter.breakPermission) +
                                     " to " + keyword(args[2]));
                limiter.placePermission = args[2];
            }
            AdvancedBanItem.getInstance().getLimitManager().saveDataToConfig();
        }
        else if (parsedVariable.value.equalsIgnoreCase("break")) {
            if (args[2].equals("!")) {
                logger.logInfoPrefix("Removing the break permissions. It was originally " + keyword(limiter.placePermission));
                limiter.placePermission = null;
            }
            else {
                logger.logInfoPrefix("Changing the permission required to break the " +
                                     "block from: " + keyword(limiter.breakPermission) +
                                     " to " + keyword(args[2]));
                limiter.breakPermission = args[2];
            }
            AdvancedBanItem.getInstance().getLimitManager().saveDataToConfig();
        }
        else if (parsedVariable.value.equalsIgnoreCase("interact")) {
            if (args[2].equals("!")) {
                logger.logInfoPrefix("Removing the interact permissions. It was originally " + keyword(limiter.placePermission));
                limiter.placePermission = null;
            }
            else {
                logger.logInfoPrefix("Changing the permission required to interact with the " +
                                     "block from: " + keyword(limiter.interactPermission) +
                                     " to " + keyword(args[2]));
                limiter.interactPermission = args[2];
            }
            AdvancedBanItem.getInstance().getLimitManager().saveDataToConfig();
        }
        else if (parsedVariable.value.equalsIgnoreCase("placemsg")) {
            String newMessage = ArgsParser.combineEndArgs(args, 2).value;
            logger.logInfoPrefix("Changing the message from " +
                                 keyword(limiter.noPlaceMessage) + " to " +
                                 keyword(newMessage));
            limiter.noPlaceMessage = newMessage;
            AdvancedBanItem.getInstance().getLimitManager().saveDataToConfig();
        }
        else if (parsedVariable.value.equalsIgnoreCase("breakmsg")) {
            String newMessage = ArgsParser.combineEndArgs(args, 2).value;
            logger.logInfoPrefix("Changing the message from " +
                                 keyword(limiter.noBreakMessage) + " to " +
                                 keyword(newMessage));
            limiter.noBreakMessage = newMessage;
            AdvancedBanItem.getInstance().getLimitManager().saveDataToConfig();
        }
        else if (parsedVariable.value.equalsIgnoreCase("interactmsg")) {
            String newMessage = ArgsParser.combineEndArgs(args, 2).value;
            logger.logInfoPrefix("Changing the message from " +
                                 keyword(limiter.noInteractMessage) + " to " +
                                 keyword(newMessage));
            limiter.noInteractMessage = newMessage;
            AdvancedBanItem.getInstance().getLimitManager().saveDataToConfig();
        }
        else if (parsedVariable.value.equalsIgnoreCase("invertmetadata")) {
            ParsedValue<Boolean> parsedInvert = ArgsParser.parseBoolean(args, 2);
            if (parsedInvert.failed) {
                logger.logInfoPrefix("Error with the boolean value. it can be t/true or f/false");
            }
            else {
                limiter.invertMetadata = parsedInvert.value;
                logger.logInfoPrefix("Set invert metadata to " + keyword(limiter.invertMetadata));
                AdvancedBanItem.getInstance().getLimitManager().saveDataToConfig();
            }
        }
        else if (parsedVariable.value.equalsIgnoreCase("invertworlds")) {
            ParsedValue<Boolean> parsedInvert = ArgsParser.parseBoolean(args, 2);
            if (parsedInvert.failed) {
                logger.logInfoPrefix("Error with the boolean value. it can be t/true or f/false");
            }
            else {
                limiter.invertWorld = parsedInvert.value;
                logger.logInfoPrefix("Set invert worlds to " + keyword(limiter.invertWorld));
                AdvancedBanItem.getInstance().getLimitManager().saveDataToConfig();
            }
        }
        else if (parsedVariable.value.equalsIgnoreCase("invertperms")) {
            ParsedValue<Boolean> parsedInvert = ArgsParser.parseBoolean(args, 2);
            if (parsedInvert.failed) {
                logger.logInfoPrefix("Error with the boolean value. it can be t/true or f/false");
            }
            else {
                limiter.invertPermission = parsedInvert.value;
                logger.logInfoPrefix("Set invert permissions to " + keyword(limiter.invertPermission));
                AdvancedBanItem.getInstance().getLimitManager().saveDataToConfig();
            }
        }
        else {
            logger.logInfoPrefix("That variable doesn't exist. Theres: id, invertworlds, invertperms, place, break, interact");
            logger.logInfoPrefix("These 4 aren't really variables, but: addmeta, removemeta, addworld, removeworld");
        }
    }

    private static String keyword(String keyword) {
        return ChatColor.DARK_AQUA + keyword + ChatColor.GOLD;
    }

    private static String keyword(Integer keyword) {
        return ChatColor.DARK_AQUA + String.valueOf(keyword) + ChatColor.GOLD;
    }

    private static String keyword(Boolean keyword) {
        return ChatColor.DARK_AQUA + String.valueOf(keyword) + ChatColor.GOLD;
    }
}
