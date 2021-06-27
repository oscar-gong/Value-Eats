// package com.nuggets.valueeats.service;

// import com.nuggets.valueeats.entity.Eatery;
// import com.nuggets.valueeats.repository.EateryRepository;
// import com.nuggets.valueeats.utils.*;
// import org.json.simple.JSONObject;
// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.stereotype.Service;

// import javax.transaction.Transactional;

// import com.nuggets.valueeats.repository.DinerRepository;
// import com.nuggets.valueeats.repository.UserRepository;
// import com.nuggets.valueeats.utils.JwtUtils;
// import org.springframework.http.HttpStatus;
// import org.springframework.http.ResponseEntity;

// @Service
// public class EateryService extends LoggedInUserService {
//     @Autowired
//     private EateryRepository eateryRepository;
//     @Autowired
//     private LoggedInUserService loggedInUserService;
//     @Autowired
//     private DinerRepository dinerRepository;
//     @Autowired
//     private JwtUtils jwtUtils;
//     @Autowired
//     private UserRepository userRepository;

//     @Transactional
//     public ResponseEntity<JSONObject> registerEatery(Eatery eatery) {
//         if (!isValidInput(eatery)) {
//             return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ResponseUtils.createResponse("Please fill in all required fields."));
//         }

//         ResponseEntity<JSONObject> result = super.register(eatery);
//         if (result.getStatusCode().is2xxSuccessful()) {
//             eateryRepository.save(eatery);
//         }

//         return result;
//     }

//     private boolean isValidInput(final Eatery eatery) {
//         // We do not need to check if an eatery has images, further checks at user
//         return true;
//     }
// }
