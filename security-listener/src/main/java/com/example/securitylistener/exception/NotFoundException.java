package com.example.securitylistener.exception;

public class NotFoundException extends  Exception{

    public NotFoundException(){}

    public NotFoundException(String text){
        System.out.println(text);;
    }
}
