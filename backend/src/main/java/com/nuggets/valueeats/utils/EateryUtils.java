package com.nuggets.valueeats.utils;

import java.util.HashMap;

import com.nuggets.valueeats.entity.Eatery;
import com.nuggets.valueeats.repository.voucher.RepeatVoucherRepository;
import com.nuggets.valueeats.repository.voucher.VoucherRepository;

public class EateryUtils {

    public static HashMap<String, Object> createEatery(VoucherRepository voucherRepository, RepeatVoucherRepository repeatVoucherRepository, Eatery e, HashMap<String, Integer> distanceFromDiner) {
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
        
        eatery.put("name", e.getAlias());
        eatery.put("discount", discount);
        if (e.getLazyRating() != null) {
            eatery.put("rating", String.format("%.1f", e.getLazyRating()));
        } else {
            eatery.put("rating", "0.0");
        }
        eatery.put("id", e.getId());
        eatery.put("profilePic", e.getProfilePic());
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
