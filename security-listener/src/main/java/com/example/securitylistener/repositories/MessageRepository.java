package com.example.securitylistener.repositories;

import com.example.securitylistener.model.MessageEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MessageRepository extends JpaRepository<MessageEntity,Integer> {
}
