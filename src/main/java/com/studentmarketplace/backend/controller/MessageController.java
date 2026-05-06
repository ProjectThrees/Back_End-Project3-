package com.studentmarketplace.backend.controller;

import com.studentmarketplace.backend.dto.ApiMapper;
import com.studentmarketplace.backend.dto.MessageRequestDto;
import com.studentmarketplace.backend.dto.MessageResponseDto;
import com.studentmarketplace.backend.exception.NotFoundException;
import com.studentmarketplace.backend.service.MessageService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
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
    public ResponseEntity<List<MessageResponseDto>> getAllMessages(){
        return ResponseEntity.ok(messageService.getAllMessages().stream().map(ApiMapper::toMessageResponse).toList());
    }

    //GET /messages/{messageId}
    @GetMapping("/{messageId}")
    public ResponseEntity<MessageResponseDto> getMessage(@PathVariable UUID messageId){
        return ResponseEntity.ok(
                messageService.getMessageById(messageId)
                        .map(ApiMapper::toMessageResponse)
                        .orElseThrow(() -> new NotFoundException("Message not found with id: " + messageId))
        );
    }

    //POST /messages
    @PostMapping
    public ResponseEntity<MessageResponseDto> sendMessage(@RequestBody MessageRequestDto messageRequest){
        return ResponseEntity.ok(ApiMapper.toMessageResponse(messageService.sendMessage(ApiMapper.toMessage(messageRequest))));
    }

    //DELETE /messages/{messageId}
    @DeleteMapping("/{messageId}")
    public ResponseEntity<Void> deleteMessage(@PathVariable UUID messageId){
        messageService.deleteMessage(messageId);
        return ResponseEntity.noContent().build();
    }
}
