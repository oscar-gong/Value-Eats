package com.nuggets.valueeats.repository.voucher;

import com.nuggets.valueeats.entity.voucher.RepeatedVoucher;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
public interface RepeatVoucherRepository extends JpaRepository<RepeatedVoucher, Long> {
    @Query("select e from RepeatedVoucher e where e.nextUpdate < CURRENT_DATE")
    List<RepeatedVoucher> findOverdueRepeatVouchers();

    @Query("select max(id) from RepeatedVoucher")
    Long findMaxId();

    Optional<RepeatedVoucher> findById(Long id);

    boolean existsById(Long id);

    void deleteById(Long id);

    ArrayList<RepeatedVoucher> findByEateryId (Long eateryId);

    @Query(value = "select * from repeated_voucher where eatery_id = ?1 and is_active = true", nativeQuery = true)
    ArrayList<RepeatedVoucher> findActiveByEateryId (Long eateryId);

    @Query(value = "select * from repeated_voucher where is_active = true", nativeQuery = true)
    ArrayList<RepeatedVoucher> findAllActive ();
}
