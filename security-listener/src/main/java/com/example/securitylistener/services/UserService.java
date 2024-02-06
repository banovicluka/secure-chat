package com.example.securitylistener.services;


import com.example.securitylistener.dto.User;
import com.example.securitylistener.exception.NotFoundException;

import java.util.List;

public interface UserService {
    List<User> findAll();

    User insert(User userRequest);

    User findById(Integer id);

    public String getPrivateKeyById(Integer id) throws NotFoundException;
    public String getPublicKeyById(Integer id) throws NotFoundException;
}
