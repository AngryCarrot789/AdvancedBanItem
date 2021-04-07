package reghzy.advbanitem.command.helpers;

import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import reghzy.advbanitem.helpers.StringHelper;
import reghzy.advbanitem.helpers.WorldHelper;

// a null-safe class used for parsing commands and stuff.
// never directly returns null, and never throws exceptions
public final class ArgsParser {
    /** Returns true if the index is too big, meaning the array is too small. True == Out of range
     * @param array the args array
     * @param smallestIndexRequired The length - 1, aka the max length required to index without worry... sort of
     * @return True if index is out of range
     */
    public static boolean argsTooSmall(Object[] array, int smallestIndexRequired){
        if (array == null){
            return true;
        }
        return array.length <= smallestIndexRequired;
    }

    public static ParsedValue<ItemDataPair> parseItemData(String[] args, int index) {
        ParsedValue<String> itemData = parseString(args, index);
        if (itemData.failed)
            return new ParsedValue<ItemDataPair>(null, true);
        try {
            if (itemData.value.contains(":")) {
                int dataSplit = itemData.value.indexOf(":");
                int id = Integer.parseInt(itemData.value.substring(0, dataSplit - 1));
                int data = Integer.parseInt(itemData.value.substring(dataSplit + 1));
                return new ParsedValue<ItemDataPair>(new ItemDataPair(id, data), false);
            }
            else {
                return new ParsedValue<ItemDataPair>(new ItemDataPair(Integer.parseInt(itemData.value), -1), false);
            }
        }
        catch (Exception ignored) {
        }
        return new ParsedValue<ItemDataPair>(null, true);
    }

    public static ParsedValue<String> parseString(String[] args, int index) {
        if (argsTooSmall(args, index)) {
            return new ParsedValue<String>("", true);
        }
        return new ParsedValue<String>(args[index], false);
    }

    public static ParsedValue<Boolean> parseBoolean(String[] args, int index) {
        if (argsTooSmall(args, index)) {
            return new ParsedValue<Boolean>(false, true);
        }
        String content = args[index];
        if (content.equalsIgnoreCase("true") || content.equalsIgnoreCase("t")) {
            return new ParsedValue<Boolean>(true, false);
        }
        else {
            return new ParsedValue<Boolean>(false, false);
        }
    }

    /**
     * Parses custom boolean true/false keywords from the content at the given args index. this will simply check
     * if the content contains any of the true/false values. contains... not equals
     * <p>
     * e.g, given String[] arr = { hello, do, dont, b },
     * </p>
     * <p>
     * parseBoolean(arr, 1, "do", "dontdonot"); would return a parsed true, but
     * parseBoolean(arr, 2, "do", "dontdonot"); would return a parsed false
     * parseBoolean(arr, 3, "do", "dontdonot"); would return a failed parse
     * </p>
     * @param args the array
     * @param index where to extract the content from
     * @param trueStrings the string that contains all the true cases
     * @param falseStrings the string that contains all the false cases
     * @return a parsed bool or failed parse
     */
    public static ParsedValue<Boolean> parseBoolean(String[] args, int index, String[] trueStrings, String[] falseStrings) {
        if (argsTooSmall(args, index)) {
            return new ParsedValue<Boolean>(false, true);
        }
        String content = args[index];
        for (String str : trueStrings) {
            if (content.contains(str)) {
                return new ParsedValue<Boolean>(true, false);
            }
        }
        for (String str : falseStrings) {
            if (content.contains(str)) {
                return new ParsedValue<Boolean>(true, false);
            }
        }
        return new ParsedValue<Boolean>(false, true);
    }

    /**
     * tries to parse the value in args at the given index as an integer
     */
    public static ParsedValue<Integer> parseInteger(String[] args, int index){
        if (argsTooSmall(args, index)) {
            return new ParsedValue<Integer>(0, true);
        }
        try{
            return new ParsedValue<Integer>(Integer.parseInt(args[index]), false);
        }
        catch (Exception e){
            return new ParsedValue<Integer>(0, true);
        }
    }

    /**
     * tries to parse the value in args at the given index as a double (including decimals and stuff)
     */
    public static ParsedValue<Double> parseDouble(String[] args, int index){
        if (argsTooSmall(args, index)) {
            return new ParsedValue<Double>(0.0d, true);
        }
        try{
            return new ParsedValue<Double>(Double.parseDouble(args[index]), false);
        }
        catch (Exception e){
            return new ParsedValue<Double>(0.0d, true);
        }
    }

