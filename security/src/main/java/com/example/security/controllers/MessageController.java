package com.example.security.controllers;

import com.example.security.entities.dto.Message;
import com.example.security.entities.dto.MessageRequest;
import com.example.security.entities.exception.NotFoundException;
import com.example.security.services.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin(origins = "https://localhost:4200")
@RequestMapping("/messages")
public class MessageController {

    final
    MessageService service;

    public MessageController(MessageService service) {
        this.service = service;
    }

    @GetMapping("/{id}")
    public List<Message> findAllMessagesOfUser(@PathVariable Integer id){
        return this.service.findAllMessagesOfUser(id);
    }

    @PostMapping
    public ResponseEntity<String> postPartMessage(@RequestBody MessageRequest messageRequest) throws Exception {
        this.service.postMessage(messageRequest);
        return ResponseEntity.ok("Dio poruke poslan");
    }

    @PostMapping("/last-part")
    public ResponseEntity<String> postLastPartMessage(@RequestBody MessageRequest messageRequest) throws Exception{
        this.service.postLastPartSteganography(messageRequest);
        return ResponseEntity.ok("Zadnji dio poruke poslan.");
    }
}
