package com.nuggets.valueeats.service;

import com.nuggets.valueeats.controller.model.VoucherInput;
import com.nuggets.valueeats.entity.voucher.RepeatedVoucher;
import com.nuggets.valueeats.entity.voucher.Voucher;
import com.nuggets.valueeats.repository.EateryRepository;
import com.nuggets.valueeats.repository.voucher.RepeatVoucherRepository;
import com.nuggets.valueeats.repository.voucher.VoucherRepository;
import com.nuggets.valueeats.utils.JwtUtils;
import com.nuggets.valueeats.utils.ResponseUtils;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.temporal.TemporalAdjusters;
import java.util.List;
import java.util.Map;
import java.util.ArrayList;
import java.util.HashMap;


@Service
public class VoucherService {
    @Autowired
    private RepeatVoucherRepository repeatVoucherRepository;
    @Autowired
    private VoucherRepository voucherRepository;

    @Autowired
    private EateryRepository eateryRepository;

    @Autowired
    private JwtUtils jwtUtils;

    @Transactional
    public ResponseEntity<JSONObject> createVoucher(VoucherInput voucherInput, String token) {

        String decodedToken = jwtUtils.decode(token);

        if (decodedToken == null) {

            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ResponseUtils.createResponse("Token is not valid or expired"));

        }
        Long eateryId = Long.valueOf(decodedToken);


        if (voucherInput.getEatingStyle() == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ResponseUtils.createResponse("Received a null eating style"));
        }
        if (voucherInput.getDiscount() == null || voucherInput.getDiscount() <= 0 || voucherInput.getDiscount() > 100) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ResponseUtils.createResponse("Discount is invalid"));
        }
        if (voucherInput.getQuantity() == null || voucherInput.getQuantity() < 1) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ResponseUtils.createResponse("Invalid voucher quantity"));
        }
        if (voucherInput.getDay() == null || voucherInput.getStartMinute() == null || voucherInput.getEndMinute() == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ResponseUtils.createResponse("Invalid dates"));
        }
        if (voucherInput.getStartMinute() == null && voucherInput.getEndMinute() == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ResponseUtils.createResponse("Start and/or end minute missing"));
        }

        if (voucherInput.getStartMinute() < 0 || voucherInput.getStartMinute() >= 1440 || voucherInput.getEndMinute() <= 0 || voucherInput.getEndMinute() > 1440 || voucherInput.getStartMinute() >= voucherInput.getEndMinute()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ResponseUtils.createResponse("Invalid times"));
        }

        if (voucherInput.getIsRecurring() != null && voucherInput.getIsRecurring()) {
            return handleRecurringCreateVoucher(voucherInput, eateryId);
        }
        return handleOneOffCreateVoucher(voucherInput, eateryId);
    }

    private ResponseEntity<JSONObject> handleRecurringCreateVoucher(VoucherInput voucherInput, Long eateryId) {
        RepeatedVoucher repeatedVoucher = new RepeatedVoucher();
        repeatedVoucher.setEateryId(eateryId);
        repeatedVoucher.setEatingStyle(voucherInput.getEatingStyle());
        repeatedVoucher.setDiscount(voucherInput.getDiscount());
        repeatedVoucher.setQuantity(voucherInput.getQuantity());
        repeatedVoucher.setDay(voucherInput.getDay());

        repeatedVoucher.setStart(voucherInput.getStartMinute());
        repeatedVoucher.setEnd(voucherInput.getEndMinute());

        repeatedVoucher.setNextUpdate(Timestamp.valueOf(LocalDateTime.now().with(TemporalAdjusters.next(voucherInput.getDay()))));

        repeatVoucherRepository.save(repeatedVoucher);

        return handleOneOffCreateVoucher(voucherInput, eateryId);
    }

    private ResponseEntity<JSONObject> handleOneOffCreateVoucher(VoucherInput voucherInput, Long eateryId) {
        Voucher newVoucher = new Voucher();
        newVoucher.setEateryId(eateryId);
        newVoucher.setEatingStyle(voucherInput.getEatingStyle());
        newVoucher.setDiscount(voucherInput.getDiscount());
        newVoucher.setQuantity(voucherInput.getQuantity());

        try {
            newVoucher.setStart(new Timestamp(new SimpleDateFormat("mm").parse(String.valueOf(voucherInput.getStartMinute())).getTime() + LocalDateTime.now().with(TemporalAdjusters.next(voucherInput.getDay())).atZone(ZoneId.systemDefault()).toEpochSecond() * 1000));
            newVoucher.setEnd(new Timestamp(new SimpleDateFormat("mm").parse(String.valueOf(voucherInput.getEndMinute())).getTime() + LocalDateTime.now().with(TemporalAdjusters.next(voucherInput.getDay())).atZone(ZoneId.systemDefault()).toEpochSecond() * 1000));
        } catch (ParseException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ResponseUtils.createResponse("Given dates are invalid"));
        }

        voucherRepository.save(newVoucher);

        return ResponseEntity.status(HttpStatus.OK).body(ResponseUtils.createResponse("Successfully created voucher"));
    }


    public ResponseEntity<JSONObject> listVouchers(String token, Long id) {

        String decodedToken = jwtUtils.decode(token);

        Long eateryId;

        if (decodedToken == null) {

            eateryId = id;

        } else {
            
            eateryId = Long.valueOf(decodedToken);

        } 

        if (!eateryRepository.existsById(eateryId)) {

            eateryId = id;

        }
        
        Boolean isEateryExit = eateryRepository.existsById(eateryId);
        
        if (isEateryExit == false) {

            return ResponseEntity.status(HttpStatus.OK).body(ResponseUtils.createResponse("Eatery does not exist, check your token and eatery Id"));

        }

        ArrayList<Object> vouchersList = voucherRepository.findByEateryId(eateryId);

        if (vouchersList == null) {

            return ResponseEntity.status(HttpStatus.OK).body(ResponseUtils.createResponse("Eatery does not have any vouchers"));

        }
        Map<String, ArrayList<Object>> dataMedium = new HashMap<>();

        dataMedium.put("voucherList", vouchersList);
        
        JSONObject data = new JSONObject(dataMedium);

        return ResponseEntity.status(HttpStatus.OK).body(ResponseUtils.createResponse(data));
    }
}
