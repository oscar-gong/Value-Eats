// package com.nuggets.valueeats.repository;

// import com.nuggets.valueeats.entity.UserToken;
// import org.springframework.data.jpa.repository.JpaRepository;
// import org.springframework.data.jpa.repository.Query;
// import org.springframework.stereotype.Repository;

// @Repository
// public interface UserTokenRepository extends JpaRepository<UserToken, Long> {
//     void deleteByToken(String token);
//     boolean existsByToken(String token);

//     @Query("select max(id) from UserToken")
//     Long findMaxId();
// }
