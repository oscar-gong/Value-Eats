package com.nuggets.valueeats.service;

import java.util.HashMap;

import javax.transaction.Transactional;

import org.json.simple.JSONObject;
import org.springframework.stereotype.Service;

@Service
public class ResponseService {

    @Transactional
    public JSONObject createResponse (String message) {
        HashMap<String,String> response = new HashMap<String,String>();
        response.put("message", message);
        JSONObject jsonResponse = new JSONObject(response);
        return jsonResponse;
    }

    public JSONObject createResponse(String message, JSONObject result) {
        HashMap<String,Object> response = new HashMap<String,Object>();
        response.put("message", message);
        response.put("data", result);
        JSONObject jsonResponse = new JSONObject(response);
        return jsonResponse;
    }
}
