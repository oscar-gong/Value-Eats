package com.nuggets.valueeats.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.nuggets.valueeats.entity.Diner;
import com.nuggets.valueeats.entity.Eatery;
import com.nuggets.valueeats.entity.Review;
import com.nuggets.valueeats.repository.DinerRepository;
import com.nuggets.valueeats.repository.EateryRepository;
import com.nuggets.valueeats.repository.ReviewRepository;
import com.nuggets.valueeats.utils.ResponseUtils;
import org.apache.commons.lang3.StringUtils;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;


@Service
public class DinerFunctionalityService {
    @Autowired
    private DinerRepository dinerRepository;

    @Autowired
    private EateryRepository eateryRepository;

    @Autowired
    private ReviewRepository reviewRepository;

    public ResponseEntity<JSONObject> createReview(String jsonString) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
            Review review = objectMapper.readValue(jsonString, Review.class);
            Diner diner = objectMapper.readValue(jsonString, Diner.class);

            // Check for required inputs
            if(!(StringUtils.isNotBlank(String.valueOf(diner.getToken())) &&
                StringUtils.isNotBlank(String.valueOf(review.getEateryId())) &&
                StringUtils.isNotBlank(String.valueOf(review.getRating())))
                ){
                    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ResponseUtils.createResponse("Missing fields"));
            }

            // Check if eatery id exists
            if(!eateryRepository.existsById(review.getEateryId())){
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ResponseUtils.createResponse("Invalid Eatery ID"));
            }

            // Check if token is valid
            if(!dinerRepository.existsByToken(diner.getToken())){
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ResponseUtils.createResponse("Invalid token"));
            }

            // Check if rating is between 1 to 5 and is in increments of 0.5
            if(!isValidRating(review.getRating())){
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ResponseUtils.createResponse("Rating must be between 1 and 5 and in 0.5 increments"));
            }

            // Check if review character length does not exceed 280 characters.
            if(!isValidMessage(review.getMessage())){
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ResponseUtils.createResponse("Message must not exceed 280 characters"));
            }

            Long dinerId = dinerRepository.findByToken(diner.getToken()).getId();

            // Check if user already made a review
            if(reviewRepository.existsByDinerIdAndEateryId(dinerId, review.getEateryId()) == 1){
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ResponseUtils.createResponse("User already has an existing review for this eatery."));
            }

            review.setDinerId(dinerId);
            review.setId(reviewRepository.findMaxId() == null ? 0 : reviewRepository.findMaxId() + 1);

            reviewRepository.save(review);

            Map<String, Long> dataMedium = new HashMap<>();
            dataMedium.put("reviewId", review.getId());
            JSONObject data = new JSONObject(dataMedium);
            
            System.out.println(data);

            return ResponseEntity.status(HttpStatus.OK).body(ResponseUtils.createResponse("Review was created successfully", data));

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ResponseUtils.createResponse(e.toString()));
        }
    }

    public ResponseEntity<JSONObject> removeReview(String jsonString) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
            Review review = objectMapper.readValue(jsonString, Review.class);
            Diner diner = objectMapper.readValue(jsonString, Diner.class);

            // Check for required inputs
            if(!(StringUtils.isNotBlank(String.valueOf(diner.getToken())) &&
                StringUtils.isNotBlank(String.valueOf(review.getEateryId())) &&
                StringUtils.isNotBlank(String.valueOf(review.getId())))
                ){
                    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ResponseUtils.createResponse("Missing fields"));
            }

            // Check if eatery id exists
            if(!eateryRepository.existsById(review.getEateryId())){
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ResponseUtils.createResponse("Invalid Eatery ID"));
            }

            // Check if token is valid
            if(!dinerRepository.existsByToken(diner.getToken())){
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ResponseUtils.createResponse("Invalid token"));
            }


            Long dinerId = dinerRepository.findByToken(diner.getToken()).getId();

            // Check if diner has a review and delete it
            if(reviewRepository.existsByDinerIdAndEateryIdAndReviewId(dinerId, review.getEateryId(), review.getId()) == 1){
                reviewRepository.deleteById(review.getId());
                return ResponseEntity.status(HttpStatus.OK).body(ResponseUtils.createResponse("Review was deleted successfully"));
            }else{
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ResponseUtils.createResponse("Review does not exist."));
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ResponseUtils.createResponse(e.toString()));
        }
    }

    public ResponseEntity<JSONObject> listEateries() {
        List<Eatery> eateryList = eateryRepository.findAll();
        // need name, discount, rating

        ArrayList<Object> list = new ArrayList<Object>();

        for(Eatery e : eateryList){
            HashMap<String, Object> map = new HashMap<String, Object>();
            map.put("name", e.getAlias());
            map.put("discount", "50%"); // placeholder
            List<Float> reviews= reviewRepository.listReviewsOfEatery(e.getId());
            Double averageRating = reviews.stream().mapToDouble(i -> i).average().orElse(0);
            map.put("rating", averageRating);
            list.add(map);
        }

        Map<String, ArrayList<Object>> dataMedium = new HashMap<>();
        dataMedium.put("eateryList", list);
        JSONObject data = new JSONObject(dataMedium);

        return ResponseEntity.status(HttpStatus.OK).body(ResponseUtils.createResponse(data));
    }
    

    private static boolean isValidRating(Float rating){
        return rating % 0.5 == 0 && rating >= 1 && rating <= 5;
    }

    private static boolean isValidMessage(String message){
        return message.length() <= 280;
    }
    
}


