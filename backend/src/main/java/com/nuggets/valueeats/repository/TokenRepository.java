package com.nuggets.valueeats.repository;

import com.nuggets.valueeats.entity.Token;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface TokenRepository extends JpaRepository<Token, Long> {
    void deleteByToken(String token);
    boolean existsByToken(String token);

    @Query("select max(id) from Token")
    Long findMaxId();
}
