package com.nuggets.valueeats.repository;

import java.util.Optional;

import com.nuggets.valueeats.entity.BookingRecord;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface BookingRecordRepository extends JpaRepository<BookingRecord, Long> {

  @Query("select max(id) from BookingRecord")
  Long findMaxId();

  Optional<BookingRecord> findById(Long id);

  boolean existsById(Long id);
  
  boolean existsByCode(String code);

}