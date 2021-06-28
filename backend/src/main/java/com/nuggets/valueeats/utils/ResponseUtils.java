package com.nuggets.valueeats.utils;

import org.json.simple.JSONObject;

import java.util.HashMap;
import java.util.Map;

public final class ResponseUtils {
    public static JSONObject createResponse(final String message) {
        Map<String, String> response = new HashMap<>();
        response.put("message", message);

        return new JSONObject(response);
    }

    public static JSONObject createResponse(final Exception exception) {
        return createResponse(exception.getMessage());
    }

    public static JSONObject createResponse(final String message, final JSONObject result) {
        Map<String, Object> response = new HashMap<>();
        response.put("message", message);
        response.put("data", result);

        return new JSONObject(response);
    }

    public static JSONObject createResponse(final JSONObject result) {
        // Map<String, Object> response = new HashMap<>();
        // response.put("data", result);

        return new JSONObject(result);
    }
}
