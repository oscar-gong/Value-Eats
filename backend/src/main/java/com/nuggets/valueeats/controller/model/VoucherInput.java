package com.nuggets.valueeats.controller.model;

import com.nuggets.valueeats.entity.voucher.VoucherEatingStyle;
import lombok.Data;

import java.time.DayOfWeek;

@Data
public class VoucherInput {
    private Long id;
    private Long eateryId;
    
    private VoucherEatingStyle eatingStyle;
    private Double discount;
    private Integer quantity;

    private Boolean isRecurring;
    private DayOfWeek day;
    private Integer startMinute;
    private Integer endMinute;
}
