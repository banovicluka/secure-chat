package com.example.security.repositories;

import com.example.security.entities.model.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface UserEntityRepository extends JpaRepository<UserEntity,Integer> {

    @Query("SELECT u.id FROM UserEntity u WHERE u.username = :username")
    Integer findIdByUsername(@Param("username") String username);

    UserEntity findByUsername(String username);
    Optional<UserEntity> findById(Integer integer);
}
