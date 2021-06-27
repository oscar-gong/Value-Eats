package com.nuggets.valueeats.utils;

import java.util.regex.Pattern;

public class ValidationUtils {
    public static boolean isValidEmail(final String email) {
        String regex = "^([\\w-\\.]+){1,64}@([\\w&&[^_]]+){2,255}.[a-z]{2,}$";
        return Pattern.matches(regex, email);
    }

    public static boolean isValidPassword(final String password) {
        return password.length() >= 8 &&
                password.length() <= 32 &&
                !password.toLowerCase().equals(password) &&
                !password.toUpperCase().equals(password);
    }
}
