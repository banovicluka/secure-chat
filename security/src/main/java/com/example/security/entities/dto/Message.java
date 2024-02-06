package com.example.security.entities.dto;


import lombok.Data;

@Data
public class Message {

    private Integer id;

    private String content;

    private Integer idSender;

    private Integer idReciever;

}
