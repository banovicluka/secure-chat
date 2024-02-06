package com.example.securitylistener.repositories;

import com.example.securitylistener.model.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface UserRepository extends JpaRepository<UserEntity,Integer> {
    @Query("SELECT u.id FROM UserEntity u WHERE u.username = :username")
    Integer findIdByUsername(@Param("username") String username);

    UserEntity findByUsername(String username);

}
