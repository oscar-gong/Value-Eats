package com.nuggets.valueeats.service;

import com.nuggets.valueeats.entity.User;
import org.springframework.stereotype.Service;

import java.util.regex.Pattern;

@Service
public final class UserService {
    public String validInputChecker(final User user) {
        if (!isValidEmail(user.getEmail())) {
            return "Invalid Email Format.";
        }
        if (!isValidPassword(user.getPassword())) {
            return "Password must be between 8 to 32 characters long, and contain a lower and uppercase character.";
        }

        return null;
    }

    private boolean isValidEmail(final String email) {
        String regex = "^([\\w-\\.]+){1,64}@([\\w&&[^_]]+){2,255}.[a-z]{2,}$";
        return Pattern.matches(regex, email);
    }

    private boolean isValidPassword(final String password) {
        // Checks password is: between 8 to 32 char long, contains one uppercase and lowercase
        String regex = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)[a-zA-Z\\d\\w\\W]{8,32}$";
        return Pattern.matches(regex, password);
    }
}
