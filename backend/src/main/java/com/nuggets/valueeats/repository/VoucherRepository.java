package com.nuggets.valueeats.repository;

import com.nuggets.valueeats.entity.Voucher;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface VoucherRepository extends JpaRepository<Voucher, Long> {
    @Query("select max(id) from Voucher")
    Long findMaxId();
}