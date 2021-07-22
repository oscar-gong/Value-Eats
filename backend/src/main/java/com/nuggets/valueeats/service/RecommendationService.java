package com.nuggets.valueeats.service;

import com.nuggets.valueeats.entity.Eatery;
import com.nuggets.valueeats.repository.EateryRepository;
import com.nuggets.valueeats.repository.ReviewRepository;
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
import java.util.PriorityQueue;
import java.util.stream.Collectors;

@Service
public class RecommendationService {
    @Autowired
    private EateryRepository eateryRepository;
    
    @Autowired
    private VoucherRepository voucherRepository;
    
    @Autowired
    private RepeatVoucherRepository repeatVoucherRepository;
    
    @Autowired
    private ReviewRepository reviewRepository;

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
}
