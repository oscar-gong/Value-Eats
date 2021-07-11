package com.nuggets.valueeats.service;

import java.text.SimpleDateFormat;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import com.nuggets.valueeats.controller.model.VoucherInput;
import com.nuggets.valueeats.entity.BookingRecord;
import com.nuggets.valueeats.entity.Diner;
import com.nuggets.valueeats.entity.Eatery;
import com.nuggets.valueeats.entity.voucher.RepeatedVoucher;
import com.nuggets.valueeats.entity.voucher.Voucher;
import com.nuggets.valueeats.repository.BookingRecordRepository;
import com.nuggets.valueeats.repository.DinerRepository;
import com.nuggets.valueeats.repository.EateryRepository;
import com.nuggets.valueeats.repository.voucher.RepeatVoucherRepository;
import com.nuggets.valueeats.repository.voucher.VoucherRepository;
import com.nuggets.valueeats.utils.JwtUtils;
import com.nuggets.valueeats.utils.ResponseUtils;
import com.nuggets.valueeats.utils.HelperFunctions;

import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


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

    private static final int MAX_VOUCHER_DURATION = 1440;
    private static final int MIN_VOUCHER_DURATION = 30;

    @Transactional
    public ResponseEntity<JSONObject> createVoucher(VoucherInput voucherInput, String token) {

        String decodedToken = jwtUtils.decode(token);

        if (decodedToken == null) {

            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ResponseUtils.createResponse("Token is not valid or expired"));

        }
        Long eateryId;
        try{
            eateryId = Long.valueOf(decodedToken);
        } catch (NumberFormatException e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ResponseUtils.createResponse("Eatery ID is invalid"));
        }

        if (!eateryRepository.existsByToken(token)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ResponseUtils.createResponse("Token is invalid."));
        }
        
        if(!eateryRepository.existsById(eateryId)){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ResponseUtils.createResponse("Eatery does not exist"));
        }

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

        if (!isValidTime(voucherInput.getDate(), voucherInput.getStartMinute())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ResponseUtils.createResponse("Start time must not be in the past."));
        }

        if ((voucherInput.getEndMinute() - voucherInput.getStartMinute()) < MIN_VOUCHER_DURATION || (voucherInput.getEndMinute() - voucherInput.getStartMinute()) > MAX_VOUCHER_DURATION) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ResponseUtils.createResponse("Vouchers must be at least 30 min long and cannot exceed 24 hours."));
        }

        if (voucherInput.getStartMinute() < 0 || voucherInput.getStartMinute() >= 1440 || voucherInput.getEndMinute() <= 0 || voucherInput.getStartMinute() >= voucherInput.getEndMinute()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ResponseUtils.createResponse("Invalid times"));
        }

        if (voucherInput.getIsRecurring() != null && voucherInput.getIsRecurring()) {
            return handleRecurringCreateVoucher(voucherInput, eateryId);
        }
        return handleOneOffCreateVoucher(voucherInput, eateryId);
    }

    private boolean isValidTime(Date date, Integer minutes){
        Date time = new Date();
        time = Date.from(date.toInstant().plus(Duration.ofMinutes(minutes)));
        Date timeNow = new Date(System.currentTimeMillis());
        timeNow = Date.from(timeNow.toInstant().plus(Duration.ofHours(10)));
        System.out.println(time);
        System.out.println(timeNow);
        return (time.compareTo(timeNow) > 0);
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
        
        repeatedVoucher.setNextUpdate(Date.from(voucherInput.getDate().toInstant().plus(Duration.ofDays(7))));
        repeatedVoucher.setRestockTo(voucherInput.getQuantity());
        repeatedVoucher.setActive(true);

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
        newVoucher.setActive(true);
        voucherRepository.save(newVoucher);

        return ResponseEntity.status(HttpStatus.OK).body(ResponseUtils.createResponse("Successfully created voucher"));
    }

    public ResponseEntity<JSONObject> dinerListVouchers(String token) {

        String id = jwtUtils.decode(token);

        if (id == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ResponseUtils.createResponse("Token is not valid or expired"));
        }
        Long dinerId;
        try{
            dinerId = Long.parseLong(id);
        } catch (NumberFormatException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ResponseUtils.createResponse("Invalid ID format"));
        }

        Boolean isDinerExist = dinerRepository.existsById(dinerId);

        if (isDinerExist == false) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ResponseUtils.createResponse("Diner does not exist, check your token please"));
        }



        ArrayList<BookingRecord> dinerBookings = new ArrayList<BookingRecord>();
        ArrayList<Object> bookingData = new ArrayList<Object>();
        dinerBookings = bookingRecordRepository.findAllByDinerId(dinerId);

        for (BookingRecord booking:dinerBookings) {
            HashMap<String, Object> dinerBooking = new HashMap<String, Object>();
            boolean voucherExists = voucherRepository.existsById(booking.getVoucherId());
            boolean repeatVoucherExists = repeatVoucherRepository.existsById(booking.getVoucherId());

            if (voucherExists) {
                dinerBooking.put("bookingId", booking.getId());
                dinerBooking.put("code", booking.getCode());
                dinerBooking.put("isActive", HelperFunctions.checkActive(booking.getDate(), booking.getEnd()));
                dinerBooking.put("eatingStyle", booking.getEatingStyle());
                dinerBooking.put("discount", booking.getDiscount());
                dinerBooking.put("eateryId", booking.getEateryId());
                dinerBooking.put("isRedeemable", HelperFunctions.isInTimeRange(booking.getDate(), booking.getStart(), booking.getEnd()));
                dinerBooking.put("used", booking.isRedeemed());

                Optional<Eatery> eatery = eateryRepository.findById(booking.getEateryId());
                if (!eatery.isPresent()) {
                    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ResponseUtils.createResponse("Eatery ID is booking is invalid."));
                }
                Eatery eateryDb = eatery.get();
                
                dinerBooking.put("eateryName", eateryDb.getAlias());

                SimpleDateFormat formatter = new SimpleDateFormat("dd MMMM yyyy");
                String strDate = formatter.format(booking.getDate());
                int startHour = booking.getStart() / 60; //since both are ints, you get an int
                int startMinute = booking.getStart() % 60;
                int endHour = booking.getEnd() / 60; //since both are ints, you get an int
                int endMinute = booking.getEnd() % 60;
                
                dinerBooking.put("date", strDate);
                dinerBooking.put("startTime", String.format("%d:%02d", startHour, startMinute));
                dinerBooking.put("endTime", String.format("%d:%02d", endHour, endMinute));

                if (HelperFunctions.isInTimeRange(booking.getDate(), booking.getStart(), booking.getEnd()))
                    dinerBooking.put("duration", HelperFunctions.getDuration(booking.getDate(), booking.getEnd()));

            } else if (repeatVoucherExists) {
                dinerBooking.put("bookingId", booking.getId());
                dinerBooking.put("code", booking.getCode());
                dinerBooking.put("isActive", HelperFunctions.checkActive(booking.getDate(), booking.getEnd()));
                dinerBooking.put("eatingStyle", booking.getEatingStyle());
                dinerBooking.put("discount", booking.getDiscount());
                dinerBooking.put("eateryId", booking.getEateryId());
                dinerBooking.put("isRedeemable", HelperFunctions.isInTimeRange(booking.getDate(), booking.getStart(), booking.getEnd()));
                dinerBooking.put("used", booking.isRedeemed());

                Optional<Eatery> eatery = eateryRepository.findById(booking.getEateryId());
                if (!eatery.isPresent()) {
                    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ResponseUtils.createResponse("Eatery ID is booking is invalid."));
                }
                Eatery eateryDb = eatery.get();

                dinerBooking.put("eateryName", eateryDb.getAlias());

                SimpleDateFormat formatter = new SimpleDateFormat("dd MMMM yyyy");
                String strDate = formatter.format(booking.getDate());
                int startHour = booking.getStart() / 60; //since both are ints, you get an int
                int startMinute = booking.getStart() % 60;
                int endHour = booking.getEnd() / 60; //since both are ints, you get an int
                int endMinute = booking.getEnd() % 60;
                
                dinerBooking.put("date", strDate);
                dinerBooking.put("startTime", String.format("%d:%02d", startHour, startMinute));
                dinerBooking.put("endTime", String.format("%d:%02d", endHour, endMinute));

                if (HelperFunctions.isInTimeRange(booking.getDate(), booking.getStart(), booking.getEnd()))
                    dinerBooking.put("duration", HelperFunctions.getDuration(booking.getDate(), booking.getEnd()));
                    
            }
            bookingData.add(dinerBooking);
        }
        Map<String, Object> dataMedium = new HashMap<>();
        dataMedium.put("vouchers", bookingData);
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

        if (!eateryRepository.existsByToken(token)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ResponseUtils.createResponse("Token is invalid."));
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
            System.out.println(voucherInDb.get());
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
            if ((voucher.getEndMinute() - voucher.getStartMinute()) < MIN_VOUCHER_DURATION || (voucher.getEndMinute() - voucher.getStartMinute()) > MAX_VOUCHER_DURATION) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ResponseUtils.createResponse("Vouchers must be at least 30 min long and cannot exceed 24 hours."));
            }
    
            if (voucher.getStartMinute() < 0 || voucher.getStartMinute() >= 1440 || voucher.getEndMinute() <= 0 || voucher.getStartMinute() >= voucher.getEndMinute()) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ResponseUtils.createResponse("Invalid times"));
            }
            
            if (!isValidTime(voucher.getDate(), voucher.getEndMinute())) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ResponseUtils.createResponse("End time must not be in the past."));
            }
            
            repeatedVoucher.setStart(voucher.getStartMinute());
            repeatedVoucher.setEnd(voucher.getEndMinute());
            repeatedVoucher.setNextUpdate(Date.from(repeatedVoucher.getDate().toInstant().plus(Duration.ofDays(7))));
        } else if (oldVoucher != null) {
            repeatedVoucher.setStart(oldVoucher.getStart());
            repeatedVoucher.setEnd(oldVoucher.getEnd());
            repeatedVoucher.setNextUpdate(Date.from(oldVoucher.getDate().toInstant().plus(Duration.ofDays(7))));
        } else {
            repeatedVoucher.setStart(existingVoucher.getStart());
            repeatedVoucher.setEnd(existingVoucher.getEnd());
            repeatedVoucher.setNextUpdate(Date.from(existingVoucher.getDate().toInstant().plus(Duration.ofDays(7))));
        }
        if (oldVoucher != null){
            voucherRepository.deleteById(oldVoucher.getId());
        }

        repeatedVoucher.setActive(true);

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
            if ((voucher.getEndMinute() - voucher.getStartMinute()) < MIN_VOUCHER_DURATION || (voucher.getEndMinute() - voucher.getStartMinute()) > MAX_VOUCHER_DURATION) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ResponseUtils.createResponse("Vouchers must be at least 30 min long and cannot exceed 24 hours."));
            }
    
            if (voucher.getStartMinute() < 0 || voucher.getStartMinute() >= 1440 || voucher.getEndMinute() <= 0 || voucher.getStartMinute() >= voucher.getEndMinute()) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ResponseUtils.createResponse("Invalid times"));
            }

            if (!isValidTime(voucher.getDate(), voucher.getEndMinute())) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ResponseUtils.createResponse("End time must not be in the past."));
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

        newVoucher.setActive(true);

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

        if (!voucherRepository.existsById(voucherId) && !repeatVoucherRepository.existsById(voucherId)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ResponseUtils.createResponse("Voucher does not exist"));
        }

        if(bookingRecordRepository.existsByDinerIdAndVoucherId(dinerId, voucherId) != 0){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ResponseUtils.createResponse("You cannot book more than one of the same voucher."));
        }
    


        BookingRecord bookingRecord = new BookingRecord();

        if (repeatVoucherRepository.existsById(voucherId)) {
            RepeatedVoucher repeatedVoucher = repeatVoucherRepository.getById(voucherId);

            if (!repeatedVoucher.isActive()) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ResponseUtils.createResponse("Voucher is no longer active."));
            }

            if (repeatedVoucher.getQuantity() < 1) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ResponseUtils.createResponse("No enough voucher for booking"));
            
            } else {

                repeatedVoucher.setQuantity(repeatedVoucher.getQuantity() - 1);
                
                repeatVoucherRepository.save(repeatedVoucher);

            }
            setVoucherDetails(bookingRecord, repeatedVoucher);
            bookingRecord.setId(bookingRecordRepository.findMaxId() == null ? 0 : bookingRecordRepository.findMaxId() + 1);
            bookingRecord.setDinerId(dinerId);
            bookingRecord.setEateryId(repeatedVoucher.getEateryId());
            bookingRecord.setCode(generateRandomCode());
            bookingRecord.setVoucherId(voucherId);
        } else if (voucherRepository.existsById(voucherId)) {
            Voucher voucher = voucherRepository.getById(voucherId);

            if (!voucher.isActive()) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ResponseUtils.createResponse("Voucher is no longer active."));
            }

            if (voucher.getQuantity() < 1) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ResponseUtils.createResponse("No enough voucher for booking"));
            
            } else {
                voucher.setQuantity(voucher.getQuantity() - 1);
                voucherRepository.save(voucher);
            }
            if (voucher.getQuantity() == 0) {
                voucher.setActive(false);
            }

            setVoucherDetails(bookingRecord, voucher);
            bookingRecord.setId(bookingRecordRepository.findMaxId() == null ? 0 : bookingRecordRepository.findMaxId() + 1);
            bookingRecord.setDinerId(dinerId);
            bookingRecord.setEateryId(voucher.getEateryId());
            bookingRecord.setCode(generateRandomCode());
            bookingRecord.setVoucherId(voucherId);
        }
        bookingRecord.setRedeemed(false);
        bookingRecordRepository.save(bookingRecord);

        Map<String,String> dataMedium = new HashMap<>();

        dataMedium.put("id",String.valueOf(bookingRecord.getId()));
        dataMedium.put("dinerId",String.valueOf(bookingRecord.getDinerId()));
        dataMedium.put("eateryId",String.valueOf(bookingRecord.getEateryId()));
        dataMedium.put("code", bookingRecord.getCode());



        JSONObject data = new JSONObject(dataMedium);

        return ResponseEntity.status(HttpStatus.OK).body(ResponseUtils.createResponse("Successfully booked", data));
    }

    private BookingRecord setVoucherDetails(BookingRecord bookingRecord, Voucher voucher) {
        bookingRecord.setEatingStyle(voucher.getEatingStyle());
        bookingRecord.setDiscount(voucher.getDiscount());
        bookingRecord.setDate(voucher.getDate());
        bookingRecord.setStart(voucher.getStart());
        bookingRecord.setEnd(voucher.getEnd());
        return bookingRecord;
    }

    private BookingRecord setVoucherDetails(BookingRecord bookingRecord, RepeatedVoucher repeatedVoucher) {
        bookingRecord.setEatingStyle(repeatedVoucher.getEatingStyle());
        bookingRecord.setDiscount(repeatedVoucher.getDiscount());
        bookingRecord.setDate(repeatedVoucher.getDate());
        bookingRecord.setStart(repeatedVoucher.getStart());
        bookingRecord.setEnd(repeatedVoucher.getEnd());
        return bookingRecord;
    }

    private String generateRandomCode() {
        String uuid = UUID.randomUUID().toString().substring(0, 5);
        while (bookingRecordRepository.existsByCode(uuid)) {
            uuid = UUID.randomUUID().toString().substring(0, 5);
        }
        return uuid;
    }

    public ResponseEntity<JSONObject> deleteVoucher (Long voucherId, String token) {
        String decodedToken = jwtUtils.decode(token);

        if (decodedToken == null) {

            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ResponseUtils.createResponse("Token is not valid or expired"));

        }

        Eatery eateryInDb = eateryRepository.findByToken(token);

        if (eateryInDb == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ResponseUtils.createResponse("Token is not valid or expired"));
        }

        if (!voucherRepository.existsById(voucherId) && !repeatVoucherRepository.existsById(voucherId)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ResponseUtils.createResponse("Voucher does not exist"));
        }

        if (repeatVoucherRepository.existsById(voucherId)) {
            RepeatedVoucher repeatedVoucher = repeatVoucherRepository.getById(voucherId);
            repeatedVoucher.setActive(false);
            repeatedVoucher.setQuantity(0);
            repeatVoucherRepository.save(repeatedVoucher);
        } else if (voucherRepository.existsById(voucherId)) {
            Voucher voucher = voucherRepository.getById(voucherId);
            voucher.setActive(false);
            voucher.setQuantity(0);
            voucherRepository.save(voucher);
        }
        return ResponseEntity.status(HttpStatus.OK).body(ResponseUtils.createResponse("Voucher successfully deleted."));
    }

    public ResponseEntity<JSONObject> verifyVoucher (String code, String token) {
        String decodedToken = jwtUtils.decode(token);

        if (decodedToken == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ResponseUtils.createResponse("Token is not valid or expired"));
        }

        // Check if token is from an eatery
        Eatery eateryInDb = eateryRepository.findByToken(token);

        if (eateryInDb == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ResponseUtils.createResponse("Token is not valid or expired"));
        }
        
        // Check if code is from the eatery
        BookingRecord booking = bookingRecordRepository.findBookingByEateryIdAndCode(eateryInDb.getId(), code);

        if (booking == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ResponseUtils.createResponse("Invalid voucher code."));
        }

        boolean voucherExists = voucherRepository.existsById(booking.getVoucherId());
        boolean repeatedVoucherExists = repeatVoucherRepository.existsById(booking.getVoucherId());

        // Check if voucher is in redeem range
        HashMap<String, Object> dinerBooking = new HashMap<String, Object>();
        if  (voucherExists) {
            if (!HelperFunctions.isInTimeRange(booking.getDate(), booking.getStart(), booking.getEnd())) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ResponseUtils.createResponse("Voucher has expired or cannot be redeemed yet."));
            }
            dinerBooking.put("eatingStyle", booking.getEatingStyle());
            dinerBooking.put("discount", booking.getDiscount());
            dinerBooking.put("date", booking.getDate());
            dinerBooking.put("start", booking.getStart());
            dinerBooking.put("end", booking.getEnd());
        } else if (repeatedVoucherExists) {
            if (!HelperFunctions.isInTimeRange(booking.getDate(), booking.getStart(), booking.getEnd())) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ResponseUtils.createResponse("Voucher has expired or cannot be redeemed yet."));
            }
            dinerBooking.put("eatingStyle", booking.getEatingStyle());
            dinerBooking.put("discount", booking.getDiscount());
            dinerBooking.put("date", booking.getDate());
            dinerBooking.put("start", booking.getStart());
            dinerBooking.put("end", booking.getEnd());
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ResponseUtils.createResponse("Voucher cannot be found."));
        }

        // Check if voucher is already redeemed.
        if (booking.isRedeemed()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ResponseUtils.createResponse("Voucher has already been redeemed."));
        }
        Map<String, Object> dataMedium = new HashMap<>();
        dataMedium.put("data", dinerBooking);
        JSONObject data = new JSONObject(dataMedium);

        // If voucher is, set booking to redeemed.
        booking.setRedeemed(true);

        bookingRecordRepository.save(booking);

        return ResponseEntity.status(HttpStatus.OK).body(ResponseUtils.createResponse("Voucher is valid and has been used!", data));
    }
}
