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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.CrossOrigin;

@CrossOrigin(origins = ControllerConstants.URL)
@RestController
public final class AuthenticationController {
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
    public ResponseEntity<JSONObject> updateDiner(@RequestBody final Diner diner) {
        return userManagementService.updateDiner(diner);
    }

    @RequestMapping(value = "update/eatery", method = RequestMethod.POST)
    public ResponseEntity<JSONObject> updateDiner(@RequestBody final Eatery eatery) {
        return userManagementService.updateEatery(eatery);
    }


    @RequestMapping(value = "logout", method = RequestMethod.POST)
    public ResponseEntity<JSONObject> logout(@RequestBody User user) {
        return userManagementService.logout(user);
    }

    @RequestMapping(value = "list/cuisines", method = RequestMethod.GET)
    public ResponseEntity<JSONObject> logout() {
        return cuisineService.listCuisines();
    }
}
