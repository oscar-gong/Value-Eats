package com.nuggets.valueeats.utils;

import java.util.ArrayList;
import java.util.HashMap;

public class ReviewUtils {
    public static boolean isValidRating(Float rating) {
        return rating % 0.5 == 0 && rating >= 0.5 && rating <= 5;
    }

    public static boolean isValidMessage(String message) {
        return message.length() <= 280;
    }

    public static HashMap<String, Object> createReview(Long id, String pic, String name, String message, float rating, Long eateryId, ArrayList<String> reviewPhotos, String eateryName) {
        HashMap<String, Object> review = new HashMap<String, Object>();
        review.put("reviewId", id);
        review.put("profilePic", pic);
        review.put("name", name);
        review.put("rating", rating);
        review.put("message", message);
        review.put("eateryId", eateryId);
        review.put("reviewPhotos", reviewPhotos);
        review.put("eateryName", eateryName);
        return review;
    }
}
