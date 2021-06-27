package com.nuggets.valueeats.service;

import com.nuggets.valueeats.entity.Diner;
import com.nuggets.valueeats.entity.Eatery;
import com.nuggets.valueeats.entity.LoginCredentials;
import com.nuggets.valueeats.entity.User;
import com.nuggets.valueeats.repository.DinerRepository;
import com.nuggets.valueeats.repository.EateryRepository;
import com.nuggets.valueeats.repository.UserRepository;
import com.nuggets.valueeats.utils.AuthenticationUtils;
import com.nuggets.valueeats.utils.ResponseUtils;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import javax.persistence.PersistenceException;

@Service
public class LoginCredentialsService {
    @Autowired
    private UserRepository<User> userRepository;

    @Autowired
    private DinerRepository dinerRepository;

    public ResponseEntity<JSONObject> login(final LoginCredentials user) {
        User userDb;
        try {
            userDb = userRepository.findByEmail(user.getEmail());
        } catch (PersistenceException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ResponseUtils.createResponse(e.toString()));
        }

        if (userDb == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ResponseUtils.createResponse("Failed to login, please try again"));
        }

        // We know the user type by checking whether it exists in the dinerRepository
        // If it does, it's a diner otherwise since we know the email exists in the user repository at this point, we know it is an eatery
        return AuthenticationUtils.loginPasswordCheck(user.getPassword(), 
                                                      String.valueOf(userDb.getId()), userDb.getPassword(), 
                                                      "Welcome back, " + userDb.getEmail(), 
                                                      dinerRepository.existsByEmail(userDb.getEmail()));
    }
}
