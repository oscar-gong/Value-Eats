package com.nuggets.valueeats.repository;

import com.nuggets.valueeats.entity.User;
import org.springframework.context.annotation.Primary;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
@Primary
public interface UserRepository<T extends User> extends JpaRepository<T, Long> {
    boolean existsByEmail(String email);

    T findByEmail(String email);

    
    T findById(int id);

    T findByToken(String token);

    boolean existsByToken(String token);

    @Query("select max(id) from User")
    Long findMaxId();

    @Modifying
    @Query("update User u set u.token = ?1 where u.token = ?2")
    void setTokenByEmail(String token, String email);
}
