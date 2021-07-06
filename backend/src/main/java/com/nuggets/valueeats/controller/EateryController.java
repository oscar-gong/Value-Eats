package com.nuggets.valueeats.controller;

import com.nuggets.valueeats.controller.model.VoucherInput;
import com.nuggets.valueeats.service.VoucherService;
import com.nuggets.valueeats.utils.JwtUtils;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Objects;

@CrossOrigin(origins = ControllerConstants.URL)
@RestController
public class EateryController {
    @Autowired
    private VoucherService voucherService;
    
    @Autowired
    private JwtUtils jwtUtils;

    @RequestMapping(value = "eatery/voucher", method = RequestMethod.POST)
    // TODO: Add eatery security check annotation
    public ResponseEntity<JSONObject> eateryCreateVoucher(
            @RequestBody VoucherInput voucher, @RequestHeader(name = "Authorization") String token) {
                System.out.println(voucher);
                System.out.println(token);
        return voucherService.createVoucher(voucher, token);
    }

    // An eatery list its own active vouchers
    // Check if the token is an eatery, then check the eateryId.
    @RequestMapping(value = "eatery/listVouchers", method = RequestMethod.GET)
    public ResponseEntity<JSONObject> EateryListVouchers (@RequestHeader (name="Authorization") String token, 
    @RequestHeader (name="eateryId") Long eateryId) {
        return voucherService.listVouchers(token, eateryId);
    }

    // A diner view the target resturant for all past or current vouchers
    @RequestMapping(value = "diner/listVouchers", method = RequestMethod.GET)
    public ResponseEntity<JSONObject> DinerListVouchers(@RequestHeader (name="Authorization") String token, 
    @RequestHeader (name="eateryId") Long eateryId) {
        return voucherService.listVouchers(token, eateryId);
    }
}
