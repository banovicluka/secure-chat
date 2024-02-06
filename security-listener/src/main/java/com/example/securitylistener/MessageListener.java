package com.example.securitylistener;

import com.example.securitylistener.crypto.Crypto;
import com.example.securitylistener.crypto.Steganography;
import com.example.securitylistener.dto.Message;
import com.example.securitylistener.dto.MessagePart;
import com.example.securitylistener.exception.NotFoundException;
import com.example.securitylistener.services.MessageService;
import com.example.securitylistener.services.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.PrivateKey;
import java.util.*;

@Component
public class MessageListener {

    ObjectMapper objectMapper = new ObjectMapper();
    private final Object lock = new Object();
    private final Map<String, List<MessagePart>> messagesMap = new HashMap<>();

    Steganography steganography = new Steganography();

    private UserService userService;
    private MessageService messageService;

    public MessageListener(UserService userService,MessageService messageService) {
        this.userService = userService;
        this.messageService = messageService;
    }

    @RabbitListener(queues = MQConfig.QUEUE)
    public void listener(String messagePart) throws Exception{
        byte[] messagePartBytes = messagePart.getBytes(StandardCharsets.UTF_8);
        MessagePart messagePart1 = deserializeMessagePart(messagePartBytes);
        processMessagePartFromMq(messagePart1);
    }

    @RabbitListener(queues = MQConfig.QUEUE2)
    public void listener2(String lastMessagePart) throws Exception{
        byte[] imageBytes = lastMessagePart.getBytes(StandardCharsets.UTF_8);
        Path imagePath = Paths.get("src/main/resources/images/image.jpg");
        try{
            Files.write(imagePath,imageBytes);
        }catch (IOException ex){
            ex.printStackTrace();
        }
    }

    private void processMessagePartFromMq(MessagePart messagePart) throws Exception{
        System.out.println("Received message" + messagePart.getId() + " from server.") ;

        PrivateKey receiverPrivateKey = Crypto.stringToPrivateKey(userService.getPrivateKeyById(messagePart.getReceiverId()));
        byte[] aesKey = Crypto.decryptAESKey(messagePart.getEncryptedAesKey(),receiverPrivateKey);
        SecretKey secretKey = new SecretKeySpec(aesKey,"AES");
        Crypto.setAESKey(secretKey);
        messagePart.setMessagePartText(Crypto.decryptAndDeserializeWithAES(messagePart.getMessagePartText()));
        putMessagePartIntoMap(messagePart);
        List<MessagePart> listOfParts = messagesMap.get(messagePart.getId());
        try{
            if(!Crypto.verifyMessageSignature(
                    userService.getPublicKeyById(messagePart.getSenderId()),
                    messagePart.getMessagePartText(),
                    messagePart.getSignature())
            ){
                System.out.println("Signature verification failed!");
            }else{
                System.out.println("Verification OK");
                if(listOfParts != null && listOfParts.size() == messagePart.getTotalParts()){
                    System.out.println("Stigli su svi dijelovi");

                    String assembledDecryptedMessage = assembleMessage(listOfParts);
                    System.out.println("Poruka je:" + assembledDecryptedMessage);
                    Message message = new Message();
                    message.setContent(assembledDecryptedMessage);
                    message.setIdReciever(messagePart.getReceiverId());
                    message.setIdSender(messagePart.getSenderId());

                    processMessage(message);
                }

            }

        }catch (NotFoundException ex){
            throw new RuntimeException(ex);
        }

    }

    private void processMessage(Message message) {
        messageService.insert(message);
    }

    private String assembleMessage(List<MessagePart> listOfParts) {
        StringBuilder fullMessageContent = new StringBuilder();
        listOfParts.sort(Comparator.comparingInt(MessagePart::getCurrentPart));
        System.out.println("PORUKA");
        for(MessagePart messagePart: listOfParts){
            System.out.println(messagePart.getMessagePartText());
            fullMessageContent.append(messagePart.getMessagePartText());
        }
        return fullMessageContent.toString();
    }

    private MessagePart deserializeMessagePart(byte[] bytes){
       try{
           return objectMapper.readValue(bytes,MessagePart.class);
       }catch (Exception ex){
           throw new RuntimeException("Failed to deserialize message.",ex);
       }
    }

    public void putMessagePartIntoMap(MessagePart messagePart) {
        synchronized (lock){
            messagesMap.computeIfAbsent(messagePart.getId(), k -> new ArrayList<>()).add(messagePart);
        }
    }

    //                    System.out.println("Dodajemo poslednji dio iz steganografije.");
//                    String lastPartString = steganography.retrieveDataFromImage(messagePart.getId());
//                    byte[] lastPartBytes = lastPartString.getBytes(StandardCharsets.UTF_8);
//                    MessagePart lastPartMessage = deserializeMessagePart(lastPartBytes);
//                    System.out.println(lastPartMessage);
//                    listOfParts.add(messagePart);

}
