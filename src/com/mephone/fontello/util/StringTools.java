package com.mephone.fontello.util;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class StringTools {
    public static String format(String format, Object... args) {
        return String.format(Locale.US, format, args);
    }

    public static long parseLong(String text, long def) {
        try {
            if (!StringTools.isNonEmpty(text)) {
                return def;
            }
            return Long.parseLong(text);
        } catch (Throwable e) {
            return def;
        }
    }

    public static float parseFloat(String text, float def) {
        try {
            if (!StringTools.isNonEmpty(text)) {
                return def;
            }
            return Float.parseFloat(text);
        } catch (Throwable e) {
            return def;
        }
    }

    public static int parseInt(String text, int def) {
        try {
            if (!StringTools.isNonEmpty(text)) {
                return def;
            }
            return Integer.parseInt(text);
        } catch (Throwable e) {
            return def;
        }
    }

    public static boolean parseBoolean(String text, boolean def) {
        try {
            if (!StringTools.isNonEmpty(text)) {
                return def;
            }
            return Boolean.parseBoolean(text);
        } catch (Throwable e) {
            return def;
        }
    }

    public static boolean isNonEmpty(String text) {
        return text != null && !"".equals(text);
    }

    public static String toUpperCase(String in) {
        if (isNonEmpty(in)) {
            return in.toUpperCase();
        }
        return in;
    }

    public static String toLowerCase(String in) {
        if (isNonEmpty(in)) {
            return in.toLowerCase();
        }
        return in;
    }

    public static String getTimestamp(long millis) {
        Date date = new Date(millis);
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS",
                Locale.ENGLISH);
        return df.format(date);
    }

    public static String getTimestampShort(long millis) {
        Date date = new Date(millis);
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss",
                Locale.ENGLISH);
        return df.format(date);
    }

}
