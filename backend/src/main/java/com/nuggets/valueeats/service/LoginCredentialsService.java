package com.nuggets.valueeats.service;

import com.nuggets.valueeats.entity.*;
import com.nuggets.valueeats.repository.TokenRepository;
import com.nuggets.valueeats.repository.UserRepository;
import com.nuggets.valueeats.utils.AuthenticationUtils;
import com.nuggets.valueeats.utils.ResponseUtils;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.persistence.PersistenceException;

@Service
public class LoginCredentialsService {
    @Autowired
    private UserRepository<User> userRepository;
    @Autowired
    private TokenRepository tokenRepository;

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

        return AuthenticationUtils.loginPasswordCheck(user.getPassword(), String.valueOf(userDb.getId()), userDb.getPassword(), "Welcome back, " + userDb.getEmail());
    }

    public ResponseEntity<JSONObject> logout(final String token) {
        if (!tokenRepository.existsByToken(token)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ResponseUtils.createResponse("Invalid authentication"));
        }

        try {
            tokenRepository.deleteByToken(token);
        } catch (PersistenceException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ResponseUtils.createResponse("Cannot log out"));
        }

        return ResponseEntity.status(HttpStatus.OK).body(ResponseUtils.createResponse("Logout was successful"));
    }
}
