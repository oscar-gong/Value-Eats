package com.nuggets.valueeats.controller.model;

import java.util.Date;

import com.nuggets.valueeats.entity.voucher.VoucherEatingStyle;
import lombok.Data;


@Data
public class VoucherInput {
    private Long id;
    private Long eateryId;
    
    private VoucherEatingStyle eatingStyle;
    private Double discount;
    private Integer quantity;

    private Boolean isRecurring;
    private Date date;
    private Integer startMinute;
    private Integer endMinute;
}
