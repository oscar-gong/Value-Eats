package com.nuggets.valueeats.utils;

import org.json.simple.JSONObject;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Map;

public class AuthenticationUtils {
    public static ResponseEntity<JSONObject> loginPasswordCheck(final String loginPassword, final String secret,
            final String actualPassword, final String successMessage,
            final boolean isDiner, Map<String, String> dataMedium) {
        if (EncryptionUtils.encrypt(loginPassword, secret).equals(actualPassword)) {

            dataMedium.put("type", (isDiner ? "diner" : "eatery"));
            JSONObject data = new JSONObject(dataMedium);

            return ResponseEntity.status(HttpStatus.OK).body(ResponseUtils.createResponse(successMessage, data));
        }

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ResponseUtils.createResponse("Invalid password, please try again"));
    }
}
