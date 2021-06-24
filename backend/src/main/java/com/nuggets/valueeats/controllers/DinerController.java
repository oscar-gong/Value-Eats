package com.nuggets.valueeats.controllers;

import java.util.*;

import com.nuggets.valueeats.entity.Diner;
import com.nuggets.valueeats.service.DinerService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping(path = "diner")
public class DinerController {
    private final DinerService dinerService;

    @Autowired
    public DinerController (DinerService dinerService) {
        this.dinerService = dinerService;
    }

    @GetMapping
    public List<Diner> getAllDiners () {
        return this.dinerService.findAllDiners();
    }

    @PostMapping
    public void registerDiner (Diner diner) {
        dinerService.registerDiner(diner);
    }
}
