package com.nuggets.valueeats.service;

import com.nuggets.valueeats.entity.Eatery;
import com.nuggets.valueeats.entity.Token;
import com.nuggets.valueeats.repository.EateryRepository;
import com.nuggets.valueeats.utils.EncryptionUtils;
import com.nuggets.valueeats.utils.JwtUtils;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.HashMap;
import java.util.Map;

@Service
public class EateryService {
    @Autowired
    private EateryRepository eateryRepository;
    @Autowired
    private UserService userService;
    @Autowired
    private JwtUtils jwtUtils;

    @Transactional
    public String registerEatery(Eatery eatery) {
        if (!isValidInput(eatery)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(responseService.createResponse("Please fill in all required fields."));
        }

        // want the email to be unique across diners and eateries
        if (dinerRepository.existsByEmail(diner.getEmail()) || eateryRepository.existsByEmail(diner.getEmail())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(responseService.createResponse("This Eatery already exists, please try again"));
        }

        String isValidResult = userService.validInputChecker(eatery);
        if (isValidResult != null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(responseService.createResponse(isValidResult));
        }

        Token token = new Token(jwtUtils.encode(String.valueOf(eatery.getId())));
        eatery.setId(null == eateryRepository.findMaxId() ? 0 : eateryRepository.findMaxId() + 1);
        eatery.setPassword(EncryptionUtils.encrypt(eatery.getPassword(), String.valueOf(eatery.getId())));
        eateryRepository.save(eatery);

        Map<String, String> dataMedium = new HashMap<>();
        dataMedium.put("token", token.getToken());
        JSONObject data = new JSONObject(dataMedium);

        return ResponseEntity.status(HttpStatus.OK).body(responseService.createResponse("Eatery Registered! Welcome to ValueEats, " + eatery.getName(), data));
    }

    private boolean isValidInput(final Eatery eatery) {
        return (eatery.getAddress() != null &&
                        eatery.getPassword() != null &&
                        eatery.getName() != null &&
                        eatery.getEmail() != null);
    }
}
