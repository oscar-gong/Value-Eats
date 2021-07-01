package com.nuggets.valueeats.controller;

import java.util.Map;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.nuggets.valueeats.entity.Diner;
import com.nuggets.valueeats.entity.Eatery;
import com.nuggets.valueeats.entity.User;
import com.nuggets.valueeats.entity.Review;
import com.nuggets.valueeats.service.CuisineService;
import com.nuggets.valueeats.service.UserManagementService;
import com.nuggets.valueeats.service.DinerFunctionalityService;

import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.CrossOrigin;

@CrossOrigin(origins = ControllerConstants.URL)
@RestController
public final class ReviewController {
    @Autowired
    private UserManagementService userManagementService;
    
    @Autowired
    private CuisineService cuisineService;

    @Autowired
    private DinerFunctionalityService dinerFunctionalityService;

    @RequestMapping(value = "create/review", method = RequestMethod.POST)
    public ResponseEntity<JSONObject> createReview(@RequestBody String jsonString){
        return dinerFunctionalityService.createReview(jsonString);
    }
}
