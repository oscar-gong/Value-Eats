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
}