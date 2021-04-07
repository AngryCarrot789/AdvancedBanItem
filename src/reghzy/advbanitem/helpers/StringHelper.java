package reghzy.advbanitem.helpers;

public class StringHelper {
    public boolean isEmpty(String string) {
        return string == null || string.isEmpty();
    }

    public static String repeat(char repeat, int count) {
        StringBuilder string = new StringBuilder(count);
        for (int i = 0; i < count; i++) {
            string.append(repeat);
        }
        return string.toString();
    }

    public static String repeat(String repeat, int count) {
        StringBuilder string = new StringBuilder(repeat.length() * count);
        for (int i = 0; i < count; i++) {
            string.append(repeat);
        }
        return string.toString();
    }

    public static String ensureLength(String string, int maxLength, char fillValue) {
        int extra = maxLength - string.length();
        if (extra > 0)
            return string + repeat(fillValue, extra);
        if (extra < 0)
            return string.substring(0, maxLength);
        else
            return string;
    }

    public static String joinArray(String[] args, int offset, char joinCharacter) {
        if ((args == null) || (offset < 0) || (offset >= args.length))
            return null;

        StringBuilder string = new StringBuilder(args.length);
        for(int i = offset; i < args.length; i++) {
            string.append(args[i]).append(joinCharacter);
        }
        return string.toString();
    }

    public static boolean containsChar(String string, char character) {
        for(int i = 0; i < string.length(); i++) {
            if (string.charAt(i) == character)
                return true;
        }
        return false;
    }
}
