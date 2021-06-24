package com.nuggets.valueeats.repository;

import com.nuggets.valueeats.entity.Diner;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface DinerRepository extends JpaRepository<Diner, Integer>{
    public boolean existsByEmail(String email);

    public List<Diner> findByEmail(String email);

    @Query("select max(s.id) from Diner s")
    public Integer findMaxId();
    
}
