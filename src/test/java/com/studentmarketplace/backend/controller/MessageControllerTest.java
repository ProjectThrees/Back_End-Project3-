package com.studentmarketplace.backend.controller;

import com.studentmarketplace.backend.TestDataFactory;
import com.studentmarketplace.backend.dto.MessageRequestDto;
import com.studentmarketplace.backend.model.Listing;
import com.studentmarketplace.backend.model.Message;
import com.studentmarketplace.backend.model.User;
import com.studentmarketplace.backend.service.MessageService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class MessageControllerTest {

    @Mock
    private MessageService messageService;

    private MessageController messageController;

    @BeforeEach
    void setUp() {
        messageController = new MessageController(messageService);
    }

    @Test
    void getAllMessages_returnsOk() {
        User sender = TestDataFactory.user(UUID.randomUUID(), "sender@example.com");
        User receiver = TestDataFactory.user(UUID.randomUUID(), "receiver@example.com");
        Listing listing = TestDataFactory.listing(UUID.randomUUID(), sender);
        Message message = TestDataFactory.message(UUID.randomUUID(), sender, receiver, listing);
        when(messageService.getAllMessages()).thenReturn(List.of(message));

        ResponseEntity<?> response = messageController.getAllMessages();
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    void getMessage_returnsOk() {
        User sender = TestDataFactory.user(UUID.randomUUID(), "sender@example.com");
        User receiver = TestDataFactory.user(UUID.randomUUID(), "receiver@example.com");
        Listing listing = TestDataFactory.listing(UUID.randomUUID(), sender);
        Message message = TestDataFactory.message(UUID.randomUUID(), sender, receiver, listing);
        when(messageService.getMessageById(message.getMessageId())).thenReturn(Optional.of(message));

        ResponseEntity<?> response = messageController.getMessage(message.getMessageId());
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    void sendMessage_returnsOk() {
        User sender = TestDataFactory.user(UUID.randomUUID(), "sender@example.com");
        User receiver = TestDataFactory.user(UUID.randomUUID(), "receiver@example.com");
        Listing listing = TestDataFactory.listing(UUID.randomUUID(), sender);
        Message message = TestDataFactory.message(UUID.randomUUID(), sender, receiver, listing);
        when(messageService.sendMessage(any(Message.class))).thenReturn(message);

        ResponseEntity<?> response = messageController.sendMessage(
                new MessageRequestDto(sender.getUserId(), receiver.getUserId(), listing.getListingId(), "Is this still available?")
        );
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    void deleteMessage_returnsNoContent() {
        UUID messageId = UUID.randomUUID();
        doNothing().when(messageService).deleteMessage(messageId);

        ResponseEntity<Void> response = messageController.deleteMessage(messageId);
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
    }
}
