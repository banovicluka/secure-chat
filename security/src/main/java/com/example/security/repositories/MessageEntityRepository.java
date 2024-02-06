package com.example.security.repositories;

import com.example.security.entities.model.MessageEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MessageEntityRepository extends JpaRepository<MessageEntity,Integer> {
}
