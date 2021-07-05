package com.nuggets.valueeats.entity.voucher;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.sql.Timestamp;
import java.time.DayOfWeek;

@Entity
@NoArgsConstructor
@Data
public class RepeatedVoucher {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private Long eateryId;

    private VoucherEatingStyle eatingStyle;
    private Double discount;
    private Integer quantity;

    private DayOfWeek day;
    private Integer start;
    private Integer end;

    private Timestamp nextUpdate;
}
