package com.example.security.controllers;

import com.example.security.crypto.Crypto;
import com.example.security.entities.dto.JwtResponse;
import com.example.security.entities.dto.LoginRequest;
import com.example.security.entities.dto.User;
import com.example.security.entities.exception.NotFoundException;
import com.example.security.security.JwtGenerator;

import com.example.security.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.security.KeyPair;

@RestController
@CrossOrigin(origins = "https://localhost:4200")
@RequestMapping("/api")
public class AuthController {

    AuthenticationManager authenticationManager;
    private final PasswordEncoder passwordEncoder;
    JwtGenerator jwtGenerator;

    UserService userService;

    @Autowired
    public AuthController(AuthenticationManager authenticationManager, JwtGenerator jwtGenerator, PasswordEncoder passwordEncoder, UserService userService){
        this.authenticationManager=authenticationManager;
        this.jwtGenerator=jwtGenerator;
        this.passwordEncoder = passwordEncoder;
        this.userService = userService;
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest) {
        try {
            System.out.println(loginRequest.getUsername()+ " " + loginRequest.getPassword());
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword())
            );
            System.out.println("a");
            String token = jwtGenerator.generateToken(authentication);
            System.out.println("b");
            System.out.println(token);
            //return ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON).body(new JwtResponse(token));
            return  ResponseEntity.ok(new JwtResponse(token,loginRequest.username));
        } catch (AuthenticationException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid username or password");
        }
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody User userRequest) throws NotFoundException {
        //obradi ako je korisnicko ime zauzeto
        userRequest.setRole("USER");
        userRequest.setPassword(passwordEncoder.encode(userRequest.getPassword()));
        KeyPair keyPair = Crypto.generateRSAKeyPair();
        String privateKey = Crypto.keyToString(keyPair.getPrivate());
        String publicKey = Crypto.keyToString(keyPair.getPublic());
        System.out.println(privateKey);
        System.out.println(publicKey);
        userRequest.setPrivateKey(privateKey);
        userRequest.setPublicKey(publicKey);
        userService.insert(userRequest);
        return new ResponseEntity<>("Korisnik je registrovan", HttpStatus.OK);
    }
}
