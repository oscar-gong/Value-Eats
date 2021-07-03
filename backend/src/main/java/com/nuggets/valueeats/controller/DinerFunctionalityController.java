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
public final class DinerFunctionalityController {
    @Autowired
    private UserManagementService userManagementService;
    
    @Autowired
    private CuisineService cuisineService;

    @Autowired
    private DinerFunctionalityService dinerFunctionalityService;

    @RequestMapping(value = "diner/createreview", method = RequestMethod.POST)
    public ResponseEntity<JSONObject> createReview(@RequestBody Review review, @RequestHeader (name="Authorization") String token){
        return dinerFunctionalityService.createReview(review, token);
    }

    @RequestMapping(value = "diner/removereview", method = RequestMethod.DELETE)
    public ResponseEntity<JSONObject> removeReview(@RequestBody Review review, @RequestHeader (name="Authorization") String token){
        return dinerFunctionalityService.removeReview(review, token);
    }
    
    @RequestMapping(value = "list/eateries", method = RequestMethod.GET)
    public ResponseEntity<JSONObject> listEateries(@RequestHeader (name="Authorization") String token) {
        return dinerFunctionalityService.listEateries(token);
    }

    @RequestMapping(value = "diner/editreview", method = RequestMethod.POST)
    public ResponseEntity<JSONObject> editReview(@RequestBody Review review, @RequestHeader (name="Authorization") String token){
        return dinerFunctionalityService.editReview(review, token);
    }
}
