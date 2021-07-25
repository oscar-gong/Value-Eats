package com.nuggets.valueeats.utils;

import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.List;

import com.nuggets.valueeats.entity.Eatery;
import com.nuggets.valueeats.repository.ReviewRepository;
import com.nuggets.valueeats.repository.voucher.RepeatVoucherRepository;
import com.nuggets.valueeats.repository.voucher.VoucherRepository;

public class EateryUtils {

    public static HashMap<String, Object> createEatery(VoucherRepository voucherRepository, RepeatVoucherRepository repeatVoucherRepository, ReviewRepository reviewRepository, Eatery e, HashMap<String, Integer> distanceFromDiner) {
        HashMap<String, Object> eatery = new HashMap<String, Object>();

        Long maxDiscountVoucher = voucherRepository.findMaxDiscountFromEatery(e.getId());
        Long maxDiscountRepeatVoucher = repeatVoucherRepository.findMaxDiscountFromEatery(e.getId());
        Long maxDiscount = (long) 0;

        if (maxDiscountVoucher != null && maxDiscountRepeatVoucher != null) {
            maxDiscount = Math.max(maxDiscountVoucher, maxDiscountRepeatVoucher);
        } else if (maxDiscountVoucher != null) {
            maxDiscount = maxDiscountVoucher;
        } else if (maxDiscountRepeatVoucher != null) {
            maxDiscount = maxDiscountRepeatVoucher;
        }

        String discount = maxDiscount.toString() + "%";
        List<Float> reviews= reviewRepository.listReviewRatingsOfEatery(e.getId());
        Double averageRating = reviews.stream().mapToDouble(i -> i).average().orElse(0);
        DecimalFormat df = new DecimalFormat("#.0"); 
        
        eatery.put("name", e.getAlias());
        eatery.put("discount", discount);
        eatery.put("rating", df.format(averageRating));
        eatery.put("id", e.getId());
        eatery.put("cuisines", e.getCuisines());
        if (distanceFromDiner != null && distanceFromDiner.containsKey(e.getAddress())) {
            if (distanceFromDiner.get(e.getAddress()) != Integer.MAX_VALUE) {
                eatery.put("distance", DistanceUtils.convertDistanceToString(distanceFromDiner.get(e.getAddress())));
            } else {
                eatery.put("distance", "N/A");
            }
        }

        return eatery;
    }
    
}
