package com.nuggets.valueeats.repository;

import com.nuggets.valueeats.entity.Eatery;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EateryRepository extends UserRepository<Eatery> {
    boolean existsById(Long id);

    boolean existsByIdAndToken(Long id, String token);

    @Query(value = "select * from Cuisines", nativeQuery = true)
    List<Object> findAllCuisines();

    List<Eatery> findAllByCuisinesLike(String string);
}
