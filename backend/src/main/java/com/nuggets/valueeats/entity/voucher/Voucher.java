package com.nuggets.valueeats.entity.voucher;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.sql.Timestamp;
import java.time.DayOfWeek;

@NoArgsConstructor
@Data
@Entity
public class Voucher {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private Long eateryId;

    private VoucherEatingStyle eatingStyle;
    private Double discount;
    private Integer quantity;

    private DayOfWeek dayOfWeek;
    private Timestamp start;
    private Timestamp end;
}
