package com.nuggets.valueeats.service;

import com.nuggets.valueeats.entity.Diner;
import com.nuggets.valueeats.entity.Eatery;
import com.nuggets.valueeats.entity.User;
import com.nuggets.valueeats.repository.DinerRepository;
import com.nuggets.valueeats.repository.EateryRepository;
import com.nuggets.valueeats.utils.EncryptionUtils;
import org.apache.commons.codec.digest.DigestUtils;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public final class LoginService {
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
            return loginPasswordCheck(user.getPassword(), String.valueOf(diner.getId()), diner.getPassword(), "Welcome back, " + diner.getEmail());
        }
        if (!eateries.isEmpty()) {
            Eatery eatery = eateries.get(0);
            return loginPasswordCheck(user.getPassword(), String.valueOf(eatery.getId()), eatery.getPassword(), "EATERY - " + eatery.getEmail() + " has been logged in");
        }

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(responseService.createResponse("Failed to login, please try again"));
    }

    private ResponseEntity<JSONObject> loginPasswordCheck(final String loginPassword, final String secret, final String actualPassword, final String successMessage) {
        if (EncryptionUtils.encrypt(loginPassword, secret).equals(actualPassword)) {
            return ResponseEntity.status(HttpStatus.OK).body(responseService.createResponse(successMessage));
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(responseService.createResponse("Invalid password, please try again"));
    }
}
