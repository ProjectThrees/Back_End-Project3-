package com.studentmarketplace.backend.controller;

import com.studentmarketplace.backend.model.Message;
import com.studentmarketplace.backend.service.MessageService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/messages")
public class MessageController {
    private final MessageService messageService;

    MessageController(MessageService messageService) {
        this.messageService = messageService;
    }

    //GET /messages
    @GetMapping
    public ResponseEntity<List<Message>> getAllMessages(){
        return ResponseEntity.ok(messageService.getAllMessages());
    }

    //GET /messages/{messageId}
    @GetMapping("/{messageId}")
    public ResponseEntity<Optional<Message>> getMessage(@PathVariable UUID messageId){
        return ResponseEntity.ok(messageService.getMessageById(messageId));
    }

    //POST /messages
    @PostMapping
    public ResponseEntity<Message> sendMessage(@RequestBody Message message){
        Message created = messageService.sendMessage(message);
        return ResponseEntity.ok(created);
    }

    //DELETE /messages/{messageId}
    @DeleteMapping("/{messageId}")
    public ResponseEntity<Void> deleteMessage(@PathVariable UUID messageId){
        messageService.deleteMessage(messageId);
        return ResponseEntity.noContent().build();
    }
}
