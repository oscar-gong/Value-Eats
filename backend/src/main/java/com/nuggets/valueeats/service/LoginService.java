package com.nuggets.valueeats.service;

import com.nuggets.valueeats.entity.Diner;
import com.nuggets.valueeats.entity.Eatery;
import com.nuggets.valueeats.entity.User;
import com.nuggets.valueeats.repository.DinerRepository;
import com.nuggets.valueeats.repository.EateryRepository;

import org.apache.catalina.connector.Response;
import org.apache.commons.codec.digest.DigestUtils;
import org.json.simple.JSONObject;
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

    @Autowired
    private ResponseService responseService;

    @Transactional
    public ResponseEntity<JSONObject> login(User user){
        List<Diner> diners;
        List<Eatery> eateries;
        try {
            // Handle login for diners
            diners = dinerRepository.findByEmail(user.getEmail());
            eateries = eateryRepository.findByEmail(user.getPassword());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(responseService.createResponse(e.toString()));
        }
        if (diners.size() == 1){
            Diner diner = diners.get(0);
            // There exists a diner with this email in the database
            if (DigestUtils.sha256Hex(user.getPassword()).equals(diner.getPassword())) {
                // We have logged in
                return ResponseEntity.status(HttpStatus.OK).body(responseService.createResponse("Welcome back, " + user.getEmail()));
            } else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(responseService.createResponse("Invalid password, please try again"));
            }
        } else if (eateries.size() == 1) {
            Eatery eatery = eateries.get(0);
            if (user.getPassword().equals(eatery.getPassword())) {
                // We have logged in
                return ResponseEntity.status(HttpStatus.OK).body(responseService.createResponse("EATERY - " + user.getEmail() + " has been logged in"));
            } else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(responseService.createResponse("Invalid password, please try again"));
            }
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(responseService.createResponse("Email not found, please try again"));
        }
    }
}
