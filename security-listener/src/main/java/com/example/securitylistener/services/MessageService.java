package com.example.securitylistener.services;


import com.example.securitylistener.dto.Message;
import com.example.securitylistener.dto.dto.MessageRequest;

import java.util.List;

public interface MessageService {
    List<Message> findAllMessagesOfUser(Integer id);

    Message insert(Message message);

    //void postMessage(MessageRequest message) throws Exception;
}
