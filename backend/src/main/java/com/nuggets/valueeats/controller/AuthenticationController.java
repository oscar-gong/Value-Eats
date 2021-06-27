package com.nuggets.valueeats.controller;

import com.nuggets.valueeats.entity.Diner;
import com.nuggets.valueeats.entity.Eatery;
import com.nuggets.valueeats.entity.LoginCredentials;
import com.nuggets.valueeats.service.DinerService;
import com.nuggets.valueeats.service.EateryService;
import com.nuggets.valueeats.service.LoginCredentialsService;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = ControllerConstants.URL)
@RestController
public final class AuthenticationController {
    @Autowired
    private DinerService dinerService;
    @Autowired
    private EateryService eateryService;
    @Autowired
    private LoginCredentialsService loginCredentialsService;

    @RequestMapping(value = "login", method = RequestMethod.POST)
    public ResponseEntity<JSONObject> login(@RequestBody final LoginCredentials user) {
        return loginCredentialsService.login(user);
    }

    @RequestMapping(value = "register/diner", method = RequestMethod.POST)
    public ResponseEntity<JSONObject> registerDiner(@RequestBody final Diner diner) {
        return dinerService.register(diner);
    }

    @RequestMapping(value = "register/eatery", method = RequestMethod.POST)
    public ResponseEntity<JSONObject> registerEatery(@RequestBody final Eatery eatery) {
        return eateryService.registerEatery(eatery);
    }
}
