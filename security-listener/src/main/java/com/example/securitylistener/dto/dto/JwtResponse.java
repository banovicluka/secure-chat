package com.example.securitylistener.dto.dto;

import lombok.Data;

@Data
public class JwtResponse {

    private String token;
    private String username;

    public JwtResponse(String token,String username) {
        this.token = token;
        this.username = username;
    }

}
