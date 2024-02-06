package com.example.security.services;

import com.example.security.entities.dto.User;
import com.example.security.entities.exception.NotFoundException;
import com.example.security.entities.model.UserEntity;
import org.aspectj.weaver.ast.Not;

import java.util.List;

public interface UserService {
    UserEntity findByUsername(String username);

    List<User> findAll();

    User insert(User userRequest) throws NotFoundException;

    User findById(Integer id) throws NotFoundException;

    public String getPrivateKeyById(Integer id) throws NotFoundException;
    public String getPublicKeyById(Integer id) throws NotFoundException;


}
