package com.nuggets.valueeats.service;

import com.nuggets.valueeats.entity.Diner;
import com.nuggets.valueeats.repository.DinerRepository;
import com.nuggets.valueeats.repository.EateryRepository;
import com.nuggets.valueeats.repository.UserRepository;
import com.nuggets.valueeats.utils.AuthenticationUtils;
import com.nuggets.valueeats.utils.JwtUtils;
import com.nuggets.valueeats.utils.ResponseUtils;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.persistence.PersistenceException;
import javax.transaction.Transactional;
import java.util.List;

@Service
public class DinerService extends UserService {
    @Autowired
    private DinerRepository dinerRepository;
    @Autowired
    private EateryRepository eateryRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private UserService userService;
    @Autowired
    private JwtUtils jwtUtils;

    @Transactional
    public ResponseEntity<JSONObject> register(Diner diner) {
        if (!isValidDiner(diner)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ResponseUtils.createResponse("Please fill in all required fields."));
        }

        ResponseEntity<JSONObject> result = super.register(diner);
        if (result.getStatusCode().is2xxSuccessful()) {
            dinerRepository.save(diner);
        }

        return result;
    }

    private boolean isValidDiner(final Diner diner) {
        // No variables in diner which need to be checked, they are checked at user level
        return true;
    }
}
