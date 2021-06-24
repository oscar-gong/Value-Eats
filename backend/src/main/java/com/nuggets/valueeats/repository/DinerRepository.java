package com.nuggets.valueeats.repository;

import com.nuggets.valueeats.entity.Diner;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;


@Repository
public interface DinerRepository extends JpaRepository<Diner, Integer>{
    
}
