package com.nuggets.valueeats.service;

import com.nuggets.valueeats.entity.User;

import java.util.HashMap;
import java.util.Map;

import javax.persistence.PersistenceException;
import javax.transaction.Transactional;

import com.nuggets.valueeats.entity.Diner;
import com.nuggets.valueeats.entity.Eatery;
import com.nuggets.valueeats.repository.UserRepository;
import com.nuggets.valueeats.repository.DinerRepository;
import com.nuggets.valueeats.repository.EateryRepository;
import com.nuggets.valueeats.utils.AuthenticationUtils;
import com.nuggets.valueeats.utils.EncryptionUtils;
import com.nuggets.valueeats.utils.JwtUtils;
import com.nuggets.valueeats.utils.ResponseUtils;
import com.nuggets.valueeats.utils.ValidationUtils;
import org.apache.commons.lang3.StringUtils;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;


@Service
public class UserManagementService {
    @Autowired
    private UserRepository<User> userRepository;
    
    @Autowired
    private DinerRepository dinerRepository;

    @Autowired
    private EateryRepository eateryRepository;

    @Autowired
    private JwtUtils jwtUtils;

    @Transactional
    public ResponseEntity<JSONObject> registerEatery(Eatery eatery) {
        ResponseEntity<JSONObject> result = register(eatery);
        if (result.getStatusCode().is2xxSuccessful()) {
            eateryRepository.save(eatery);
        }

        return result;
    }

    @Transactional
    public ResponseEntity<JSONObject> registerDiner(Diner diner) {
        ResponseEntity<JSONObject> result = register(diner);
        if (result.getStatusCode().is2xxSuccessful()) {
            dinerRepository.save(diner);
        }

        return result;
    }

    @Transactional
    public ResponseEntity<JSONObject> register(User user) {
        if (!isValidInput(user)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ResponseUtils.createResponse("Please fill in all required fields."));
        }

        if (userRepository.existsByEmail(user.getEmail())) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(ResponseUtils.createResponse("Email is taken, try another"));
        }

        String result = validInputChecker(user);

        if (result != null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ResponseUtils.createResponse(result));
        }

        user.setId(userRepository.findMaxId() == null ? 0 : userRepository.findMaxId() + 1);

        user.setPassword(EncryptionUtils.encrypt(user.getPassword(), String.valueOf(user.getId())));

        String userToken = jwtUtils.encode(String.valueOf(user.getId()));

        user.setToken(userToken);
        
        Map<String, String> dataMedium = new HashMap<>();
        dataMedium.put("token", userToken);
        JSONObject data = new JSONObject(dataMedium);
        
        System.out.println(data);

        return ResponseEntity.status(HttpStatus.OK).body(ResponseUtils.createResponse("Welcome to ValueEats, " + user.getAlias(), data));
    }

    @Transactional
    public ResponseEntity<JSONObject> login(User user){
        User userDb;
        try {
            userDb = userRepository.findByEmail(user.getEmail());
        } catch (PersistenceException e) {
            System.out.println("error");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ResponseUtils.createResponse(e.toString()));
        }

        if (userDb == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ResponseUtils.createResponse("Failed to login, please try again"));
        }

        String token = jwtUtils.encode(String.valueOf(user.getId()));
        userDb.setToken(token);
        System.out.println(userDb.getToken());
        userRepository.save(userDb);

        Map<String, String> dataMedium = new HashMap<>();
        dataMedium.put("token", token);
        JSONObject data = new JSONObject(dataMedium);
        
        System.out.println(data);

        return AuthenticationUtils.loginPasswordCheck(user.getPassword(), String.valueOf(userDb.getId()), 
                                                      userDb.getPassword(), "Welcome back, " + userDb.getEmail(), 
                                                      dinerRepository.existsByEmail(userDb.getEmail()), data);
    }

    @Transactional
    public ResponseEntity<JSONObject> logout(User userToken) {
        if (!userRepository.existsByToken(userToken.getToken())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ResponseUtils.createResponse("Can't find the token: " + userToken.getToken()));
        }

        String userId = jwtUtils.decode(userToken.getToken());
        if (userId == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ResponseUtils.createResponse("Can't get user associated with token"));
        }


        User user = userRepository.findByToken(userToken.getToken());
        user.setToken("");
        userRepository.save(user);
    

        return ResponseEntity.status(HttpStatus.OK).body(ResponseUtils.createResponse("Logout was successful"));
    }

    @Transactional
    public ResponseEntity<JSONObject> updateDiner(Diner diner) {
        ResponseEntity<JSONObject> result = update(diner);
        if (result.getStatusCode().is2xxSuccessful()) {
            dinerRepository.save(diner);
        }

        return result;
    }

    @Transactional
    public ResponseEntity<JSONObject> updateEatery(Eatery eatery) {
        ResponseEntity<JSONObject> result = update(eatery);
        if (result.getStatusCode().is2xxSuccessful()) {
            eateryRepository.save(eatery);
        }

        return result;
    }

    @Transactional
    public ResponseEntity<JSONObject> update(User user){
        User userDb;
        try {
            userDb = userRepository.findByToken(user.getToken());
        } catch (PersistenceException e) {
            System.out.println("error");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ResponseUtils.createResponse(e.toString()));
        }

        if (userDb == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ResponseUtils.createResponse("Failed to verify, please try again"));
        }

        if (!isValidInput(user)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ResponseUtils.createResponse("Please fill in all required fields."));
        }

        if (userRepository.existsByEmail(user.getEmail())) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(ResponseUtils.createResponse("Email is taken, try another"));
        }

        String result = validInputChecker(user);

        if (result != null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ResponseUtils.createResponse(result));
        }

        userRepository.updateUserById(user.getEmail(), user.getPassword(), user.getAlias(), user.getAddress(), user.getId());

        user.setId(userDb.getId());
        return ResponseEntity.status(HttpStatus.OK).body(ResponseUtils.createResponse("Update profile successfully, " + user.getAlias()));
    }

    private boolean isValidInput(User user) {
        return StringUtils.isNotBlank(String.valueOf(user.getId())) &&
                StringUtils.isNotBlank(user.getAddress()) &&
                StringUtils.isNotBlank(user.getAlias());
    }

    public String validInputChecker(final User user) {
        if (!ValidationUtils.isValidEmail(user.getEmail())) {
            return "Invalid Email Format.";
        }
        if (!ValidationUtils.isValidPassword(user.getPassword())) {
            return "Password must be between 8 to 32 characters long, and contain a lower and uppercase character.";
        }
        return null;
    }
}
