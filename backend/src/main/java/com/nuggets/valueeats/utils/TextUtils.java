package com.nuggets.valueeats.utils;

public final class TextUtils {
    public static String toTitle(String string) {
        string = string.trim();
        string = string.toLowerCase();
        string = string.substring(0, 1).toUpperCase();

        return string;
    }
}
