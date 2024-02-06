package com.example.securitylistener;

import org.modelmapper.ModelMapper;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class SecurityListenerApplication {

    public static void main(String[] args) {
        SpringApplication.run(SecurityListenerApplication.class, args);
    }

    @Bean
    public ModelMapper modelMaper(){
        ModelMapper mapper = new ModelMapper();
        mapper.getConfiguration().setAmbiguityIgnored(true);
        return mapper;
    }
}
