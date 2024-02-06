package com.example.security.entities.dto;


import lombok.Data;

@Data
public class User {

    private Integer id;

    private String username;

    private String password;

    private String role;

    private String privateKey;

    private String publicKey;

}
