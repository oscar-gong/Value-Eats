package com.nuggets.valueeats.controller;

import com.nuggets.valueeats.controller.decorator.token.CheckDinerToken;
import com.nuggets.valueeats.service.RecommendationService;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = ControllerConstants.URL)
@RestController
@RequestMapping(path = "/recommendation")
public class RecommendationController {
    @Autowired
    private RecommendationService recommendationService;

    @GetMapping("/eatery/fuzzy_search/{search}")
    @CheckDinerToken
    public ResponseEntity<JSONObject> fuzzySearch(
            @RequestHeader(name = "Authorization") String token, @PathVariable final String search) {
        return recommendationService.fuzzySearch(search);
    }

    @GetMapping("")
    @CheckDinerToken
    public ResponseEntity<JSONObject> recommendation(@RequestHeader(name = "Authorization") String token) {
        return recommendationService.recommendation(token);
    }
}
