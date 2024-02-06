package com.example.security.services.impl;

import com.example.security.MQConfig;
import com.example.security.crypto.Crypto;
import com.example.security.crypto.Steganography;
import com.example.security.entities.dto.Message;
import com.example.security.entities.dto.MessagePart;
import com.example.security.entities.dto.MessageRequest;
import com.example.security.repositories.MessageEntityRepository;
import com.example.security.services.MessageService;
import com.example.security.services.UserService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.modelmapper.ModelMapper;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.PublicKey;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class MessageServiceImpl implements MessageService {

    final
    MessageEntityRepository repository;
    ObjectMapper objectMapper = new ObjectMapper();
    UserService userService;
    @Autowired
    private RabbitTemplate template1;
    final
    ModelMapper modelMapper;

    private Steganography steganography;

    public MessageServiceImpl(MessageEntityRepository repository, ModelMapper modelMapper, UserService userService) {
        this.repository = repository;
        this.modelMapper = modelMapper;
        this.userService = userService;
        this.steganography = new Steganography();
    }

    @Override
    public List<Message> findAllMessagesOfUser(Integer id) {
        return this.repository.findAll().stream().filter(m -> m.getIdReciever() == id)
                .map(m -> modelMapper.map(m, Message.class)).collect(Collectors.toList());
    }


    @Override
    public void postMessage(MessageRequest messageRequest) throws Exception {
        String signature = Crypto.signMessage(userService.getPrivateKeyById(messageRequest.getSenderId()),messageRequest.getText());
        String encryptedMessage = Crypto.serializeAndEncryptWithAES(messageRequest.getText());

        PublicKey receiverPublicKey = Crypto.stringToPublicKey(userService.getPublicKeyById(messageRequest.receiverId));
        byte[] encryptedAesKey = Crypto.encryptAESKey(Crypto.getAesKey().getEncoded(),receiverPublicKey);
        MessagePart messagePart = new MessagePart();
        messagePart.setId(messageRequest.getId().toString()); //ID kompletne poruke kao string.
        messagePart.setSenderId(messageRequest.getSenderId());
        messagePart.setReceiverId(messageRequest.getReceiverId());
        messagePart.setSignature(signature);
        messagePart.setMessagePartText(encryptedMessage);
        messagePart.setTotalParts(messageRequest.getTotalParts());
        messagePart.setCurrentPart(messageRequest.getCurrentPart());
        messagePart.setEncryptedAesKey(encryptedAesKey);

        String partAsString = serializeMessagePartition(messagePart);

        template1.convertAndSend(MQConfig.EXCHANGE,MQConfig.ROUTING_KEY,partAsString);
        System.out.println("MessagePart sent to MQServer" + partAsString);

    }

    @Override
    public void postLastPartSteganography(MessageRequest messageRequest) throws Exception{
        String signature = Crypto.signMessage(userService.getPrivateKeyById(messageRequest.getSenderId()),messageRequest.getText());
        String encryptedMessage = Crypto.serializeAndEncryptWithAES(messageRequest.getText());
        PublicKey receiverPublicKey = Crypto.stringToPublicKey(userService.getPublicKeyById(messageRequest.receiverId));
        byte[] encryptedAesKey = Crypto.encryptAESKey(Crypto.getAesKey().getEncoded(),receiverPublicKey);
        MessagePart messagePart = new MessagePart();
        messagePart.setId(messageRequest.getId().toString()); //ID kompletne poruke kao string.
        messagePart.setSenderId(messageRequest.getSenderId());
        messagePart.setReceiverId(messageRequest.getReceiverId());
        messagePart.setSignature(signature);
        messagePart.setMessagePartText(encryptedMessage);
        messagePart.setTotalParts(messageRequest.getTotalParts());
        messagePart.setCurrentPart(messageRequest.getCurrentPart());
        messagePart.setEncryptedAesKey(encryptedAesKey);

        String partAsString = serializeMessagePartition(messagePart);

        steganography.hideDataInImage(partAsString,messageRequest.getId().toString());
        Path imagePath = Paths.get("src/main/resources/images/" + messageRequest.getId().toString() + ".png");
        try{
            byte[] imageBytes = Files.readAllBytes(imagePath);
            template1.convertAndSend(MQConfig.EXCHANGE,MQConfig.ROUTING_KEY2,imageBytes);
            System.out.println("Last part sent.");
            Files.delete(imagePath);
        }catch (IOException ex){
            ex.printStackTrace();
        }

    }


    private String serializeMessagePartition(MessagePart part) {
        try {
            return objectMapper.writeValueAsString(part);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Failed to serialize MessagePartition", e);
        }
    }
//    private String[] separateMessage(String message,Integer numberOfParts) {
//        Integer partLength = message.length()/numberOfParts;
//        String[] parts = new String[numberOfParts];
//        for(int i=0;i<numberOfParts;i++){
//            int startIndex = i*partLength;
//            int endIndex = (i == numberOfParts -1)? message.length():startIndex+partLength;
//            parts[i] = message.substring(startIndex,endIndex);
//        }
//        return parts;
//    }

    ///provjeri jos jednom bile izmjene,
    //bice problem sa prenosom aesa kod recievera, jer ce se razlikvoati, moracu i njega nekako prenijeti.
    //UUID messagePartId = UUID.randomUUID(); // mislim da mi ne treba ovaj dio, treba mi ID kompletne poruke
}
