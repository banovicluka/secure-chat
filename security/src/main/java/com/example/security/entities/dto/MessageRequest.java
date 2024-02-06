package com.example.security.entities.dto;

import lombok.Data;

@Data
public class MessageRequest {

    public Integer id;
    public String text;
    public Integer senderId;
    public Integer receiverId;

    public Integer totalParts;
    public Integer currentPart;
}
