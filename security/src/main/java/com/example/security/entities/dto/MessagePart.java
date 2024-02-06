package com.example.security.entities.dto;

import lombok.Data;

@Data
public class MessagePart {

    private String id;
    private Integer receiverId;
    private Integer senderId;
    private String signature;
    private String messagePartText;
    private Integer totalParts;
    private Integer currentPart;
    private byte[] encryptedAesKey;

}
