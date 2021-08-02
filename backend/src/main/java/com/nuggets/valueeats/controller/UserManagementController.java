package com.nuggets.valueeats.controller;

import com.nuggets.valueeats.controller.decorator.token.CheckUserToken;
import com.nuggets.valueeats.entity.Diner;
import com.nuggets.valueeats.entity.Eatery;
import com.nuggets.valueeats.entity.User;
import com.nuggets.valueeats.service.CuisineService;
import com.nuggets.valueeats.service.UserManagementService;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = ControllerConstants.URL)
@RestController
public class UserManagementController {
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
    @CheckUserToken
    public ResponseEntity<JSONObject> updateDiner(
            @RequestHeader(name = "Authorization") String token, @RequestBody final Diner diner) {
        return userManagementService.updateDiner(diner, token);
    }

    @RequestMapping(value = "update/eatery", method = RequestMethod.POST)
    @CheckUserToken
    public ResponseEntity<JSONObject> updateEatery(
            @RequestHeader(name = "Authorization") String token, @RequestBody final Eatery eatery) {
        return userManagementService.updateEatery(eatery, token);
    }

    @RequestMapping(value = "logout", method = RequestMethod.POST)
    @CheckUserToken
    public ResponseEntity<JSONObject> logout(@RequestHeader(name = "Authorization") String token) {
        return userManagementService.logout(token);
    }

    @RequestMapping(value = "list/cuisines", method = RequestMethod.GET)
    public ResponseEntity<JSONObject> listCuisines() {
        return cuisineService.listCuisines();
    }

    @RequestMapping(value = "eatery/profile/details", method = RequestMethod.GET)
    @CheckUserToken
    public ResponseEntity<JSONObject> getEateryProfile(
            @RequestHeader(name = "Authorization") String token, @RequestParam(required = false) Long id) {
        return userManagementService.getEateryProfile(id, token);
    }

    @RequestMapping(value = "diner/profile/details", method = RequestMethod.GET)
    @CheckUserToken
    public ResponseEntity<JSONObject> getDinerProfile(@RequestHeader(name = "Authorization") String token) {
        return userManagementService.getDinerProfile(token);
    }
}
