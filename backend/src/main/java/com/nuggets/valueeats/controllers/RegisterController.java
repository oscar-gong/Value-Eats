package com.nuggets.valueeats.controllers;

import com.nuggets.valueeats.entity.User.Diner;
import com.nuggets.valueeats.service.database.DatabaseAuthenticationTableService;
import org.springframework.web.bind.annotation.RestController;

import java.sql.SQLException;

@RestController
public class RegisterController {
    public boolean registerDiner(Diner diner) throws SQLException {
        try {
            DatabaseAuthenticationTableService.addEntry(diner.getUsername(), diner.getEmail(), diner.getPassword());
            return true;
        } catch (SQLException e) {
            return false;
        }
    }
}
