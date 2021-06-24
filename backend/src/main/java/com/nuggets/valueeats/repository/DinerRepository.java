package com.nuggets.valueeats.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.nuggets.valueeats.entity.Diner;

public interface DinerRepository extends JpaRepository<Diner, Long> {

  // Jpa repository already has findAll, delete ...... methods, we just need to extends it.
}
