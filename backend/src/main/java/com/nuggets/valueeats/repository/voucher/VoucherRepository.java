package com.nuggets.valueeats.repository.voucher;

import com.nuggets.valueeats.entity.voucher.Voucher;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

import java.util.ArrayList;

@Repository
public interface VoucherRepository extends JpaRepository<Voucher, Long> {

  ArrayList<Object> findByEateryId (Long eateryId);
}
