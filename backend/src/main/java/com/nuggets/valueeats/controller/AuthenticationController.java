package com.nuggets.valueeats.controller;

import com.nuggets.valueeats.entity.Diner;
import com.nuggets.valueeats.entity.Eatery;
import com.nuggets.valueeats.entity.LoginCredentials;
import com.nuggets.valueeats.service.DinerService;
import com.nuggets.valueeats.service.EateryService;
import com.nuggets.valueeats.service.LoginService;
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
    private LoginService loginService;

    @RequestMapping(value = "login", method = RequestMethod.POST)
    public ResponseEntity<JSONObject> login(@RequestBody final LoginCredentials user) {
        return loginService.login(user);
    }

    @RequestMapping(value = "register/diner", method = RequestMethod.POST)
    public ResponseEntity<JSONObject> registerDiner(@RequestBody final Diner diner) {
        return dinerService.registerDiner(diner);
    }

    @RequestMapping(value = "register/eatery", method = RequestMethod.POST)
    public String registerEatery(@RequestBody final Eatery eatery) {
        return eateryService.registerEatery(eatery);
    }
}
