package com.nuggets.valueeats.service;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import com.nuggets.valueeats.entity.Diner;
import com.nuggets.valueeats.entity.Eatery;
import com.nuggets.valueeats.entity.Review;
import com.nuggets.valueeats.repository.DinerRepository;
import com.nuggets.valueeats.repository.EateryRepository;
import com.nuggets.valueeats.repository.ReviewRepository;
import com.nuggets.valueeats.utils.ResponseUtils;
import com.nuggets.valueeats.utils.ReviewUtils;

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

    public ResponseEntity<JSONObject> createReview(Review review, String token) {
        try {

            // Check if token is valid
            if(!dinerRepository.existsByToken(token)){
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(ResponseUtils.createResponse("Invalid token"));
            }
            
            // Check for required inputs
            if(!(token != null && review.getEateryId() != null && review.getRating() != null)){
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ResponseUtils.createResponse("Missing fields"));
            }

            // Check if eatery id exists
            if(!eateryRepository.existsById(review.getEateryId())){
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ResponseUtils.createResponse("Invalid Eatery ID"));
            }

            // Check if rating is between 1 to 5 and is in increments of 0.5
            if(!ReviewUtils.isValidRating(review.getRating())){
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ResponseUtils.createResponse("Rating must be between 1 and 5 and in 0.5 increments"));
            }

            // Check if review character length does not exceed 280 characters.
            if(!ReviewUtils.isValidMessage(review.getMessage())){
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ResponseUtils.createResponse("Message must not exceed 280 characters"));
            }

            Long dinerId = dinerRepository.findByToken(token).getId();

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
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ResponseUtils.createResponse(e.toString()));
        }
    }

    public ResponseEntity<JSONObject> removeReview(Review review, String token) {
        try {
            
            // Check if token is valid
            if(!dinerRepository.existsByToken(token)){
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(ResponseUtils.createResponse("Invalid diner token"));
            }

            // Check for required inputs
            if(!(token != null && review.getEateryId() != null && review.getId() != null)){
                    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ResponseUtils.createResponse("Missing fields"));
            }

            // Check if eatery id exists
            if(!eateryRepository.existsById(review.getEateryId())){
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ResponseUtils.createResponse("Invalid Eatery ID"));
            }

            Long dinerId = dinerRepository.findByToken(token).getId();

            // Check if diner has a review and delete it
            if(reviewRepository.existsByDinerIdAndEateryIdAndReviewId(dinerId, review.getEateryId(), review.getId()) == 1){
                reviewRepository.deleteById(review.getId());
                return ResponseEntity.status(HttpStatus.OK).body(ResponseUtils.createResponse("Review was deleted successfully"));
            }else{
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ResponseUtils.createResponse("Review does not exist."));
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ResponseUtils.createResponse(e.toString()));
        }
    }

    public ResponseEntity<JSONObject> listEateries(String token) {
        if(!dinerRepository.existsByToken(token) || token.isEmpty()){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(ResponseUtils.createResponse("Invalid token"));
        }
        Diner diner = dinerRepository.findByToken(token);
        List<Eatery> eateryList = eateryRepository.findAll();

        ArrayList<Object> list = new ArrayList<Object>();

        for(Eatery e : eateryList){
            HashMap<String, Object> map = new HashMap<String, Object>();
            map.put("name", e.getAlias());
            map.put("discount", "50%"); // placeholder
            List<Float> reviews= reviewRepository.listReviewRatingsOfEatery(e.getId());
            Double averageRating = reviews.stream().mapToDouble(i -> i).average().orElse(0);
            DecimalFormat df = new DecimalFormat("#.0"); 
            map.put("rating", df.format(averageRating));
            map.put("id", e.getId());
            map.put("cuisines", e.getCuisines());
            list.add(map);
        }

        Map<String, Object> dataMedium = new HashMap<>();
        dataMedium.put("name", diner.getAlias());
        dataMedium.put("eateryList", list);
        JSONObject data = new JSONObject(dataMedium);

        return ResponseEntity.status(HttpStatus.OK).body(ResponseUtils.createResponse(data));
    }
    
    public ResponseEntity<JSONObject> editReview(Review review, String token) {
        try {
            
            // Check if token is valid
            if(!dinerRepository.existsByToken(token)){
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(ResponseUtils.createResponse("Invalid token"));
            }
            
            // Check for required inputs
            if(!(token != null && review.getEateryId() != null && review.getId() != null)){
                    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ResponseUtils.createResponse("Missing fields"));
            }

            // Check if eatery id exists
            if(!eateryRepository.existsById(review.getEateryId())){
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ResponseUtils.createResponse("Invalid Eatery ID"));
            }


            Long dinerId = dinerRepository.findByToken(token).getId();

            // Check if review exists and is made by the diner for the specific eatery
            if(reviewRepository.existsByDinerIdAndEateryIdAndReviewId(dinerId, review.getEateryId(), review.getId()) == 0){
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ResponseUtils.createResponse("Matching review does not exist"));
            }
            
            Optional<Review> reviewInDb = reviewRepository.findById(review.getId());
            if(!reviewInDb.isPresent()){

            }
            Review reviewDb = reviewInDb.get();

            // Check if new review character length does not exceed 280 characters.
            if(review.getMessage() != ""){
                if(!ReviewUtils.isValidMessage(review.getMessage())){
                    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ResponseUtils.createResponse("Message must not exceed 280 characters"));
                }
                reviewDb.setMessage(review.getMessage());
            }
            
            // Check if new rating is between 1 to 5 and is in increments of 0.5
            if(review.getRating() != null){
                if(!ReviewUtils.isValidRating(review.getRating())){
                    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ResponseUtils.createResponse("Rating must be between 1 and 5 and in 0.5 increments"));
                }
                reviewDb.setRating(review.getRating());
            }

            if(review.getReviewPhotos() != null){
                reviewDb.setReviewPhotos(review.getReviewPhotos());
            }

            reviewRepository.save(reviewDb);

            Map<String, Long> dataMedium = new HashMap<>();
            dataMedium.put("reviewId", review.getId());
            JSONObject data = new JSONObject(dataMedium);
            
            System.out.println(data);

            return ResponseEntity.status(HttpStatus.OK).body(ResponseUtils.createResponse("Review was edited successfully", data));

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ResponseUtils.createResponse(e.toString()));
        }
    }


    
}


