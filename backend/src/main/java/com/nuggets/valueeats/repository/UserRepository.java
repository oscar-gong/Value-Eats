package com.nuggets.valueeats.repository;

import com.nuggets.valueeats.entity.User;
import org.springframework.context.annotation.Primary;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@Primary
public interface UserRepository<T extends User> extends JpaRepository<T, Integer> {
    boolean existsByEmail(String email);

    List<T> findByEmail(String email);

    @Query("select max(id) from User")
    Integer findMaxId();
}
