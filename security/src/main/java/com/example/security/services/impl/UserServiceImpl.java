package com.example.security.services.impl;

import com.example.security.entities.dto.User;
import com.example.security.entities.exception.NotFoundException;
import com.example.security.entities.model.UserEntity;
import com.example.security.repositories.UserEntityRepository;
import com.example.security.services.UserService;
import org.aspectj.weaver.ast.Not;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {

    final
    UserEntityRepository repository;

    ModelMapper modelMapper;

    public UserServiceImpl(UserEntityRepository repository,ModelMapper modelMapper) {
        this.repository = repository;
        this.modelMapper = modelMapper;
    }

    @Override
    public UserEntity findByUsername(String username){
        return repository.findByUsername(username);
    }


    @Override
    public List<User> findAll(){
        System.out.println("asdd");
        return this.repository.findAll().stream().map(u -> modelMapper.map(u,User.class))
                .collect(Collectors.toList());
    }

    @Override
    public User insert(User userRequest) throws NotFoundException {
        UserEntity userEntity = modelMapper.map(userRequest,UserEntity.class);
        userEntity.setId(null);
        this.repository.saveAndFlush(userEntity);
        return this.findById(userEntity.getId());
    }


    @Override
    public User findById(Integer id) throws NotFoundException{
        System.out.println(this.repository.findById(id));
        return modelMapper.map(this.repository.findById(id).orElseThrow(NotFoundException::new),User.class);
    }
    public String getPrivateKeyById(Integer id) throws NotFoundException{
       return this.repository.findById(id).map(UserEntity::getPrivateKey).orElseThrow(() -> new NotFoundException("User not found"));
    }

    public String getPublicKeyById(Integer id) throws NotFoundException{
        return this.repository.findById(id).map(UserEntity::getPublicKey).orElseThrow(() -> new NotFoundException("User not found."));
    }
}
