package com.nuggets.valueeats.controller;

import org.springframework.web.bind.annotation.RestController;

import com.nuggets.valueeats.entity.LoginInfo;
import com.nuggets.valueeats.entity.Diner;
import com.nuggets.valueeats.service.DinerService;
import com.nuggets.valueeats.entity.Eatery;
import com.nuggets.valueeats.service.EateryService;
import com.nuggets.valueeats.service.LoginService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
public class AuthenticationController {
    
    @Autowired
    private DinerService dinerService;

    @Autowired
    private EateryService eateryService;

    @Autowired
    private LoginService loginService;

    @RequestMapping(value = "login", method = RequestMethod.POST)
    public ResponseEntity<String> login(@RequestBody LoginInfo usernamePassword) {
        System.out.println(usernamePassword.getEmail() + " " + usernamePassword.getPassword() + "!!!!!!!");
        return loginService.login(usernamePassword);
    }

    @RequestMapping(value = "register/diner", method = RequestMethod.POST)
    public ResponseEntity<String> registerDiner(@RequestBody Diner diner){
        return dinerService.registerDiner(diner);
    }

    @RequestMapping(value = "register/eatery", method = RequestMethod.POST)
    public String registerEatery(@RequestBody Eatery eatery){
        return eateryService.registerEatery(eatery);
    }

    @RequestMapping(value = "listdiners", method = RequestMethod.GET)
    public List<Diner> listDiner(){
        return dinerService.listDiner();
    }

    @RequestMapping(value = "listeateries", method = RequestMethod.GET)
    public List<Eatery> listEatery(){
        return eateryService.listEatery();
    }
}
