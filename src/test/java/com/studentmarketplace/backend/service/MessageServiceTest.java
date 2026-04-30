package com.studentmarketplace.backend.service;

import com.studentmarketplace.backend.TestDataFactory;
import com.studentmarketplace.backend.model.Listing;
import com.studentmarketplace.backend.model.Message;
import com.studentmarketplace.backend.model.User;
import com.studentmarketplace.backend.repository.ListingRepository;
import com.studentmarketplace.backend.repository.MessageRepository;
import com.studentmarketplace.backend.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MessageServiceTest {

    @Mock
    private MessageRepository messageRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private ListingRepository listingRepository;

    @InjectMocks
    private MessageService messageService;

    private UUID senderId;
    private UUID receiverId;
    private UUID listingId;
    private User sender;
    private User receiver;
    private Listing listing;
    private Message message;

    @BeforeEach
    void setUp() {
        senderId = UUID.randomUUID();
        receiverId = UUID.randomUUID();
        listingId = UUID.randomUUID();
        sender = TestDataFactory.user(senderId, "sender@example.com");
        receiver = TestDataFactory.user(receiverId, "receiver@example.com");
        listing = TestDataFactory.listing(listingId, sender);
        message = TestDataFactory.message(UUID.randomUUID(), sender, receiver, listing);
    }

    @Test
    void sendMessageRejectsSameSenderAndReceiver() {
        Message invalid = new Message();
        invalid.setSender(sender);
        invalid.setReceiver(sender);

        assertThrows(IllegalArgumentException.class, () -> messageService.sendMessage(invalid));
    }

    @Test
    void sendMessageResolvesEntitiesAndSetsSentAt() {
        when(userRepository.findById(senderId)).thenReturn(Optional.of(sender));
        when(userRepository.findById(receiverId)).thenReturn(Optional.of(receiver));
        when(listingRepository.findById(listingId)).thenReturn(Optional.of(listing));
        when(messageRepository.save(any(Message.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Message sent = messageService.sendMessage(message);

        assertEquals(senderId, sent.getSender().getUserId());
        assertEquals(receiverId, sent.getReceiver().getUserId());
        assertEquals(listingId, sent.getListing().getListingId());
        assertNotNull(sent.getSentAt());
    }

    @Test
    void getConversationCombinesBothDirections() {
        Message reverse = TestDataFactory.message(UUID.randomUUID(), receiver, sender, listing);

        when(userRepository.findById(senderId)).thenReturn(Optional.of(sender));
        when(userRepository.findById(receiverId)).thenReturn(Optional.of(receiver));
        when(messageRepository.findBySenderAndReceiver(sender, receiver)).thenReturn(List.of(message));
        when(messageRepository.findBySenderAndReceiver(receiver, sender)).thenReturn(List.of(reverse));

        List<Message> conversation = messageService.getConversation(senderId, receiverId);

        assertEquals(2, conversation.size());
    }

    @Test
    void deleteMessageThrowsWhenMissing() {
        UUID messageId = UUID.randomUUID();
        when(messageRepository.existsById(messageId)).thenReturn(false);

        assertThrows(NoSuchElementException.class, () -> messageService.deleteMessage(messageId));
    }
}
