package com.example.securitylistener.services.impl;

import com.example.securitylistener.dto.Message;
import com.example.securitylistener.model.MessageEntity;
import com.example.securitylistener.repositories.MessageRepository;
import com.example.securitylistener.services.MessageService;
import com.example.securitylistener.services.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.modelmapper.ModelMapper;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class MessageServiceImpl implements MessageService {

    final
    MessageRepository repository;
    ObjectMapper objectMapper = new ObjectMapper();
    UserService userService;
    @Autowired
    private RabbitTemplate template1;
    final
    ModelMapper modelMapper;

    public MessageServiceImpl(MessageRepository repository, ModelMapper modelMapper, UserService userService) {
        this.repository = repository;
        this.modelMapper = modelMapper;
        this.userService = userService;
    }

    @Override
    public List<Message> findAllMessagesOfUser(Integer id) {
        return this.repository.findAll().stream().filter(m -> m.getIdReciever() == id)
                .map(m -> modelMapper.map(m, Message.class)).collect(Collectors.toList());
    }

    @Override
    public Message insert(Message message){
        MessageEntity messageEntity = modelMapper.map(message , MessageEntity.class);
        messageEntity = this.repository.saveAndFlush(messageEntity);
        return modelMapper.map(this.repository.findById(messageEntity.getId()),Message.class);
    }

}
