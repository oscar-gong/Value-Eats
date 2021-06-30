package com.nuggets.valueeats.service;

import com.nuggets.valueeats.entity.User;

import com.nuggets.valueeats.entity.Review;

import java.util.HashMap;
import java.util.Map;

import javax.persistence.PersistenceException;
import javax.transaction.Transactional;

import com.nuggets.valueeats.entity.Diner;
import com.nuggets.valueeats.entity.Eatery;
import com.nuggets.valueeats.entity.User;
import com.nuggets.valueeats.entity.Review;
import com.nuggets.valueeats.repository.UserRepository;
import com.nuggets.valueeats.repository.DinerRepository;
import com.nuggets.valueeats.repository.EateryRepository;
import com.nuggets.valueeats.repository.ReviewRepository;
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
public class ReviewService {
  @Autowired
  private UserRepository<User> userRepository;
  
  @Autowired
  private DinerRepository dinerRepository;

  @Autowired
  private EateryRepository eateryRepository;

  @Autowired
  private ReviewRepository reviewRepository;

  @Autowired
  private JwtUtils jwtUtils;

  @Transactional
  public ResponseEntity<JSONObject> createReview(Review review) {
      ResponseEntity<JSONObject> result = create(review);
      if (result.getStatusCode().is2xxSuccessful()) {
          reviewRepository.save(review);
      }

      return result;
  }

  @Transactional
  public ResponseEntity<JSONObject> create(Review review) {
    if (!userRepository.existsById(review.getDinerId())) {
      return ResponseEntity.status(HttpStatus.CONFLICT).body(ResponseUtils.createResponse("User does not exist, try another"));
  }
  if (!userRepository.existsById(review.getEateryId())) {
    return ResponseEntity.status(HttpStatus.CONFLICT).body(ResponseUtils.createResponse("Eatery does not exist, try another"));
  }
  if (review.getMessage().isEmpty()) {
    return ResponseEntity.status(HttpStatus.CONFLICT).body(ResponseUtils.createResponse("Review can not be empty, try another"));
  }
  if (review.getRating() < 1 || review.getRating() > 10) {
    return ResponseEntity.status(HttpStatus.CONFLICT).body(ResponseUtils.createResponse("Rating can only be between 1 and 10, try another"));
  }
  review.setId(reviewRepository.findMaxId() == null ? 0 : reviewRepository.findMaxId() + 1);
  return ResponseEntity.status(HttpStatus.OK).body(ResponseUtils.createResponse("Review saved successfully: " + review.getMessage()));
  }
}