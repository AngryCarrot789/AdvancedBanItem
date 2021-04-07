package reghzy.advbanitem.helpers;

public class MathsHelper {
    public static final float PI = 3.1415926535897931f;

    /**
     * Clamps the value between the given min and max value. e.g. clamp(50, 5, 10) returns 10, clamp(100, 50, 150) returns 100
     */
    public static int clamp(int value, int min, int max) {
        if (value > max) return max;
        if (value < min) return min;
        return value;
    }

    /**
     * Clamps the value between the given min and max value. e.g. clamp(50, 5, 10) returns 10, clamp(100, 50, 150) returns 100
     */
    public static double clamp(double value, double min, double max) {
        if (value > max) return max;
        if (value < min) return min;
        return value;
    }

    /**
     * returns the smallest number between the 3 values
     */
    public static int min3(int a, int b, int c) {
        return Math.min(Math.min(a, b), c);
    }

    /**
     * returns the biggest number between the 3 values
     */
    public static int max3(int a, int b, int c) {
        return Math.max(Math.max(a, b), c);
    }

    /**
     * returns the smallest number between the 3 values
     */
    public static float min3(float a, float b, float c) {
        return Math.min(Math.min(a, b), c);
    }

    /**
     * returns the biggest number between the 3 values
     */
    public static float max3(float a, float b, float c) {
        return Math.max(Math.max(a, b), c);
    }

    /**
     * rounds the given number to the given number of decimal places
     * @param number the number
     * @param places the number of decimal places to round the number to
     */
    public static double round(double number, int places) {
        if (places < 0) {
            places = 0;
        }

        long factor = (long) Math.pow(10, places);
        number = number * factor;
        long tmp = Math.round(number);
        return (double) tmp / factor;
    }

    public static float degreeToRadian(float degrees) {
        return degrees / 57.29577951309679f;
    }

    public static float radianToDegrees(float radians) {
        return radians * 57.29577951309679f;
    }

    /**
     * returns whether the given value is bigger than min and smaller than max
     */
    public static boolean between(double value, double min, double max) {
        return value >= min && value <= max;
    }

    /**
     * returns whether the given value is smaller than min and bigger than max
     */
    public static boolean outside(double value, double min, double max) {
        return value < min || value > max;
    }

    /**
     * exact same as Integer.compare but usable with project language level 6
     */
    public static int compareInt(int a, int b) {
        return (a < b) ? -1 : ((a == b) ? 0 : 1);
    }
}
