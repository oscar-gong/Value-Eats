package com.nuggets.valueeats.controller;

import com.nuggets.valueeats.entity.Diner;
import com.nuggets.valueeats.entity.Eatery;
import com.nuggets.valueeats.service.HealthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@CrossOrigin(origins = ControllerConstants.URL)
@RestController
public final class HealthController {
    @Autowired
    private HealthService healthService;

    @RequestMapping(value = "list/diners", method = RequestMethod.GET)
    public List<Diner> listDiner(){
        return healthService.listDiner();
    }

    @RequestMapping(value = "list/eateries", method = RequestMethod.GET)
    public List<Eatery> listEatery(){
        return healthService.listEatery();
    }
}
