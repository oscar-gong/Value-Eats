package com.nuggets.valueeats.controller;

import com.nuggets.valueeats.entity.Diner;
import com.nuggets.valueeats.entity.Eatery;
import com.nuggets.valueeats.entity.User;
import com.nuggets.valueeats.service.CuisineService;
import com.nuggets.valueeats.service.UserManagementService;
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
import org.springframework.web.bind.annotation.PathVariable;

@CrossOrigin(origins = ControllerConstants.URL)
@RestController
public final class UserManagementController {
    @Autowired
    private UserManagementService userManagementService;

    @Autowired
    private CuisineService cuisineService;

    @RequestMapping(value = "login", method = RequestMethod.POST)
    public ResponseEntity<JSONObject> login(@RequestBody final User user) {
        return userManagementService.login(user);
    }

    @RequestMapping(value = "register/diner", method = RequestMethod.POST)
    public ResponseEntity<JSONObject> registerDiner(@RequestBody final Diner diner) {
        return userManagementService.registerDiner(diner);
    }

    @RequestMapping(value = "register/eatery", method = RequestMethod.POST)
    public ResponseEntity<JSONObject> registerEatery(@RequestBody final Eatery eatery) {
        return userManagementService.registerEatery(eatery);
    }

    @RequestMapping(value = "update/diner", method = RequestMethod.POST)
    public ResponseEntity<JSONObject> updateDiner(@RequestBody final Diner diner, @RequestHeader (name="Authorization") String token) {
        return userManagementService.updateDiner(diner, token);
    }

    @RequestMapping(value = "update/eatery", method = RequestMethod.POST)
    public ResponseEntity<JSONObject> updateEatery(@RequestBody final Eatery eatery, @RequestHeader (name="Authorization") String token) {
        return userManagementService.updateEatery(eatery, token);
    }


    @RequestMapping(value = "logout", method = RequestMethod.POST)
    public ResponseEntity<JSONObject> logout(@RequestHeader (name="Authorization") String token) {
        return userManagementService.logout(token);
    }

    @RequestMapping(value = "list/cuisines", method = RequestMethod.GET)
    public ResponseEntity<JSONObject> listCuisines() {
        return cuisineService.listCuisines();
    }

    @RequestMapping(value = "eatery/profile/details", method = RequestMethod.GET)
    public ResponseEntity<JSONObject> getEateryProfile(@RequestParam(required=false) Long id, @RequestHeader (name="Authorization") String token) {
        return userManagementService.getEateryProfile(id, token);
    }

    @RequestMapping(value = "diner/profile/details", method = RequestMethod.GET)
    public ResponseEntity<JSONObject> getDinerProfile(@RequestHeader (name="Authorization") String token) {
        return userManagementService.getDinerProfile(token);
    }
}
