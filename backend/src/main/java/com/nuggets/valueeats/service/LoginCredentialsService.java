package com.nuggets.valueeats.service;

import com.nuggets.valueeats.entity.Diner;
import com.nuggets.valueeats.entity.Eatery;
import com.nuggets.valueeats.entity.Token;
import com.nuggets.valueeats.entity.LoginCredentials;
import com.nuggets.valueeats.entity.User;
import com.nuggets.valueeats.repository.DinerRepository;
import com.nuggets.valueeats.repository.EateryRepository;
import com.nuggets.valueeats.repository.LoggedInUserRepository;
import com.nuggets.valueeats.repository.UserTokenRepository;
import com.nuggets.valueeats.repository.UserRepository;
import com.nuggets.valueeats.utils.AuthenticationUtils;
import com.nuggets.valueeats.utils.JwtUtils;
import com.nuggets.valueeats.utils.ResponseUtils;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.HashMap;
import java.util.Map;
import com.nuggets.valueeats.utils.EncryptionUtils;
import com.nuggets.valueeats.utils.JwtUtils;
import com.nuggets.valueeats.utils.ValidationUtils;
import javax.persistence.PersistenceException;
import java.util.Optional;

@Service
public class LoginCredentialsService {
    @Autowired
    private UserRepository<User> userRepository;
    @Autowired
    private UserTokenRepository userTokenRepository;
    @Autowired
    private JwtUtils jwtUtils;
    @Autowired
    private LoggedInUserRepository loggedInUserRepository;

    @Autowired
    private JwtUtils jwtUtils;

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

        Token token = new Token(jwtUtils.encode(String.valueOf(user.getId())));
        Map<String, String> dataMedium = new HashMap<>();
        dataMedium.put("token", token.getToken());
        JSONObject data = new JSONObject(dataMedium);

        return AuthenticationUtils.loginPasswordCheck(user.getPassword(), String.valueOf(userDb.getId()), 
        userDb.getPassword(), "Welcome back, " + userDb.getEmail(), data);
    }

    public ResponseEntity<JSONObject> logout(final Token token) {
        if (!userTokenRepository.existsByToken(token.getToken())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ResponseUtils.createResponse("Invalid authentication"));
        }

        String userId = jwtUtils.decode(token.getToken());
        if (userId == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ResponseUtils.createResponse("Invalid authentication"));
        }

        LoggedInUser loggedInUser = loggedInUserRepository.findById(Long.valueOf(userId)).orElse(null);
        if (loggedInUser == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ResponseUtils.createResponse("Invalid authentication"));
        }

        loggedInUser.removeToken(token.getToken());
        try {
            loggedInUserRepository.save(loggedInUser);
        } catch (PersistenceException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ResponseUtils.createResponse("Cannot log out"));
        }

        return ResponseEntity.status(HttpStatus.OK).body(ResponseUtils.createResponse("Logout was successful"));
    }
}
