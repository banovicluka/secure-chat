package com.example.securitylistener.services.impl;


import com.example.securitylistener.dto.User;
import com.example.securitylistener.exception.NotFoundException;
import com.example.securitylistener.model.UserEntity;
import com.example.securitylistener.repositories.UserRepository;
import com.example.securitylistener.services.UserService;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {

    final
    UserRepository repository;

    ModelMapper modelMapper;

    public UserServiceImpl(UserRepository repository,ModelMapper modelMapper) {
        this.repository = repository;
        this.modelMapper = modelMapper;
    }

    public UserEntity findByUsername(String username){
        return repository.findByUsername(username);
    }

    @Override
    public List<User> findAll(){
        System.out.println("asdd");
        return this.repository.findAll().stream().map(u -> modelMapper.map(u, User.class))
                .collect(Collectors.toList());
    }

    @Override
    public User insert(User userRequest) {
        UserEntity userEntity = modelMapper.map(userRequest,UserEntity.class);
        userEntity.setId(null);
        this.repository.saveAndFlush(userEntity);
        return this.findById(userEntity.getId());
    }

    @Override
    public User findById(Integer id) {
        return modelMapper.map(this.repository.findById(id),User.class);
    }
    public String getPrivateKeyById(Integer id) throws NotFoundException {
       return this.repository.findById(id).map(UserEntity::getPrivateKey).orElseThrow(() -> new NotFoundException("User not found"));
    }

    public String getPublicKeyById(Integer id) throws NotFoundException{
        return this.repository.findById(id).map(UserEntity::getPublicKey).orElseThrow(() -> new NotFoundException("User not found."));
    }
}
