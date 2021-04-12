package reghzy.advbanitem.helpers;

public class StringHelper {
    public boolean isEmpty(String string) {
        return string == null || string.isEmpty();
    }

    /**
     * Parses integers without throwing exceptions, simply returns null if it failed to parse (more efficient, no extra class creation on failure to parse)
     */
    public static Integer parseInteger(String value) {
        int radix = 10;
        if (value == null) {
            return null;
        }

        int result = 0;
        boolean isNegative = false;
        int i = 0, len = value.length();
        int limit = -Integer.MAX_VALUE;
        int radixMinLimit;
        int digit;

        if (len > 0) {
            char firstChar = value.charAt(0);
            if (firstChar < '0') { // Possible leading "+" or "-"
                if (firstChar == '-') {
                    isNegative = true;
                    limit = Integer.MIN_VALUE;
                }
                else if (firstChar != '+')
                    return null;

                if (len == 1) // Cannot have lone "+" or "-"
                    return null;
                i++;
            }
            radixMinLimit = limit / radix;
            while (i < len) {
                // Accumulating negatively avoids surprises near MAX_VALUE
                digit = Character.digit(value.charAt(i++), radix);
                if (digit < 0) {
                    return null;
                }
                if (result < radixMinLimit) {
                    return null;
                }
                result *= radix;
                if (result < limit + digit) {
                    return null;
                }
                result -= digit;
            }
        }
        else {
            return null;
        }
        return isNegative ? result : -result;
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
        for (int i = offset, lenIndex = args.length - 1; i < args.length; i++) {
            if (i == lenIndex) {
                string.append(args[i]);
            }
            else {
                string.append(args[i]).append(joinCharacter);
            }
        }
        return string.toString();
    }

    public static String joinArray(String[] args, int offset, String joinText) {
        if ((args == null) || (offset < 0) || (offset >= args.length))
            return null;

        StringBuilder string = new StringBuilder(args.length);
        for (int i = offset, lenIndex = args.length - 1; i < args.length; i++) {
            if (i == lenIndex) {
                string.append(args[i]);
            }
            else {
                string.append(args[i]).append(joinText);
            }
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

    public static int countChar(String string, char character) {
        int count = 0;
        for (int i = 0; i < string.length(); i++) {
            if (string.charAt(i) == character)
                count++;
        }
        return count;
    }
}
