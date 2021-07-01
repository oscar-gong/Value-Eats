package com.nuggets.valueeats.repository;

import java.util.List;

import com.nuggets.valueeats.entity.Eatery;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface EateryRepository extends UserRepository<Eatery> {
    boolean existsById(Long id);

    @Query(value = "select * from Cuisines", nativeQuery = true)
    List findAllCuisines();
}
