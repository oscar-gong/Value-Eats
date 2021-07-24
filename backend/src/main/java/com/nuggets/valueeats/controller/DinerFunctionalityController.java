package com.nuggets.valueeats.controller;

import com.nuggets.valueeats.controller.decorator.token.CheckUserToken;
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
    @CheckUserToken
    public ResponseEntity<JSONObject> createReview(@RequestHeader (name="Authorization") String token, @RequestBody Review review){
        return dinerFunctionalityService.createReview(review, token);
    }

    @RequestMapping(value = "diner/removereview", method = RequestMethod.DELETE)
    @CheckUserToken
    public ResponseEntity<JSONObject> removeReview(@RequestHeader (name="Authorization") String token, @RequestBody Review review){
        return dinerFunctionalityService.removeReview(review, token);
    }

    @RequestMapping(value = "list/eateries", method = RequestMethod.GET)
    @CheckUserToken
    public ResponseEntity<JSONObject> listEateries(@RequestHeader (name="Authorization") String token, @RequestParam(required=false) String sort) {
        return dinerFunctionalityService.listEateries(token, sort);
    }

    @RequestMapping(value = "diner/editreview", method = RequestMethod.POST)
    @CheckUserToken
    public ResponseEntity<JSONObject> editReview(@RequestHeader (name="Authorization") String token, @RequestBody Review review){
        return dinerFunctionalityService.editReview(review, token);
    }

    @RequestMapping(value = "diner/book", method = RequestMethod.POST)
    @CheckUserToken
    public ResponseEntity<JSONObject> bookVoucher(@RequestHeader (name="Authorization") String token, @RequestParam Long id){
        return voucherService.bookVoucher(id, token);
    }

    @RequestMapping(value = "diner/voucher", method = RequestMethod.GET)
    @CheckUserToken
    public ResponseEntity<JSONObject> dinerListVouchers(@RequestHeader (name="Authorization") String token) {
        return voucherService.dinerListVouchers(token);
    }
}
