package com.nuggets.valueeats.service;

import com.nuggets.valueeats.entity.Eatery;
import com.nuggets.valueeats.repository.EateryRepository;
import me.xdrop.fuzzywuzzy.FuzzySearch;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.List;
import java.util.PriorityQueue;
import java.util.stream.Collectors;

@Service
public class RecommendationService {
    @Autowired
    private EateryRepository eateryRepository;

    public ResponseEntity<List<Eatery>> fuzzySearch(final String search) {
        final PriorityQueue<AbstractMap.SimpleImmutableEntry<Integer, Eatery>> pq = eateryRepository.findAll().stream()
                .map(a -> new AbstractMap.SimpleImmutableEntry<>(FuzzySearch.weightedRatio(search, a.getCuisines().toString() + "|" + a.getAlias() + "|" + a.getAddress()), a))
                .collect(Collectors.toCollection(() -> new PriorityQueue<>((a, b) -> b.getKey() - a.getKey())));

        List<Eatery> result = new ArrayList<>(10);
        while (!pq.isEmpty() && result.size() <= 10) {
            result.add(pq.poll().getValue());
        }

        return ResponseEntity.status(HttpStatus.OK).body(result);
    }
}
