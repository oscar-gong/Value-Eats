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
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.temporal.TemporalAdjusters;
import java.util.List;

@Configuration
@EnableScheduling
public class DatabaseCleaner {
    @Autowired
    private RepeatVoucherRepository repeatVoucherRepository;
    @Autowired
    private VoucherRepository voucherRepository;

    @Scheduled(fixedDelay = 100000)
    public void updateVoucher() {
        List<RepeatedVoucher> repeatedVouchers = repeatVoucherRepository.findOverdueRepeatVouchers();
        if (repeatedVouchers != null) {
            System.out.println("We are cleaning up " + repeatedVouchers.size());

            for (final RepeatedVoucher repeatedVoucher : repeatedVouchers) {
                Voucher newVoucher = new Voucher();
                newVoucher.setEateryId(repeatedVoucher.getEateryId());
                newVoucher.setEatingStyle(repeatedVoucher.getEatingStyle());
                newVoucher.setDiscount(repeatedVoucher.getDiscount());
                newVoucher.setQuantity(repeatedVoucher.getQuantity());

                try {
                    newVoucher.setStart(new Timestamp(new SimpleDateFormat("mm").parse(String.valueOf(repeatedVoucher.getStart())).getTime() + LocalDateTime.now().with(TemporalAdjusters.next(repeatedVoucher.getDay())).atZone(ZoneId.systemDefault()).toEpochSecond() * 1000));
                    newVoucher.setEnd(new Timestamp(new SimpleDateFormat("mm").parse(String.valueOf(repeatedVoucher.getEnd())).getTime() + LocalDateTime.now().with(TemporalAdjusters.next(repeatedVoucher.getDay())).atZone(ZoneId.systemDefault()).toEpochSecond() * 1000));
                } catch (ParseException e) {
                    System.out.println("You have a data integrity issue with " + repeatedVoucher.getId());
                    continue;
                }
                voucherRepository.save(newVoucher);

                repeatedVoucher.setNextUpdate(Timestamp.valueOf(LocalDateTime.now().with(TemporalAdjusters.next(repeatedVoucher.getDay()))));
                repeatVoucherRepository.save(repeatedVoucher);
            }
        }
    }
}
