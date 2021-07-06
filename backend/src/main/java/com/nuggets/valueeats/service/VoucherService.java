package com.nuggets.valueeats.service;

import com.nuggets.valueeats.controller.model.VoucherInput;
import com.nuggets.valueeats.entity.voucher.RepeatedVoucher;
import com.nuggets.valueeats.entity.voucher.Voucher;
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
    private DinerRepository dinerRepository;

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
        } else if (oldVoucher != null) {
            repeatedVoucher.setQuantity(oldVoucher.getQuantity());
        } else {
            repeatedVoucher.setQuantity(existingVoucher.getQuantity());
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

        if (voucher.getDay() != null) {
            repeatedVoucher.setDay(voucher.getDay());
        } else if (existingVoucher != null){
            repeatedVoucher.setDay(existingVoucher.getDay());
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ResponseUtils.createResponse("Day must be provided."));
        }

        if (voucher.getStartMinute()!= null && voucher.getEndMinute() != null) {
            if (voucher.getStartMinute() < 0 || voucher.getStartMinute() >= 1440 || voucher.getEndMinute() <= 0 || voucher.getEndMinute() > 1440 || voucher.getStartMinute() >= voucher.getEndMinute()) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ResponseUtils.createResponse("Invalid times"));
            }
            repeatedVoucher.setStart(voucher.getStartMinute());
            repeatedVoucher.setEnd(voucher.getEndMinute());
            repeatedVoucher.setNextUpdate(Timestamp.valueOf(LocalDateTime.now().with(TemporalAdjusters.next(voucher.getDay()))));
        } else if (oldVoucher != null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ResponseUtils.createResponse("Please enter new start and end times."));
        } else {
            repeatedVoucher.setStart(existingVoucher.getStart());
            repeatedVoucher.setEnd(existingVoucher.getEnd());
            repeatedVoucher.setNextUpdate(Timestamp.valueOf(LocalDateTime.now().with(TemporalAdjusters.next(existingVoucher.getDay()))));
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

        if (voucher.getStartMinute()!= null && voucher.getEndMinute() != null) {
            if (voucher.getStartMinute() < 0 || voucher.getStartMinute() >= 1440 || voucher.getEndMinute() <= 0 || voucher.getEndMinute() > 1440 || voucher.getStartMinute() >= voucher.getEndMinute()) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ResponseUtils.createResponse("Invalid times"));
            }
            try {
                newVoucher.setStart(new Timestamp(new SimpleDateFormat("mm").parse(String.valueOf(voucher.getStartMinute())).getTime() + LocalDateTime.now().with(TemporalAdjusters.next(voucher.getDay())).atZone(ZoneId.systemDefault()).toEpochSecond() * 1000));
                newVoucher.setEnd(new Timestamp(new SimpleDateFormat("mm").parse(String.valueOf(voucher.getEndMinute())).getTime() + LocalDateTime.now().with(TemporalAdjusters.next(voucher.getDay())).atZone(ZoneId.systemDefault()).toEpochSecond() * 1000));
            } catch (ParseException e) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ResponseUtils.createResponse("Given dates are invalid"));
            }
        } else if (oldVoucher != null) {
            try {
                newVoucher.setStart(new Timestamp(new SimpleDateFormat("mm").parse(String.valueOf(oldVoucher.getStart())).getTime() + LocalDateTime.now().with(TemporalAdjusters.next(voucher.getDay())).atZone(ZoneId.systemDefault()).toEpochSecond() * 1000));
                newVoucher.setEnd(new Timestamp(new SimpleDateFormat("mm").parse(String.valueOf(oldVoucher.getEnd())).getTime() + LocalDateTime.now().with(TemporalAdjusters.next(voucher.getDay())).atZone(ZoneId.systemDefault()).toEpochSecond() * 1000));
            } catch (ParseException e) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ResponseUtils.createResponse("Given dates are invalid"));
            }
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
}
