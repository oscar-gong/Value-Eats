package com.nuggets.valueeats.service;

import com.nuggets.valueeats.controller.model.VoucherInput;
import com.nuggets.valueeats.entity.BookingRecord;
import com.nuggets.valueeats.entity.Diner;
import com.nuggets.valueeats.entity.voucher.RepeatedVoucher;
import com.nuggets.valueeats.entity.voucher.Voucher;
import com.nuggets.valueeats.repository.BookingRecordRepository;
import com.nuggets.valueeats.repository.DinerRepository;
import com.nuggets.valueeats.repository.EateryRepository;
import com.nuggets.valueeats.repository.voucher.RepeatVoucherRepository;
import com.nuggets.valueeats.repository.voucher.VoucherRepository;
import com.nuggets.valueeats.utils.JwtUtils;
import com.nuggets.valueeats.utils.ResponseUtils;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;
import java.util.Optional;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.temporal.TemporalAdjusters;
import java.util.List;
import java.util.Map;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;


@Service
public class VoucherService {
    @Autowired
    private RepeatVoucherRepository repeatVoucherRepository;
    @Autowired
    private VoucherRepository voucherRepository;

    @Autowired
    private DinerRepository dinerRepository;

    @Autowired
    private EateryRepository eateryRepository;

    @Autowired
    private BookingRecordRepository bookingRecordRepository;

    @Autowired
    private JwtUtils jwtUtils;

