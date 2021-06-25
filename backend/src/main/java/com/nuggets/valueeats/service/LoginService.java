package com.nuggets.valueeats.service;

import com.nuggets.valueeats.entity.Diner;
import com.nuggets.valueeats.entity.Eatery;
import com.nuggets.valueeats.entity.LoginInfo;
import com.nuggets.valueeats.repository.DinerRepository;
import com.nuggets.valueeats.repository.EateryRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
// import java.util.regex.*;

@Service
public class LoginService {
    
    @Autowired
    private DinerRepository dinerRepository;

    @Autowired
    private EateryRepository eateryRepository;

    @Transactional
    public ResponseEntity<String> login(LoginInfo usernamePassword){
        try {
            // Handle login for diners
            List<Diner> diners = dinerRepository.findByEmail(usernamePassword.getEmail());
            List<Eatery> eateries = eateryRepository.findByEmail(usernamePassword.getPassword());
            if (diners.size() == 1){
                Diner diner = diners.get(0);
                // There exists a diner with this email in the database
                if (usernamePassword.getPassword().equals(diner.getPassword())) {
                    // We have logged in
                    return ResponseEntity.status(HttpStatus.OK).body("Log in success");
                } else {
                    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid password");
                }
            } else if (eateries.size() == 1) {
                Eatery eatery = eateries.get(0);
                if (usernamePassword.getPassword().equals(eatery.getPassword())) {
                    // We have logged in
                    return ResponseEntity.status(HttpStatus.OK).body("EATERY - " + usernamePassword.getEmail() + " has been logged in");
                } else {
                    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid password");
                }
            } else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Username invalid");
            }
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.toString());
        }
    }
}
