package com.nuggets.valueeats.service;

import com.nuggets.valueeats.entity.Diner;
import com.nuggets.valueeats.repository.DinerRepository;
import org.apache.commons.codec.digest.DigestUtils;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
public class DinerService {
    @Autowired
    private DinerRepository dinerRepository;
    @Autowired
    private ResponseService responseService;
    @Autowired
    private UserService userService;

    @Transactional
    public ResponseEntity<JSONObject> registerDiner(Diner diner) {
        if (!isValidInput(diner)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(responseService.createResponse("Please fill in all required fields."));
        }

        if (dinerRepository.existsByEmail(diner.getEmail())) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(responseService.createResponse("Email is taken, try another"));
        }

        String result = userService.validInputChecker(diner);
        if (result != null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(responseService.createResponse(result));
        }

        diner.setId(null == dinerRepository.findMaxId() ? 0 : dinerRepository.findMaxId() + 1);
        diner.setPassword(DigestUtils.sha256Hex(diner.getPassword()));
        dinerRepository.save(diner);

        JSONObject data = new JSONObject();
        data.put("token", generateToken());

        return ResponseEntity.status(HttpStatus.OK).body(responseService.createResponse("Welcome to ValueEats, " + diner.getUsername(), data));
    }

    private String generateToken() {
        // TODO: create an actual token which is stored in the database to authenticate user calls
        return "asbndlkfajsbndflkja1289374";
    }

    private boolean isValidInput(final Diner diner) {
        return (diner.getAddress() != null &&
                        diner.getPassword() != null &&
                        diner.getUsername() != null &&
                        diner.getEmail() != null);
    }
}
