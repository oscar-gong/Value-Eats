package com.nuggets.valueeats.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.stereotype.Repository;

import com.nuggets.valueeats.entity.User;

import org.springframework.context.annotation.Primary;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

@Repository
@Primary
public interface UserBaseRepository<T extends User> extends JpaRepository<T, Integer> {
    
    public boolean existsByEmail(String email);

    public List<T> findByEmail(String email);

    
    @Query("select max(t.id) from User t")
    public Integer findMaxId();
}
