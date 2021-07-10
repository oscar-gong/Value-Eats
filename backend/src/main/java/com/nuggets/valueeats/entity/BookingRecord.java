package com.nuggets.valueeats.entity;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.PrimaryKeyJoinColumn;

import com.nuggets.valueeats.entity.voucher.RepeatedVoucher;
import com.nuggets.valueeats.entity.voucher.Voucher;
import com.nuggets.valueeats.entity.voucher.VoucherEatingStyle;

@Entity
@NoArgsConstructor
@Data
@PrimaryKeyJoinColumn(name = "id")
public class BookingRecord {
  @Id
  private Long id;

  private Long dinerId;
  private Long eateryId;
  private Long voucherId;
  private String code;
  private boolean redeemed;
  
  private VoucherEatingStyle eatingStyle;
  private Double discount;
  private Date date;
  private Integer start;
  private Integer end;
}
