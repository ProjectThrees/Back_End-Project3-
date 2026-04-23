package com.studentmarketplace.backend.service;

import com.studentmarketplace.backend.model.Listing;
import com.studentmarketplace.backend.model.Message;
import com.studentmarketplace.backend.model.User;
import com.studentmarketplace.backend.repository.ListingRepository;
import com.studentmarketplace.backend.repository.MessageRepository;
import com.studentmarketplace.backend.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.UUID;

/**
 * Service class for Message business logic.
 * Acts as an intermediary between MessageController and MessageRepository.
 *
 * @author Student Marketplace Team
 * @version 0.1.0
 */
@Service
public class MessageService {

    private final MessageRepository messageRepository;
    private final UserRepository userRepository;
    private final ListingRepository listingRepository;

    public MessageService(MessageRepository messageRepository,
                          UserRepository userRepository,
                          ListingRepository listingRepository) {
        this.messageRepository = messageRepository;
        this.userRepository = userRepository;
        this.listingRepository = listingRepository;
    }

    public List<Message> getAllMessages() {
        return messageRepository.findAll();
    }

    public Optional<Message> getMessageById(UUID id) {
        return messageRepository.findById(id);
    }

    public List<Message> getMessagesBySender(UUID senderId) {
        User sender = userRepository.findById(senderId)
                .orElseThrow(() -> new NoSuchElementException("User not found with id: " + senderId));
        return messageRepository.findBySender(sender);
    }

    public List<Message> getMessagesByReceiver(UUID receiverId) {
        User receiver = userRepository.findById(receiverId)
                .orElseThrow(() -> new NoSuchElementException("User not found with id: " + receiverId));
        return messageRepository.findByReceiver(receiver);
    }

    public List<Message> getMessagesByListing(UUID listingId) {
        return messageRepository.findByListingListingId(listingId);
    }

    public List<Message> getConversation(UUID userId1, UUID userId2) {
        // Returns all messages where userId1 is sender or receiver and userId2 is the other party
        User user1 = userRepository.findById(userId1)
                .orElseThrow(() -> new NoSuchElementException("User not found with id: " + userId1));
        User user2 = userRepository.findById(userId2)
                .orElseThrow(() -> new NoSuchElementException("User not found with id: " + userId2));

        List<Message> sent = messageRepository.findBySenderAndReceiver(user1, user2);
        List<Message> received = messageRepository.findBySenderAndReceiver(user2, user1);
        sent.addAll(received);
        return sent;
    }

    public Message sendMessage(Message message) {
        if (message.getSender() == null || message.getSender().getUserId() == null) {
            throw new IllegalArgumentException("Message must have a valid sender.");
        }
        if (message.getReceiver() == null || message.getReceiver().getUserId() == null) {
            throw new IllegalArgumentException("Message must have a valid receiver.");
        }
        if (message.getSender().getUserId().equals(message.getReceiver().getUserId())) {
            throw new IllegalArgumentException("Sender and receiver cannot be the same user.");
        }

        User sender = userRepository.findById(message.getSender().getUserId())
                .orElseThrow(() -> new NoSuchElementException("Sender not found with id: " + message.getSender().getUserId()));
        User receiver = userRepository.findById(message.getReceiver().getUserId())
                .orElseThrow(() -> new NoSuchElementException("Receiver not found with id: " + message.getReceiver().getUserId()));

        message.setSender(sender);
        message.setReceiver(receiver);

        if (message.getListing() != null && message.getListing().getListingId() != null) {
            Listing listing = listingRepository.findById(message.getListing().getListingId())
                    .orElseThrow(() -> new NoSuchElementException("Listing not found with id: " + message.getListing().getListingId()));
            message.setListing(listing);
        }

        message.setSentAt(LocalDateTime.now());
        return messageRepository.save(message);
    }

    public void deleteMessage(UUID id) {
        if (!messageRepository.existsById(id)) {
            throw new NoSuchElementException("Message not found with id: " + id);
        }
        messageRepository.deleteById(id);
    }
}
