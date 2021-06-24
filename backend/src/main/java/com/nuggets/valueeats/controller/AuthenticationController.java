package com.nuggets.valueeats.controller;

import org.springframework.web.bind.annotation.RestController;

import com.nuggets.valueeats.entity.Diner;
import com.nuggets.valueeats.service.DinerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class AuthenticationController {
    
    @Autowired
    private DinerService dinerService;

    @RequestMapping(value = "register", method = RequestMethod.POST)
    public String registerDiner(@RequestBody Diner diner){
        return dinerService.registerDiner(diner);
    }

    @RequestMapping(value = "listdiners", method = RequestMethod.GET)
    public List<Diner> listDiner(){
        return dinerService.listDiner();
    }
}
