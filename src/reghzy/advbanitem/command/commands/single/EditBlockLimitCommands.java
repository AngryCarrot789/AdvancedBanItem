package reghzy.advbanitem.command.commands.single;

import org.bukkit.command.CommandSender;
import org.omg.CosNaming._BindingIteratorImplBase;
import reghzy.advbanitem.AdvancedBanItem;
import reghzy.advbanitem.command.CommandDescriptor;
import reghzy.advbanitem.command.ExecutableCommand;
import reghzy.advbanitem.command.helpers.ArgsParser;
import reghzy.advbanitem.command.helpers.ParsedValue;
import reghzy.advbanitem.helpers.PermissionsHelper;
import reghzy.advbanitem.helpers.StringHelper;
import reghzy.advbanitem.limit.BlockLimiter;
import reghzy.advbanitem.limit.LimitManager;
import reghzy.advbanitem.limit.WorldLookup;
import reghzy.advbanitem.logs.ChatFormat;
import reghzy.advbanitem.logs.ChatLogger;

import java.util.ArrayList;

public class EditBlockLimitCommands implements ExecutableCommand {
    public static CommandDescriptor descriptor =
            new CommandDescriptor(
                    "edit",
                    "<id> <variable or addworld/removeworld> <value>",
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
                    logger.logInfoPrefix("The IDs are the same... ;)");
                }
                else {
                    logger.logInfoPrefix("Changing the ID from " + limiter.id + " to " + newId.value);
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
                    logger.logInfoPrefix("Added metadata: " + metadata.value);
                }
                else {
                    logger.logInfoPrefix("Metadata " + metadata.value + " is already added!");
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
                    logger.logInfoPrefix("Removed metadata: " + metadata.value);
                }
                else {
                    logger.logInfoPrefix("This block limit doesn't contain the metadata " + metadata.value + "!");
                }
            }
        }
        else if (parsedVariable.value.equalsIgnoreCase("addworld")) {
            WorldLookup.addBlockDisallowedWorld(limiter.id, args[2]);
            logger.logInfoPrefix("Added world: " + args[2]);
            AdvancedBanItem.getInstance().getLimitManager().saveDataToConfig();
        }
        else if (parsedVariable.value.equalsIgnoreCase("removeworld")) {
            WorldLookup.removeBlockDisallowedWorld(limiter.id, args[2]);
            logger.logInfoPrefix("Removed world: " + args[2]);
            AdvancedBanItem.getInstance().getLimitManager().saveDataToConfig();
        }
        else if (parsedVariable.value.equalsIgnoreCase("place")) {
            logger.logInfoPrefix("Changing the permission required to place the " +
                                 "block from: " + limiter.placePermission + " to " + args[2]);
            limiter.placePermission = args[2];
            AdvancedBanItem.getInstance().getLimitManager().saveDataToConfig();
        }
        else if (parsedVariable.value.equalsIgnoreCase("break")) {
            logger.logInfoPrefix("Changing the permission required to break the " +
                                 "block from: " + limiter.placePermission + " to " + args[2]);
            limiter.breakPermission = args[2];
            AdvancedBanItem.getInstance().getLimitManager().saveDataToConfig();
        }
        else if (parsedVariable.value.equalsIgnoreCase("interact")) {
            logger.logInfoPrefix("Changing the permission required to break the " +
                                 "block from: " + limiter.placePermission + " to " + args[2]);
            limiter.interactPermission = args[2];
            AdvancedBanItem.getInstance().getLimitManager().saveDataToConfig();
        }
        else if (parsedVariable.value.equalsIgnoreCase("placemsg")) {
            String newMessage = ArgsParser.combineEndArgs(args, 2).value;
            logger.logInfoPrefix("Changing the \"no permission to place\" message from " +
                                 limiter.noPlaceMessage + " to " +
                                 newMessage);
            limiter.noPlaceMessage = newMessage;
            AdvancedBanItem.getInstance().getLimitManager().saveDataToConfig();
        }
        else if (parsedVariable.value.equalsIgnoreCase("breakmsg")) {
            String newMessage = ArgsParser.combineEndArgs(args, 2).value;
            logger.logInfoPrefix("Changing the \"no permission to break\" message from " +
                                 limiter.noBreakMessage + " to " +
                                 newMessage);
            limiter.noBreakMessage = newMessage;
            AdvancedBanItem.getInstance().getLimitManager().saveDataToConfig();
        }
        else if (parsedVariable.value.equalsIgnoreCase("interactmsg")) {
            String newMessage = ArgsParser.combineEndArgs(args, 2).value;
            logger.logInfoPrefix("Changing the \"no permission to interact\" message from " +
                                 limiter.noInteractMessage + " to " +
                                 newMessage);
            limiter.noInteractMessage = newMessage;
            AdvancedBanItem.getInstance().getLimitManager().saveDataToConfig();
        }
        else if (parsedVariable.value.equalsIgnoreCase("invertworlds")) {
            ParsedValue<Boolean> parsedInvert = ArgsParser.parseBoolean(args, 2);
            if (parsedInvert.failed) {
                logger.logInfoPrefix("Error with the boolean value. it can be t/true or f/false");
            }
            else {
                limiter.invertWorld = parsedInvert.value;
                logger.logInfoPrefix("Set invert worlds to " + limiter.invertWorld);
                AdvancedBanItem.getInstance().getLimitManager().saveDataToConfig();
            }
        }
        else if (parsedVariable.value.equalsIgnoreCase("invertperms")) {
            ParsedValue<Boolean> parsedInvert = ArgsParser.parseBoolean(args, 2);
            if (parsedInvert.failed) {
                logger.logInfoPrefix("Error with the boolean value. it can be t/true or f/false");
            }
            else {
                limiter.invertWorld = parsedInvert.value;
                logger.logInfoPrefix("Set invert permissions to " + limiter.invertWorld);
                AdvancedBanItem.getInstance().getLimitManager().saveDataToConfig();
            }
        }
        else {
            logger.logInfoPrefix("That variable doesn't exist. Theres: id, meta, invertworlds, invertperms, place, break, interact");
            logger.logInfoPrefix("These 2 aren't really variables, but: addworld, removeworld");
        }
    }
}
