package com.example.securitylistener.dto;


import lombok.Data;

@Data
public class Message {

    private Integer id;

    private String content;

    private Integer idSender;

    private Integer idReciever;
}
