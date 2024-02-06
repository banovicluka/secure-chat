package com.example.security.entities.exception;

public class NotFoundException extends  Exception{

    public NotFoundException(){}

    public NotFoundException(String text){
        System.out.println(text);;
    }
}
