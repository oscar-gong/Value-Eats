package com.nuggets.valueeats.utils;

import java.text.SimpleDateFormat;
import java.time.Duration;
import java.util.Date;
import java.util.HashMap;

import com.nuggets.valueeats.entity.Diner;
import com.nuggets.valueeats.entity.voucher.VoucherEatingStyle;
import com.nuggets.valueeats.repository.BookingRecordRepository;
import com.nuggets.valueeats.repository.voucher.RepeatVoucherRepository;
import com.nuggets.valueeats.repository.voucher.VoucherRepository;

public class VoucherUtils {
    
    public static Long getDuration(Date date, Integer end) {
        Date endTime = Date.from(date.toInstant().plus(Duration.ofMinutes(end)));
        Date timeNow = new Date(System.currentTimeMillis());
        timeNow = Date.from(timeNow.toInstant().plus(Duration.ofHours(10)));

        return Duration.between(timeNow.toInstant(), endTime.toInstant()).toMillis();
    }

    public static boolean checkActive (Date date, Integer end) {
        Date timeNow = new Date(System.currentTimeMillis());
        timeNow = Date.from(timeNow.toInstant().plus(Duration.ofHours(10)));
        Date endTime = Date.from(date.toInstant().plus(Duration.ofMinutes(end)));;
        return (endTime.compareTo(timeNow) > 0);
    }

    public static boolean isInTimeRange(Date date, Integer start, Integer end) {
        Date timeNow = new Date(System.currentTimeMillis());
        timeNow = Date.from(timeNow.toInstant().plus(Duration.ofHours(10)));
        Date startTime = Date.from(date.toInstant().plus(Duration.ofMinutes(start)));
        Date endTime = Date.from(date.toInstant().plus(Duration.ofMinutes(end)));
        // Check if start time before timeNow, endTime after timeNow
        if (startTime.compareTo(timeNow) <= 0 && endTime.compareTo(timeNow) > 0) {
            return true;
        }
        return false;
    }

    public static boolean isValidTime(Date date, Integer minutes){
        Date time = new Date();
        time = Date.from(date.toInstant().plus(Duration.ofMinutes(minutes)));
        Date timeNow = new Date(System.currentTimeMillis());
        timeNow = Date.from(timeNow.toInstant().plus(Duration.ofHours(10)));
        System.out.println(time);
        System.out.println(timeNow);
        return (time.compareTo(timeNow) > 0);
    }

    public static HashMap<String, Object> createVoucher(Long id, Double discount, Long eateryId, VoucherEatingStyle voucherEatingStyle,
                                        Integer quantity, Date date, Integer startTime, Integer endTime, Diner dinerDb,
                                        boolean isRecurring, Date getNextUpdate, BookingRecordRepository bookingRecordRepository) {

        HashMap<String, Object> voucher = new HashMap<String, Object>();

        SimpleDateFormat formatter = new SimpleDateFormat("dd MMMM yyyy");
        String strDate = formatter.format(date);
        int startHour = startTime / 60;
        int startMinute = startTime % 60;
        int endHour = endTime / 60;
        int endMinute = endTime % 60;

        voucher.put("id", id);
        voucher.put("discount", discount);
        voucher.put("eateryId", eateryId);
        voucher.put("eatingStyle", voucherEatingStyle);
        voucher.put("quantity", quantity);
        voucher.put("duration", getDuration(date, endTime));
        voucher.put("isActive", checkActive(date, endTime));
        voucher.put("isRedeemable", isInTimeRange(date, startTime, endTime));
        voucher.put("date", strDate);
        voucher.put("startTime", String.format("%d:%02d", startHour, startMinute));
        voucher.put("endTime", String.format("%d:%02d", endHour, endMinute));
        voucher.put("isRecurring", isRecurring);

        if(dinerDb != null){
            voucher.put("disableButton", (bookingRecordRepository.existsByDinerIdAndVoucherId(dinerDb.getId(), id)) != 0);
        } else {
            voucher.put("disableButton", true);
        }

        if (getNextUpdate != null) {
            String nextUpdate = formatter.format(getNextUpdate);
            voucher.put("nextUpdate", nextUpdate);
        } else {
            voucher.put("nextUpdate", "Deleted");
        }

        return voucher;
    }

    public static Long getNextVoucherId(RepeatVoucherRepository repeatVoucherRepository, VoucherRepository voucherRepository){
        Long newId;
        if (repeatVoucherRepository.findMaxId() != null && voucherRepository.findMaxId() != null){
            newId = Math.max((repeatVoucherRepository.findMaxId()+1), (voucherRepository.findMaxId()+1));
        } else if (repeatVoucherRepository.findMaxId() != null) {
            newId = repeatVoucherRepository.findMaxId()+1;
        } else if (voucherRepository.findMaxId() != null) {
            newId = voucherRepository.findMaxId()+1;
        } else {
            newId = (long) 0;
        }
        return newId;
    }

}
