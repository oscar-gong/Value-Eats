package com.nuggets.valueeats.repository;

import com.nuggets.valueeats.entity.voucher.RepeatedVoucher;
import com.nuggets.valueeats.entity.voucher.Voucher;
import com.nuggets.valueeats.repository.voucher.RepeatVoucherRepository;
import com.nuggets.valueeats.repository.voucher.VoucherRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.temporal.TemporalAdjusters;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

@Configuration
@EnableScheduling
public class DatabaseCleaner {
    @Autowired
    private RepeatVoucherRepository repeatVoucherRepository;
    @Autowired
    private VoucherRepository voucherRepository;

    @Scheduled(fixedDelay = 10000)
    public void updateVoucher() {
        List<RepeatedVoucher> repeatedVouchers = repeatVoucherRepository.findAll();

        Date timeNow = new Date(System.currentTimeMillis());
        timeNow = Date.from(timeNow.toInstant().plus(Duration.ofHours(10)));

        if (repeatedVouchers != null) {
            for (RepeatedVoucher repeatedVoucher : repeatedVouchers) {
                if (repeatedVoucher.getNextUpdate().compareTo(timeNow) < 0){
                    System.out.println("Updated Voucher " + repeatedVoucher.getId());
                    repeatedVoucher.setQuantity(repeatedVoucher.getRestockTo());
                    repeatedVoucher.setNextUpdate(Date.from(repeatedVoucher.getDate().toInstant().plus(Duration.ofMinutes(repeatedVoucher.getStart()).plus(Duration.ofDays(7)))));
                    repeatedVoucher.setDate(Date.from(repeatedVoucher.getNextUpdate().toInstant().minus(Duration.ofDays(7)).minus(Duration.ofMinutes(repeatedVoucher.getStart()))));
                    repeatVoucherRepository.save(repeatedVoucher);
                }
            }
        }
    }
}
