package com.nuggets.valueeats.repository;

import com.nuggets.valueeats.entity.Review;

import org.springframework.context.annotation.Primary;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Long> {
  @Query("select max(id) from Review")
  Long findMaxId();

  @Query(
    value = "select exists(select * from Review where diner_id = ?1 and eatery_id = ?2)",
    nativeQuery = true)
  int existsByDinerIdAndEateryId(Long dinerId, Long eateryId);
}