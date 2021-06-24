package com.nuggets.valueeats.repository;

import com.nuggets.valueeats.entity.Eatery;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;


@Repository
public interface EateryRepository extends JpaRepository<Eatery, Integer>{
    
}
