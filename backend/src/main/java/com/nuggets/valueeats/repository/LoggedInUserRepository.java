package com.nuggets.valueeats.repository;

import com.nuggets.valueeats.entity.Eatery;
import com.nuggets.valueeats.entity.LoggedInUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LoggedInUserRepository extends JpaRepository<LoggedInUser, Long> {

}
