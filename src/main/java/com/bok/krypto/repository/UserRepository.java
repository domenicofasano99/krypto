package com.bok.krypto.repository;

import com.bok.krypto.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface UserRepository extends JpaRepository<User, Long> {

    @Query("select email from User where id=:userId")
    String findEmailById(@Param("userId") Long userId);

}