    @Transactional
    public ResponseEntity<JSONObject> createVoucher(VoucherInput voucherInput, String token) {

        String decodedToken = jwtUtils.decode(token);

        if (decodedToken == null) {

            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ResponseUtils.createResponse("Token is not valid or expired"));

        }
        Long eateryId = Long.valueOf(decodedToken);
        System.out.println("might get locked");
        if(!eateryRepository.existsById(eateryId)){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ResponseUtils.createResponse("Eatery does not exist"));
        }
        System.out.println("lock?");

        if (voucherInput.getEatingStyle() == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ResponseUtils.createResponse("Received a null eating style"));
        }
        if (voucherInput.getDiscount() == null || voucherInput.getDiscount() <= 0 || voucherInput.getDiscount() > 100) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ResponseUtils.createResponse("Discount is invalid"));
        }
        if (voucherInput.getQuantity() == null || voucherInput.getQuantity() < 1) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ResponseUtils.createResponse("Invalid voucher quantity"));
        }
        if (voucherInput.getDate() == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ResponseUtils.createResponse("Invalid date"));
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
        repeatedVoucher.setId(getNextVoucherId());
        repeatedVoucher.setEateryId(eateryId);
        repeatedVoucher.setEatingStyle(voucherInput.getEatingStyle());
        repeatedVoucher.setDiscount(voucherInput.getDiscount());
        repeatedVoucher.setQuantity(voucherInput.getQuantity());
        repeatedVoucher.setDate(voucherInput.getDate());
        repeatedVoucher.setStart(voucherInput.getStartMinute());
        repeatedVoucher.setEnd(voucherInput.getEndMinute());

        repeatedVoucher.setNextUpdate(getDateTime(voucherInput.getDate(), voucherInput.getStartMinute()));
        repeatedVoucher.setRestockTo(voucherInput.getQuantity());

        repeatVoucherRepository.save(repeatedVoucher);

        return ResponseEntity.status(HttpStatus.OK).body(ResponseUtils.createResponse("Successfully created recurring voucher"));
    }

    private ResponseEntity<JSONObject> handleOneOffCreateVoucher(VoucherInput voucherInput, Long eateryId) {
        Voucher newVoucher = new Voucher();
        newVoucher.setId(getNextVoucherId());
        newVoucher.setEateryId(eateryId);
        newVoucher.setEatingStyle(voucherInput.getEatingStyle());
        newVoucher.setDiscount(voucherInput.getDiscount());
        newVoucher.setQuantity(voucherInput.getQuantity());
        newVoucher.setDate(voucherInput.getDate());
        newVoucher.setStart(voucherInput.getStartMinute());
        newVoucher.setEnd(voucherInput.getEndMinute());
        voucherRepository.save(newVoucher);

        return ResponseEntity.status(HttpStatus.OK).body(ResponseUtils.createResponse("Successfully created voucher"));
    }

    private Date getDateTime(Date date, Integer minute){
        return Date.from(date.toInstant().plus(Duration.ofMinutes(minute)));
    }


    public ResponseEntity<JSONObject> eateryListVouchers(String token, Long id) {

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
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ResponseUtils.createResponse("Eatery does not exist, the token or eatery Id must be valid"));
        }

        ArrayList<Voucher> vouchersList = voucherRepository.findByEateryId(eateryId);

        if (vouchersList == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ResponseUtils.createResponse("Eatery does not have any vouchers"));
        }
        
        List<Map<String,String>> ls = new ArrayList<Map<String,String>>();

        for (Voucher v : vouchersList) {
            Map<String,String> tmp = new HashMap<>();
            tmp.put("id", Long.toString(v.getId()));
            tmp.put("eateryId", Long.toString(v.getEateryId()));
            tmp.put("eatingStyle", v.getEatingStyle().toString());
            tmp.put("discount", Double.toString(v.getDiscount()));
            tmp.put("quantity", Integer.toString(v.getQuantity()));
            tmp.put("start", v.getQuantity().toString());
            tmp.put("end", v.getEnd().toString());
            ls.add(tmp);
        }
        Map<String, List<Map<String,String>>> dataMedium = new HashMap<>();
        
        dataMedium.put("voucherList", ls);
        
        JSONObject data = new JSONObject(dataMedium);

        return ResponseEntity.status(HttpStatus.OK).body(ResponseUtils.createResponse(data));
    }

    public ResponseEntity<JSONObject> dinerListVouchers(String token, Long eateryId) {

        String decodedToken = jwtUtils.decode(token);

        if (decodedToken == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ResponseUtils.createResponse("Token is not valid or expired"));
        }

        Boolean isDinerExit = dinerRepository.existsByToken(token);

        if (isDinerExit == false) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ResponseUtils.createResponse("Diner does not exist, check your token please"));
        }
        
        Boolean isEateryExit = eateryRepository.existsById(eateryId);
        
        if (isEateryExit == false) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ResponseUtils.createResponse("Eatery does not exist, check your eatery Id please"));
        }

        ArrayList<Voucher> vouchersList = voucherRepository.findByEateryId(eateryId);

        if (vouchersList == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ResponseUtils.createResponse("Eatery does not have any vouchers"));
        }
        
        List<Map<String,String>> ls = new ArrayList<Map<String,String>>();

        for (Voucher v : vouchersList) {
            Map<String,String> tmp = new HashMap<>();
            tmp.put("id", Long.toString(v.getId()));
            tmp.put("eateryId", Long.toString(v.getEateryId()));
            tmp.put("eatingStyle", v.getEatingStyle().toString());
            tmp.put("discount", Double.toString(v.getDiscount()));
            tmp.put("quantity", Integer.toString(v.getQuantity()));
            tmp.put("start", v.getQuantity().toString());
            tmp.put("end", v.getEnd().toString());
            ls.add(tmp);
        }
        Map<String, List<Map<String,String>>> dataMedium = new HashMap<>();
        
        dataMedium.put("voucherList", ls);
        
        JSONObject data = new JSONObject(dataMedium);

        return ResponseEntity.status(HttpStatus.OK).body(ResponseUtils.createResponse(data));
    }

    // Take a voucher and an eatery token as input.
    public ResponseEntity<JSONObject> editVoucher(VoucherInput voucher, String token) {
        if (voucher == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ResponseUtils.createResponse("Please enter the valid information of a voucher"));
        }
        String decodedToken = jwtUtils.decode(token);

        if (decodedToken == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ResponseUtils.createResponse("Token is not valid or expired"));
        }
        Long eateryId = Long.valueOf(decodedToken);
        Boolean isEateryExist = eateryRepository.existsById(eateryId);
        
        if (isEateryExist == false) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ResponseUtils.createResponse("Eatery does not exist, check your token again"));
        }

        if (voucher.getEateryId() != eateryId) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ResponseUtils.createResponse("Voucher and eatery does not match, check again"));
        }
        
        Long voucherId = voucher.getId();

        Optional<Voucher> voucherInDb = voucherRepository.findById(voucherId);
        Optional<RepeatedVoucher> repeatedVoucherInDb = repeatVoucherRepository.findById(voucherId);

        if (!voucherInDb.isPresent() && !repeatedVoucherInDb.isPresent()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ResponseUtils.createResponse("Voucher does not exist, check again"));
        }

        if (voucherInDb.isPresent()){
            Voucher voucherDb = voucherInDb.get();
            // Convert it to recurring and delete old voucher
            if (voucher.getIsRecurring() != null && voucher.getIsRecurring() == true){
                return editRepeatedVoucher(voucher, voucherDb, null);
            }else{ // Do normal updates
                return editVoucher(voucher, null, voucherDb);
            }
        }

        if (repeatedVoucherInDb.isPresent()){
            RepeatedVoucher repeatedVoucherDb = repeatedVoucherInDb.get();
            // Convert to a one-time voucher and delete old one
            if (voucher.getIsRecurring() != null && voucher.getIsRecurring() == false){
                return editVoucher(voucher, repeatedVoucherDb, null);
            } else { // Edit existing recurring voucher
                return editRepeatedVoucher(voucher, null, repeatedVoucherDb);
            }
        }
        return ResponseEntity.status(HttpStatus.OK).body(ResponseUtils.createResponse("Voucher was edited successfully.")); 
    }

    public ResponseEntity<JSONObject> editRepeatedVoucher(VoucherInput voucher, Voucher oldVoucher, RepeatedVoucher existingVoucher){
        RepeatedVoucher repeatedVoucher = new RepeatedVoucher();
        if (oldVoucher == null && existingVoucher != null){
            repeatedVoucher.setId(existingVoucher.getId());
        } else {
            repeatedVoucher.setId(oldVoucher.getId());
        }
        
        repeatedVoucher.setEateryId(voucher.getEateryId());
        if (voucher.getEatingStyle() != null){
            repeatedVoucher.setEatingStyle(voucher.getEatingStyle());
        } else if (oldVoucher != null){
            repeatedVoucher.setEatingStyle(oldVoucher.getEatingStyle());
        } else {
            repeatedVoucher.setEatingStyle(existingVoucher.getEatingStyle());
        }

        if (voucher.getQuantity() != null) {
            if (voucher.getQuantity() < 1) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ResponseUtils.createResponse("Invalid voucher quantity"));
            }
            repeatedVoucher.setQuantity(voucher.getQuantity());
            repeatedVoucher.setRestockTo(voucher.getQuantity());
        } else if (oldVoucher != null) {
            repeatedVoucher.setQuantity(oldVoucher.getQuantity());
            repeatedVoucher.setRestockTo(oldVoucher.getQuantity());
        } else {
            repeatedVoucher.setQuantity(existingVoucher.getQuantity());
            repeatedVoucher.setRestockTo(existingVoucher.getRestockTo());
        }

        if (voucher.getDiscount() != null) {
            if (voucher.getDiscount() < 0 || voucher.getDiscount() > 100) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ResponseUtils.createResponse("Invalid voucher discount"));
            }
            repeatedVoucher.setDiscount(voucher.getDiscount());
        } else if (oldVoucher != null) {
            repeatedVoucher.setDiscount(oldVoucher.getDiscount());
        } else {
            repeatedVoucher.setDiscount(existingVoucher.getDiscount());
        }

        if (voucher.getDate() != null) {
            repeatedVoucher.setDate(voucher.getDate());
        } else if (oldVoucher != null){
            repeatedVoucher.setDate(oldVoucher.getDate());
        } else {
            repeatedVoucher.setDate(existingVoucher.getDate());
        }

        if (voucher.getStartMinute()!= null && voucher.getEndMinute() != null) {
            if (voucher.getStartMinute() < 0 || voucher.getStartMinute() >= 1440 || voucher.getEndMinute() <= 0 || voucher.getEndMinute() > 1440 || voucher.getStartMinute() >= voucher.getEndMinute()) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ResponseUtils.createResponse("Invalid times"));
            }
            repeatedVoucher.setStart(voucher.getStartMinute());
            repeatedVoucher.setEnd(voucher.getEndMinute());
            repeatedVoucher.setNextUpdate(getDateTime(repeatedVoucher.getDate(), repeatedVoucher.getStart()));
        } else if (oldVoucher != null) {
            repeatedVoucher.setStart(oldVoucher.getStart());
            repeatedVoucher.setEnd(oldVoucher.getEnd());
            repeatedVoucher.setNextUpdate(getDateTime(oldVoucher.getDate(), oldVoucher.getStart()));
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ResponseUtils.createResponse("Please enter new start and end times."));
        } else {
            repeatedVoucher.setStart(existingVoucher.getStart());
            repeatedVoucher.setEnd(existingVoucher.getEnd());
            repeatedVoucher.setNextUpdate(getDateTime(existingVoucher.getDate(), existingVoucher.getStart()));
        }
        if (oldVoucher != null){
            voucherRepository.deleteById(oldVoucher.getId());
        }
        repeatVoucherRepository.save(repeatedVoucher);

        return ResponseEntity.status(HttpStatus.OK).body(ResponseUtils.createResponse("Voucher was edited successfully.")); 
    }

    public ResponseEntity<JSONObject> editVoucher(VoucherInput voucher, RepeatedVoucher oldVoucher, Voucher existingVoucher){
        Voucher newVoucher = new Voucher();
        if (oldVoucher == null && existingVoucher != null){
            newVoucher.setId(existingVoucher.getId());
        } else {
            newVoucher.setId(oldVoucher.getId());
        }
        newVoucher.setEateryId(voucher.getEateryId());

        if (voucher.getEatingStyle() != null) {
            newVoucher.setEatingStyle(voucher.getEatingStyle());
        } else if (oldVoucher != null) {
            newVoucher.setEatingStyle(oldVoucher.getEatingStyle());
        } else {
            newVoucher.setEatingStyle(existingVoucher.getEatingStyle());
        }

        if (voucher.getQuantity() != null) {
            if (voucher.getQuantity() < 1) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ResponseUtils.createResponse("Invalid voucher quantity"));
            }
            newVoucher.setQuantity(voucher.getQuantity());
        } else if (oldVoucher != null) {
            newVoucher.setQuantity(oldVoucher.getQuantity());
        } else {
            newVoucher.setQuantity(existingVoucher.getQuantity());
        }

        if (voucher.getDiscount() != null) {
            if (voucher.getDiscount() < 0 || voucher.getDiscount() > 100) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ResponseUtils.createResponse("Invalid voucher discount"));
            }
            newVoucher.setDiscount(voucher.getDiscount());
        } else if (oldVoucher != null) {
            newVoucher.setDiscount(oldVoucher.getDiscount());
        } else {
            newVoucher.setDiscount(existingVoucher.getDiscount());
        }

        if (voucher.getDate() != null) {
            newVoucher.setDate(voucher.getDate());
        } else if (oldVoucher != null){
            newVoucher.setDate(oldVoucher.getDate());
        } else {
            newVoucher.setDate(existingVoucher.getDate());
        }

        if (voucher.getStartMinute()!= null && voucher.getEndMinute() != null) {
            if (voucher.getStartMinute() < 0 || voucher.getStartMinute() >= 1440 || voucher.getEndMinute() <= 0 || voucher.getEndMinute() > 1440 || voucher.getStartMinute() >= voucher.getEndMinute()) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ResponseUtils.createResponse("Invalid times"));
            }
            newVoucher.setStart(voucher.getStartMinute());
            newVoucher.setEnd(voucher.getEndMinute());
        } else if (oldVoucher != null) {
            newVoucher.setStart(oldVoucher.getStart());
            newVoucher.setEnd(oldVoucher.getEnd());
        } else {
            newVoucher.setStart(existingVoucher.getStart());
            newVoucher.setEnd(existingVoucher.getEnd());
        }

        if (oldVoucher != null){
            repeatVoucherRepository.deleteById(oldVoucher.getId());
        }
        voucherRepository.save(newVoucher);
        return ResponseEntity.status(HttpStatus.OK).body(ResponseUtils.createResponse("Voucher was edited successfully.")); 
    }

    private Long getNextVoucherId(){
        Long newId;
        if (repeatVoucherRepository.findMaxId() != null && voucherRepository.findMaxId() != null){
            newId = Math.max((repeatVoucherRepository.findMaxId()+1), (voucherRepository.findMaxId()+1));
        } else if (repeatVoucherRepository.findMaxId() != null) {
            newId = repeatVoucherRepository.findMaxId()+1;
        }else if (voucherRepository.findMaxId() != null) {
            newId = voucherRepository.findMaxId()+1;
        }else {
            newId = (long) 0;
        }
        return newId;
    }

    // Input: voucher id && diner token.
    public ResponseEntity<JSONObject> bookVoucher (Long voucherId, String token) {
        
        String decodedToken = jwtUtils.decode(token);

        if (decodedToken == null) {

            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ResponseUtils.createResponse("Token is not valid or expired"));

        }

        Diner dinerInDb = dinerRepository.findByToken(token);

        if (dinerInDb == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ResponseUtils.createResponse("Token is not valid or expired"));
        }
        
        Long dinerId = dinerInDb.getId();

        boolean isExist = voucherRepository.existsById(voucherId);


        BookingRecord bookingRecord = new BookingRecord();

        if (isExist == false) {
            isExist = repeatVoucherRepository.existsById(voucherId);

            if (isExist == false) {

                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ResponseUtils.createResponse("Voucher does not exist"));
            
            } else {  
            
                RepeatedVoucher repeatedVoucher = repeatVoucherRepository.getById(voucherId);
                bookingRecord.setId(bookingRecordRepository.findMaxId() == null ? 0 : bookingRecordRepository.findMaxId() + 1);
                bookingRecord.setDinerId(dinerId);
                bookingRecord.setEateryId(repeatedVoucher.getEateryId());
                bookingRecord.setEatingStyle(repeatedVoucher.getEatingStyle());

                String code = jwtUtils.encode(String.valueOf(bookingRecord.getId()));

                bookingRecord.setDiscount(repeatedVoucher.getDiscount());
                bookingRecord.setDate(repeatedVoucher.getDate());
                bookingRecord.setStart(repeatedVoucher.getStart());
                bookingRecord.setEnd(repeatedVoucher.getEnd());
            }   
        } else {
            Voucher voucher = voucherRepository.getById(voucherId);

            bookingRecord.setId(bookingRecordRepository.findMaxId() == null ? 0 : bookingRecordRepository.findMaxId() + 1);
            bookingRecord.setDinerId(dinerId);
            bookingRecord.setEateryId(voucher.getEateryId());
            bookingRecord.setEatingStyle(voucher.getEatingStyle());

            String code = jwtUtils.encode(String.valueOf(bookingRecord.getId()));

            bookingRecord.setDiscount(voucher.getDiscount());
            bookingRecord.setDate(voucher.getDate());
            bookingRecord.setStart(voucher.getStart());
            bookingRecord.setEnd(voucher.getEnd());
        }

        bookingRecordRepository.save(bookingRecord);

        return ResponseEntity.status(HttpStatus.OK).body(ResponseUtils.createResponse("Successfully booked: " + bookingRecord.getCode()));

    }
}
