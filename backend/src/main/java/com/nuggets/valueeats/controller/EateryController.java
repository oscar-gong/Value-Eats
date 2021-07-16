package com.nuggets.valueeats.controller;

import com.nuggets.valueeats.controller.decorator.CheckToken;
import com.nuggets.valueeats.controller.model.VoucherInput;
import com.nuggets.valueeats.service.VoucherService;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@CrossOrigin(origins = ControllerConstants.URL)
@RestController
public class EateryController {
    @Autowired
    private VoucherService voucherService;

    @RequestMapping(value = "eatery/voucher", method = RequestMethod.POST)
    @CheckToken
    public ResponseEntity<JSONObject> eateryCreateVoucher(@RequestHeader(name = "Authorization") String token, @RequestBody VoucherInput voucher) {
        return voucherService.createVoucher(voucher, token);
    }

    @RequestMapping(value = "eatery/voucher", method = RequestMethod.PUT)
    @CheckToken
    public ResponseEntity<JSONObject> DinerListVouchers(@RequestHeader (name="Authorization") String token, @RequestBody VoucherInput voucher) {
        return voucherService.editVoucher(voucher, token);
    }

    @RequestMapping(value = "eatery/voucher", method = RequestMethod.DELETE)
    @CheckToken
    public ResponseEntity<JSONObject> eateryDeleteVoucher(@RequestHeader (name="Authorization") String token, @RequestParam Long id) {
        return voucherService.deleteVoucher(id, token);
    }

    @RequestMapping(value = "eatery/verify/voucher", method = RequestMethod.POST)
    @CheckToken
    public ResponseEntity<JSONObject> eateryVerifyVoucher(@RequestHeader (name="Authorization") String token, @RequestParam String code) {
        return voucherService.verifyVoucher(code, token);
    }
}

/*
{
  "eatingStyle":"DineIn",
  "discount":"0.8",
  "quantity":"1",
  "isRecurring":"false",
  "day":"1",
  "startMinute":"10",
  "endMinute":"20"
}
*/