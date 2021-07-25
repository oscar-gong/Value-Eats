package com.nuggets.valueeats.service;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.DecimalFormat;
import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.PriorityQueue;
import java.util.stream.Collectors;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nuggets.valueeats.entity.Diner;
import com.nuggets.valueeats.entity.Eatery;
import com.nuggets.valueeats.entity.Review;
import com.nuggets.valueeats.repository.BookingRecordRepository;
import com.nuggets.valueeats.repository.DinerRepository;
import com.nuggets.valueeats.repository.EateryRepository;
import com.nuggets.valueeats.repository.ReviewRepository;
import com.nuggets.valueeats.repository.voucher.RepeatVoucherRepository;
import com.nuggets.valueeats.repository.voucher.VoucherRepository;
import com.nuggets.valueeats.utils.DistanceUtils;
import com.nuggets.valueeats.utils.EateryUtils;
import com.nuggets.valueeats.utils.ResponseUtils;
import com.nuggets.valueeats.utils.ReviewUtils;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.ResponseBody;


@Service
public class DinerFunctionalityService {
    @Autowired
    private DinerRepository dinerRepository;

    @Autowired
    private EateryRepository eateryRepository;

    @Autowired
    private ReviewRepository reviewRepository;
    
    @Autowired
    private VoucherRepository voucherRepository;
    
    @Autowired
    private RepeatVoucherRepository repeatVoucherRepository;
    
    @Autowired
    private BookingRecordRepository bookingRepository;

    @Autowired
    private DistanceUtils distanceUtils;

    public ResponseEntity<JSONObject> createReview(Review review, String token) {
        try {

            // Check if token is valid
            if(!dinerRepository.existsByToken(token)){
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(ResponseUtils.createResponse("Invalid token"));
            }
            
            // Check for required inputs
            if(!(token != null && review.getEateryId() != null)){
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ResponseUtils.createResponse("Missing fields"));
            }

            // Check for required inputs
            if(review.getRating() == null){
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ResponseUtils.createResponse("Review must have a rating."));
            }
            
            // Check if eatery id exists
            if(!eateryRepository.existsById(review.getEateryId())){
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ResponseUtils.createResponse("Invalid Eatery ID"));
            }

            // Check if rating is between 0.5 to 5 and is in increments of 0.5
            if(!ReviewUtils.isValidRating(review.getRating())){
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ResponseUtils.createResponse("Rating must be between 0.5 and 5 and in 0.5 increments"));
            }

            // Check if review character length does not exceed 280 characters.
            if(!ReviewUtils.isValidMessage(review.getMessage())){
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ResponseUtils.createResponse("Message must not exceed 280 characters"));
            }

            Long dinerId = dinerRepository.findByToken(token).getId();

            if (bookingRepository.existsByDinerIdAndEateryId(dinerId, review.getEateryId()) == 0) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ResponseUtils.createResponse("You cannot review a restaurant that you have not dined at."));
            }

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

    public ResponseEntity<JSONObject> listEateries(String token, String sort, Double latitude, Double longitude) {
        if(!dinerRepository.existsByToken(token) || token.isEmpty()){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(ResponseUtils.createResponse("Invalid token"));
        }
        Diner diner = dinerRepository.findByToken(token);
        List<Eatery> eateryList = eateryRepository.findAll();
        HashMap<String, Integer> distanceFromDiner = null;
        if ("New".equals(sort)) {
            eateryList = eateryRepository.findAllByOrderByIdDesc();
        } else if ("Rating".equals(sort)) {
            eateryList = eateryRepository.findAllByOrderByLazyRatingDesc();
        } else if ("Distance".equals(sort)) {
            if (latitude == null || longitude == null) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ResponseUtils.createResponse("Location must be provided."));
            }
            if (eateryList.isEmpty()) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ResponseUtils.createResponse("There are no eateries at the moment."));
            }

            List<String> addresses = new ArrayList<>();
            for (Eatery e:eateryList) {
                addresses.add(e.getAddress());
            }
            try {
                String addressesURLString = URLEncoder.encode(String.join("|", addresses), "UTF-8");
                distanceFromDiner = distanceUtils.findDistanceFromDiner(latitude, longitude, addressesURLString, addresses);
                if (distanceFromDiner == null) {
                    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ResponseUtils.createResponse("Unable to retrieve distance data at the moment."));
                }
                final HashMap<String, Integer> distanceFromDinerFinal = distanceFromDiner;
                final PriorityQueue<AbstractMap.SimpleImmutableEntry<Integer, Eatery>> pq = eateryList.stream()
                    .map(a -> new AbstractMap.SimpleImmutableEntry<>(distanceFromDinerFinal.get(a.getAddress()), a))
                    .collect(Collectors.toCollection(() -> new PriorityQueue<>((a, b) -> b.getKey() - a.getKey())));

                List<Eatery> result = new ArrayList<Eatery>();
                while (!pq.isEmpty()) {
                    Eatery newEatery = pq.poll().getValue();
                    result.add(0, newEatery);
                }
                eateryList = result;

            } catch (UnsupportedEncodingException e1) {
                e1.printStackTrace();
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ResponseUtils.createResponse("Address cannot be encoded."));
            } 
        }

        ArrayList<Object> list = new ArrayList<Object>();

        for(Eatery e : eateryList){
            HashMap<String, Object> map = EateryUtils.createEatery(voucherRepository, repeatVoucherRepository, reviewRepository, e, distanceFromDiner);
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

            // Check for required inputs
            if(review.getRating() == null){
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ResponseUtils.createResponse("Review must have a rating."));
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
            
            // Check if new rating is between 0.5 to 5 and is in increments of 0.5
            if(review.getRating() != null){
                if(!ReviewUtils.isValidRating(review.getRating())){
                    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ResponseUtils.createResponse("Rating must be between 0.5 and 5 and in 0.5 increments"));
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


