package com.nuggets.valueeats.controller;

import com.nuggets.valueeats.controller.decorator.CheckToken;
import com.nuggets.valueeats.entity.Review;
import com.nuggets.valueeats.service.VoucherService;
import com.nuggets.valueeats.service.DinerFunctionalityService;

import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.CrossOrigin;

@CrossOrigin(origins = ControllerConstants.URL)
@RestController
public class DinerFunctionalityController {

    @Autowired
    private VoucherService voucherService;

    @Autowired
    private DinerFunctionalityService dinerFunctionalityService;

    @RequestMapping(value = "diner/createreview", method = RequestMethod.POST)
    @CheckToken
    public ResponseEntity<JSONObject> createReview(@RequestBody Review review, @RequestHeader (name="Authorization") String token){
        return dinerFunctionalityService.createReview(review, token);
    }

    @RequestMapping(value = "diner/removereview", method = RequestMethod.DELETE)
    @CheckToken
    public ResponseEntity<JSONObject> removeReview(@RequestHeader (name="Authorization") String token, @RequestBody Review review){
        return dinerFunctionalityService.removeReview(review, token);
    }
    
    @RequestMapping(value = "list/eateries", method = RequestMethod.GET)
    @CheckToken
    public ResponseEntity<JSONObject> listEateries(@RequestHeader (name="Authorization") String token) {
        return dinerFunctionalityService.listEateries(token);
    }

    @RequestMapping(value = "diner/editreview", method = RequestMethod.POST)
    @CheckToken
    public ResponseEntity<JSONObject> editReview(@RequestHeader (name="Authorization") String token, @RequestBody Review review){
        return dinerFunctionalityService.editReview(review, token);
    }

    @RequestMapping(value = "diner/book", method = RequestMethod.POST)
    @CheckToken
    public ResponseEntity<JSONObject> bookVoucher(@RequestHeader (name="Authorization") String token, @RequestParam Long id){
        return voucherService.bookVoucher(id, token);
    }

    @RequestMapping(value = "diner/voucher", method = RequestMethod.GET)
    @CheckToken
    public ResponseEntity<JSONObject> dinerListVouchers(@RequestHeader (name="Authorization") String token) {
        return voucherService.dinerListVouchers(token);
    }
}
