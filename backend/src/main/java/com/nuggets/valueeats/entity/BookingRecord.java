package com.nuggets.valueeats.entity;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.PrimaryKeyJoinColumn;

import com.nuggets.valueeats.entity.voucher.VoucherEatingStyle;

import java.util.Date;
import java.util.ArrayList;
import java.util.List;

@Entity
@NoArgsConstructor
@Data
@PrimaryKeyJoinColumn(name = "id")
public class BookingRecord {
  @Id
  private Long id;

  private Long dinerId;
  private Long eateryId;

  private VoucherEatingStyle eatingStyle;
  private String code;
  private Double discount;

  private Date date;
  private Integer start;
  private Integer end;

}
