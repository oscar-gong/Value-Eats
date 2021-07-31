package com.nuggets.valueeats.controller;

import com.nuggets.valueeats.controller.decorator.token.CheckUserToken;
import com.nuggets.valueeats.controller.model.VoucherInput;
import com.nuggets.valueeats.service.EateryService;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = ControllerConstants.URL)
@RestController
public class EateryController {
    @Autowired
    private EateryService eateryService;

    @RequestMapping(value = "eatery/voucher", method = RequestMethod.POST)
    @CheckUserToken
    public ResponseEntity<JSONObject> eateryCreateVoucher(
            @RequestHeader(name = "Authorization") String token, @RequestBody VoucherInput voucher) {
        return eateryService.createVoucher(voucher, token);
    }

    @RequestMapping(value = "eatery/voucher", method = RequestMethod.PUT)
    @CheckUserToken
    public ResponseEntity<JSONObject> DinerListVouchers(
            @RequestHeader(name = "Authorization") String token, @RequestBody VoucherInput voucher) {
        return eateryService.editVoucher(voucher, token);
    }

    @RequestMapping(value = "eatery/voucher", method = RequestMethod.DELETE)
    @CheckUserToken
    public ResponseEntity<JSONObject> eateryDeleteVoucher(
            @RequestHeader(name = "Authorization") String token, @RequestParam Long id) {
        return eateryService.deleteVoucher(id, token);
    }

    @RequestMapping(value = "eatery/verify/voucher", method = RequestMethod.POST)
    @CheckUserToken
    public ResponseEntity<JSONObject> eateryVerifyVoucher(
            @RequestHeader(name = "Authorization") String token, @RequestParam String code) {
        return eateryService.verifyVoucher(code, token);
    }
}
