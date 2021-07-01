package com.nuggets.valueeats.repository;

import com.nuggets.valueeats.entity.User;
import org.springframework.context.annotation.Primary;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
@Primary
public interface UserRepository<T extends User> extends JpaRepository<T, Long> {
    boolean existsByEmail(String email);

    T findByEmail(String email);

    
    T findById(int id);

    boolean existsById(Long id);


    T findByToken(String token);

    boolean existsByToken(String token);

    @Query("select max(id) from User")
    Long findMaxId();

    @Modifying
    @Query("update User u set u.email = ?1, u.password = ?2, u.alias = ?3, u.address = ?4 where u.id = ?5")
    void updateUserById(String email, String password, String alias, String address, Long userId);
}
