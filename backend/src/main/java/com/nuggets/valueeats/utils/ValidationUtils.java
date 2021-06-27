package com.nuggets.valueeats.utils;

import java.util.regex.Pattern;

public class ValidationUtils {
    public static boolean isValidEmail(final String email) {
        String regex = "^([\\w-\\.]+){1,64}@([\\w&&[^_]]+){2,255}.[a-z]{2,}$";
        return Pattern.matches(regex, email);
    }

    public static boolean isValidPassword(final String password) {
        // Checks password is: between 8 to 32 char long, contains one uppercase and lowercase
        String regex = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)[a-zA-Z\\d\\w\\W]{8,32}$";
        return Pattern.matches(regex, password);
    }
}
