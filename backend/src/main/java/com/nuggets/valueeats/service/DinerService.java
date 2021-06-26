package com.nuggets.valueeats.service;

import com.nuggets.valueeats.entity.Diner;
import com.nuggets.valueeats.entity.Token;
import com.nuggets.valueeats.repository.DinerRepository;
import com.nuggets.valueeats.utils.EncryptionUtils;
import com.nuggets.valueeats.utils.JwtUtils;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.HashMap;
import java.util.Map;

@Service
public final class DinerService {
    @Autowired
    private DinerRepository dinerRepository;
    @Autowired
    private ResponseService responseService;
    @Autowired
    private UserService userService;
    @Autowired
    private JwtUtils jwtUtils;

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

        Token token = new Token(jwtUtils.encode(String.valueOf(diner.getId())));
        diner.setId(null == dinerRepository.findMaxId() ? 0 : dinerRepository.findMaxId() + 1);
        diner.setPassword(EncryptionUtils.encrypt(diner.getPassword(), String.valueOf(diner.getId())));
        dinerRepository.save(diner);

        Map<String, String> dataMedium = new HashMap<>();
        dataMedium.put("token", token.getToken());
        JSONObject data = new JSONObject(dataMedium);

        return ResponseEntity.status(HttpStatus.OK).body(responseService.createResponse("Welcome to ValueEats, " + diner.getUsername(), data));
    }

    private boolean isValidInput(final Diner diner) {
        return (diner.getAddress() != null &&
                        diner.getPassword() != null &&
                        diner.getUsername() != null &&
                        diner.getEmail() != null);
    }
}
