package com.nuggets.valueeats.repository;

import com.nuggets.valueeats.entity.Eatery;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;


@Repository
public interface EateryRepository extends UserBaseRepository<Eatery>{

    
}
