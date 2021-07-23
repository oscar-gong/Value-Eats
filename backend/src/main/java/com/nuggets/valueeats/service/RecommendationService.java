package com.nuggets.valueeats.service;

import com.nuggets.valueeats.entity.Diner;
import com.nuggets.valueeats.entity.Eatery;
import com.nuggets.valueeats.entity.User;
import com.nuggets.valueeats.repository.BookingRecordRepository;
import com.nuggets.valueeats.repository.DinerRepository;
import com.nuggets.valueeats.repository.EateryRepository;
import com.nuggets.valueeats.repository.ReviewRepository;
import com.nuggets.valueeats.repository.UserRepository;
import com.nuggets.valueeats.repository.voucher.RepeatVoucherRepository;
import com.nuggets.valueeats.repository.voucher.VoucherRepository;
import com.nuggets.valueeats.utils.EateryUtils;
import com.nuggets.valueeats.utils.ResponseUtils;

import me.xdrop.fuzzywuzzy.FuzzySearch;

import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.PriorityQueue;
import java.util.stream.Collectors;

@Service
public class RecommendationService {
    @Autowired
    private EateryRepository eateryRepository;

    @Autowired
    private DinerRepository dinerRepository;
    
    @Autowired
    private VoucherRepository voucherRepository;
    
    @Autowired
    private RepeatVoucherRepository repeatVoucherRepository;
    
    @Autowired
    private ReviewRepository reviewRepository;

    @Autowired
    private BookingRecordRepository bookingRecordRepository;

    public ResponseEntity<JSONObject> fuzzySearch(final String search) {
        final PriorityQueue<AbstractMap.SimpleImmutableEntry<Integer, Eatery>> pq = eateryRepository.findAll().stream()
                .map(a -> new AbstractMap.SimpleImmutableEntry<>(FuzzySearch.weightedRatio(search, a.getCuisines().toString() + "|" + a.getAlias() + "|" + a.getAddress()), a))
                .collect(Collectors.toCollection(() -> new PriorityQueue<>((a, b) -> b.getKey() - a.getKey())));

        List<Object> result = new ArrayList<Object>();
        while (!pq.isEmpty() && result.size() <= 10) {
            Eatery newEatery = pq.poll().getValue();
            HashMap<String, Object> eatery = EateryUtils.createEatery(voucherRepository, repeatVoucherRepository, reviewRepository, newEatery);
            result.add(eatery);
        }

        Map<String, Object> dataMedium = new HashMap<>();
        dataMedium.put("eateryList", result);
        JSONObject data = new JSONObject(dataMedium);

        return ResponseEntity.status(HttpStatus.OK).body(ResponseUtils.createResponse(data));
    }

    
    public ResponseEntity<JSONObject> recommendation(String token) {
        // - Eateries that are currently offering discount vouchers:
	    //     - Diner has not previously booked for
	    //     - Diner might be interested in using:
        //         - past bookings (using cuisines)
        //         - review ratings for past bookings
        //         - review ratings left by other diners


        if (!dinerRepository.existsByToken(token)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(ResponseUtils.createResponse("Invalid diner token"));
        }
        
        Diner diner = dinerRepository.findByToken(token);

        List<Long> eateriesDinerBeenTo = bookingRecordRepository.findEateriesDinerBeenTo(diner.getId());;


        // Sort these eateries based on diner interests.
        List<Eatery> eateriesDinerNotBeenTo = eateryRepository.findAllEateriesNotInList(eateriesDinerBeenTo);

        final PriorityQueue<AbstractMap.SimpleImmutableEntry<Integer, Eatery>> pq = eateriesDinerNotBeenTo.stream()
                .map(a -> new AbstractMap.SimpleImmutableEntry<>(getWeight(diner, a, eateriesDinerBeenTo), a))
                .collect(Collectors.toCollection(() -> new PriorityQueue<>((a, b) -> b.getKey() - a.getKey())));

        List<Object> result = new ArrayList<Object>();
        while (!pq.isEmpty() && result.size() <= 10) {
            Eatery newEatery = pq.poll().getValue();
            HashMap<String, Object> eatery = EateryUtils.createEatery(voucherRepository, repeatVoucherRepository, reviewRepository, newEatery);
            result.add(eatery);
        }


        Map<String, Object> dataMedium = new HashMap<>();
        dataMedium.put("eateryList", result);
        JSONObject data = new JSONObject(dataMedium);

        return ResponseEntity.status(HttpStatus.OK).body(ResponseUtils.createResponse(data));
    }

    private Integer getWeight(Diner diner, Eatery eatery, List<Long> eateriesDinerBeenTo) {
        int weight = 0;

        // If the restaurant serves a cuisine the diner had before, +1 per cuisine
        weight += eateryRepository.dinerHadCuisineBefore(eatery.getId(), eateriesDinerBeenTo);

        // If the diner had a poor experience (<3) with a certain cuisine at a previous restaurant, -1 per cuisine
        List<Long> eateriesDinerDidNotEnjoy = reviewRepository.listEateriesDinerDidNotEnjoy(diner.getId());
        // Count how many similar cuisines this eatery serves compared to past eatery that diner did not enjoy.
        weight -= eateryRepository.dinerHadCuisineBefore(eatery.getId(), eateriesDinerDidNotEnjoy);

        // If the restaurant has good rating (use avg rating - 3), add some weight, else subtract
        // 5* = 2
        // 4* = 1
        // 3* = 0
        // 2* = -1
        // 1* = -2
        if (eatery.getLazyRating() != null) weight += eatery.getLazyRating() - 3;

        return weight;
    }

}
