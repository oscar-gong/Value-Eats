package com.nuggets.valueeats.service;

import com.nuggets.valueeats.entity.Diner;
import com.nuggets.valueeats.entity.Eatery;
import com.nuggets.valueeats.entity.User;
import com.nuggets.valueeats.repository.DinerRepository;
import com.nuggets.valueeats.repository.EateryRepository;
import org.apache.commons.codec.digest.DigestUtils;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LoginService {
    @Autowired
    private DinerRepository dinerRepository;
    @Autowired
    private EateryRepository eateryRepository;
    @Autowired
    private ResponseService responseService;

    public ResponseEntity<JSONObject> login(final User user) {
        List<Diner> diners;
        List<Eatery> eateries;
        try {
            diners = dinerRepository.findByEmail(user.getEmail());
            eateries = eateryRepository.findByEmail(user.getPassword());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(responseService.createResponse(e.toString()));
        }

        if (!diners.isEmpty()) {
            Diner diner = diners.get(0);
            return loginPasswordCheck(DigestUtils.sha256Hex(user.getPassword()), diner.getPassword(), "Welcome back, " + diner.getEmail());
        }
        if (!eateries.isEmpty()) {
            Eatery eatery = eateries.get(0);
            return loginPasswordCheck(user.getPassword(), eatery.getPassword(), "EATERY - " + user.getEmail() + " has been logged in");
        }

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(responseService.createResponse("Failed to login, please try again"));
    }

    private ResponseEntity<JSONObject> loginPasswordCheck(final String loginPassword, final String actualPassword, final String successMessage) {
        if (loginPassword.equals(actualPassword)) {
            return ResponseEntity.status(HttpStatus.OK).body(responseService.createResponse(successMessage));
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(responseService.createResponse("Invalid password, please try again"));
        }
    }
}
