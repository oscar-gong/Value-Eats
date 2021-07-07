package com.nuggets.valueeats.repository.voucher;

import com.nuggets.valueeats.entity.voucher.Voucher;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

import java.util.ArrayList;

@Repository
public interface VoucherRepository extends JpaRepository<Voucher, Long> {

  ArrayList<Voucher> findByEateryId (Long eateryId);

  @Query("select max(id) from Voucher")
  Long findMaxId();
}
