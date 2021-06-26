package com.nuggets.valueeats.service;

import org.json.simple.JSONObject;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.HashMap;

@Service
public class ResponseService {
    public JSONObject createResponse(final String message) {
        HashMap<String, String> response = new HashMap<>();
        response.put("message", message);

        return new JSONObject(response);
    }

    public JSONObject createResponse(final String message, final JSONObject result) {
        HashMap<String, Object> response = new HashMap<>();
        response.put("message", message);
        response.put("data", result);

        return new JSONObject(response);
    }
}
