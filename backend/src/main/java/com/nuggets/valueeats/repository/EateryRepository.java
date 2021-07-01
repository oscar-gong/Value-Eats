package com.nuggets.valueeats.repository;

import com.nuggets.valueeats.entity.Eatery;
import org.springframework.stereotype.Repository;

@Repository
public interface EateryRepository extends UserRepository<Eatery> {
    boolean existsById(Long id);
}
