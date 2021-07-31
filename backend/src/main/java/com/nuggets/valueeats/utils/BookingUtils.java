package com.nuggets.valueeats.utils;

import com.nuggets.valueeats.entity.BookingRecord;
import com.nuggets.valueeats.entity.voucher.RepeatedVoucher;
import com.nuggets.valueeats.entity.voucher.Voucher;
import com.nuggets.valueeats.entity.voucher.VoucherEatingStyle;
import com.nuggets.valueeats.repository.BookingRecordRepository;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.UUID;

public class BookingUtils {
    public static BookingRecord setVoucherDetails(BookingRecord bookingRecord, Voucher voucher) {
        bookingRecord.setEatingStyle(voucher.getEatingStyle());
        bookingRecord.setDiscount(voucher.getDiscount());
        bookingRecord.setDate(voucher.getDate());
        bookingRecord.setStart(voucher.getStart());
        bookingRecord.setEnd(voucher.getEnd());
        return bookingRecord;
    }

    public static BookingRecord setVoucherDetails(BookingRecord bookingRecord, RepeatedVoucher repeatedVoucher) {
        bookingRecord.setEatingStyle(repeatedVoucher.getEatingStyle());
        bookingRecord.setDiscount(repeatedVoucher.getDiscount());
        bookingRecord.setDate(repeatedVoucher.getDate());
        bookingRecord.setStart(repeatedVoucher.getStart());
        bookingRecord.setEnd(repeatedVoucher.getEnd());
        return bookingRecord;
    }

    public static String generateRandomCode(BookingRecordRepository bookingRecordRepository) {
        String uuid = UUID.randomUUID().toString().substring(0, 5);
        while (bookingRecordRepository.existsByCode(uuid)) {
            uuid = UUID.randomUUID().toString().substring(0, 5);
        }
        return uuid;
    }

    public static HashMap<String, Object> createBooking(Long id, String code, Date date, Integer start, Integer end, VoucherEatingStyle eatingStyle, Double discount, Long eateryId, boolean isRedeemed, String alias) {
        SimpleDateFormat formatter = new SimpleDateFormat("dd MMMM yyyy");
        String strDate = formatter.format(date);
        int startHour = start / 60; //since both are ints, you get an int
        int startMinute = start % 60;
        int endHour = end / 60; //since both are ints, you get an int
        int endMinute = end % 60;

        HashMap<String, Object> dinerBooking = new HashMap<>();

        dinerBooking.put("bookingId", id);
        dinerBooking.put("code", code);
        dinerBooking.put("isActive", VoucherUtils.checkActive(date, end));
        dinerBooking.put("eatingStyle", eatingStyle);
        dinerBooking.put("discount", discount);
        dinerBooking.put("eateryId", eateryId);
        dinerBooking.put("isRedeemable", VoucherUtils.isInTimeRange(date, start, end));
        dinerBooking.put("used", isRedeemed);
        dinerBooking.put("eateryName", alias);
        dinerBooking.put("date", strDate);
        dinerBooking.put("startTime", String.format("%d:%02d", startHour, startMinute));
        dinerBooking.put("endTime", String.format("%d:%02d", endHour, endMinute));

        return dinerBooking;
    }
}
