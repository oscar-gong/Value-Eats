package com.nuggets.valueeats.service;

import org.json.simple.JSONObject;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public final class ResponseService {
    public JSONObject createResponse(final String message) {
        Map<String, String> response = new HashMap<>();
        response.put("message", message);

        return new JSONObject(response);
    }

    public JSONObject createResponse(final String message, final JSONObject result) {
        Map<String, Object> response = new HashMap<>();
        response.put("message", message);
        response.put("data", result);

        return new JSONObject(response);
    }
}