    /**
     * Tries to get the senders world. wont fail if they're a player, will fail if they're console.
     * a more compact way of getting the world than using instanceof tbh
     */
    public static ParsedValue<World> getSenderWorld(CommandSender sender) {
        if (sender instanceof Player) {
            return new ParsedValue<World>(((Player)sender).getWorld(), false);
        }
        else {
            return new ParsedValue<World>(null, true);
        }
    }

    /**
     * Tries to get the world name in args at the given index, then tries to find the world with that name
     */
    public static ParsedValue<World> parseWorld(String[] args, int index) {
        if (argsTooSmall(args, index)) {
            return new ParsedValue<World>(null, true);
        }
        String worldName = args[index];
        World world = WorldHelper.getWorldFromName(worldName);
        if (world == null) {
            return new ParsedValue<World>(null, true);
        }
        return new ParsedValue<World>(world, false);
    }

    /**
     * Tries to either parse the world name in args at the given index
     * and find the world with that name, or tries to get the sender's world
     */
    public static ParsedValue<World> parseWorldOrPlayerWorld(CommandSender sender, String[] args, int index) {
        ParsedValue<World> world = parseWorld(args, index);
        if (world.failed) {
            return getSenderWorld(sender);
        }
        else {
            return world;
        }
    }

    /**
     * Combines an array of characters (offset from the start by the given offsetIndex)
     * and joins it into a string (joined with a whitespace (' '))
     * <p>
     *     e.g.: combineEndArgs({hello, okay, no, ok}, 1) == "okay no ok"
     * </p>
     */
    public static ParsedValue<String> combineEndArgs(String[] args, int offsetIndex) {
        String joined = StringHelper.joinArray(args, offsetIndex, ' ');
        return new ParsedValue<String>(joined, joined == null);
    }

    public static String GetCommand(String[] fullArgs) {
        if (argsTooSmall(fullArgs, 0)) {
            return "command_missing";
        }
        return fullArgs[0];
    }

    public static String[] extractCommandArgs(String[] fullArgs) {
        return extractArrayOffset(fullArgs, 1);
    }

    /**
     * Returns a new array which contains the contents of the given array but the contents at the start
     * are removed based on the startIndex. removes stuff based on an offset from the start basically
     * <p>
     *     example:
     *     <p>
     *         extractArrayOffsets({1, 2, 3, 4, 5}, 1) returns { 2, 3, 4, 5 }
     *         extractArrayOffsets({1, 2, 3, 4, 5}, 3) returns { 4, 5 }
     *     </p>
     * </p>
     */
    public static String[] extractArrayOffset(String[] array, int startIndex) {
        if (startIndex == 0) {
            return array;
        }
        if (array == null || startIndex < 0 || (startIndex >= array.length)) {
            return null;
        }

        String[] newArray = new String[array.length - startIndex];
        System.arraycopy(array, startIndex, newArray, 0, newArray.length);
        return newArray;
    }

    /**
     * Returns a new array which contains the contents of the given array but the contents at the start
     * are removed based on the startIndex, and the end is removed from the endIndex.
     * removes stuff based on an offset from the start basically
     * <p>
     *     example:
     *     extractArrayOffsets({1, 2, 3, 4, 5}, 1, 3) returns { 2, 3, 4}
     *     extractArrayOffsets({1, 2, 3, 4, 5}, 3) returns { 4, 5 }
     * </p>
     */
    public static String[] extractArrayOffset(String[] array, int startIndex, int endIndex) {
        if (array == null) {
            return null;
        }
        if (startIndex < 0 ||  endIndex < 0 || startIndex > endIndex || (endIndex >= array.length)) {
            return null;
        }

        // 6.... 0, 3 == 4
        // [0][1][2][3][4][5]
        // [t][t][t][t][f][f]

        // 7.... 2, 4 == 3
        // [0][1][2][3][4][5][5]
        // [f][f][t][t][t][f][f]

        // 12.... 2, 4 == 3
        // [][][][][][][][][][][][][]

        int copyAmount = 0;
        String[] newArray = new String[endIndex - startIndex + 1];
        System.arraycopy(array, startIndex, newArray, 0, newArray.length);
        return newArray;
    }
}
