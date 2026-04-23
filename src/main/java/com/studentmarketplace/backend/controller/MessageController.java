package com.studentmarketplace.backend.controller;

import com.studentmarketplace.backend.model.Message;
import com.studentmarketplace.backend.service.MessageService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;

/**
 * REST controller for Message endpoints.
 * Delegates all business logic to MessageService.
 *
 * @author Student Marketplace Team
 * @version 0.1.0
 */
@RestController
@RequestMapping("/api/messages")
public class MessageController {

    private final MessageService messageService;

    public MessageController(MessageService messageService) {
        this.messageService = messageService;
    }

    @GetMapping
    public ResponseEntity<List<Message>> getAllMessages() {
        return ResponseEntity.ok(messageService.getAllMessages());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Message> getMessageById(@PathVariable UUID id) {
        return messageService.getMessageById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/sent/{senderId}")
    public ResponseEntity<List<Message>> getMessagesBySender(@PathVariable UUID senderId) {
        try {
            return ResponseEntity.ok(messageService.getMessagesBySender(senderId));
        } catch (NoSuchElementException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/received/{receiverId}")
    public ResponseEntity<List<Message>> getMessagesByReceiver(@PathVariable UUID receiverId) {
        try {
            return ResponseEntity.ok(messageService.getMessagesByReceiver(receiverId));
        } catch (NoSuchElementException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/listing/{listingId}")
    public ResponseEntity<List<Message>> getMessagesByListing(@PathVariable UUID listingId) {
        return ResponseEntity.ok(messageService.getMessagesByListing(listingId));
    }

    @GetMapping("/conversation")
    public ResponseEntity<?> getConversation(@RequestParam UUID userId1, @RequestParam UUID userId2) {
        try {
            return ResponseEntity.ok(messageService.getConversation(userId1, userId2));
        } catch (NoSuchElementException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping
    public ResponseEntity<?> sendMessage(@RequestBody Message message) {
        try {
            Message sent = messageService.sendMessage(message);
            return ResponseEntity.status(HttpStatus.CREATED).body(sent);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (NoSuchElementException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMessage(@PathVariable UUID id) {
        try {
            messageService.deleteMessage(id);
            return ResponseEntity.noContent().build();
        } catch (NoSuchElementException e) {
            return ResponseEntity.notFound().build();
        }
    }
}
