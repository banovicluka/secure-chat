package com.example.security.controllers;

import com.example.security.entities.dto.User;
import com.example.security.entities.exception.NotFoundException;
import com.example.security.entities.model.UserEntity;
import com.example.security.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin(origins = "https://localhost:4200")
@RequestMapping("/users")
public class UserController {

    final
    UserService service;

    public UserController(UserService service) {
        this.service = service;
    }

    @GetMapping
    public List<User> findAll(){
        return this.service.findAll();
    }

    @GetMapping("/{username}")
    public UserEntity findUserByUsername(@PathVariable String username){
        return this.service.findByUsername(username);
    }

    @GetMapping("/id/{id}")
    public User findUserById(@PathVariable Integer id) throws NotFoundException {
        return this.service.findById(id);
    }
}
