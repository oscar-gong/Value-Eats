package com.nuggets.valueeats.controllers;

import com.nuggets.valueeats.entities.LoginInfo;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

// @CrossOrigin(origins = "http://localhost", maxAge = 3600)
@CrossOrigin(origins = "http://localhost:3000")
@RestController
public class LoginController {
    
    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public ResponseEntity<String> postController(@RequestBody LoginInfo usernamePassword) {
        System.out.println(usernamePassword.getUsername() + " " + usernamePassword.getPassword() + "!!!!!!!");
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
}
