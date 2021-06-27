package com.nuggets.valueeats.utils;

import org.json.simple.JSONObject;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public class AuthenticationUtils {
    public static ResponseEntity<JSONObject> loginPasswordCheck(final String loginPassword, final String secret, final String actualPassword, final String successMessage) {
        if (EncryptionUtils.encrypt(loginPassword, secret).equals(actualPassword)) {
            return ResponseEntity.status(HttpStatus.OK).body(ResponseUtils.createResponse(successMessage));
        }

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ResponseUtils.createResponse("Invalid password, please try again"));
    }
}
