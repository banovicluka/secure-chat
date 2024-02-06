package com.example.security.services;

import com.example.security.entities.dto.Message;
import com.example.security.entities.dto.MessageRequest;
import com.example.security.entities.exception.NotFoundException;

import java.util.List;

public interface MessageService {
    List<Message> findAllMessagesOfUser(Integer id);

    void postMessage(MessageRequest message) throws Exception;

    void postLastPartSteganography(MessageRequest messageRequest) throws  Exception;
}
