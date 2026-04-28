package com.studentmarketplace.backend.repository;

import com.studentmarketplace.backend.TestDataFactory;
import com.studentmarketplace.backend.model.Listing;
import com.studentmarketplace.backend.model.Message;
import com.studentmarketplace.backend.model.User;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class MessageRepositoryTest {

    @Mock
    private MessageRepository messageRepository;

    @Test
    void findBySenderAndReceiverAndListingIdReturnMatches() {
        User sender = TestDataFactory.user(UUID.randomUUID(), "sender@example.com");
        User receiver = TestDataFactory.user(UUID.randomUUID(), "receiver@example.com");
        Listing listing = TestDataFactory.listing(UUID.randomUUID(), sender);
        Message message = TestDataFactory.message(UUID.randomUUID(), sender, receiver, listing);
        when(messageRepository.findBySenderAndReceiver(sender, receiver)).thenReturn(List.of(message));
        when(messageRepository.findByListingListingId(listing.getListingId())).thenReturn(List.of(message));

        List<Message> byPair = messageRepository.findBySenderAndReceiver(sender, receiver);
        List<Message> byListing = messageRepository.findByListingListingId(listing.getListingId());

        assertEquals(1, byPair.size());
        assertEquals(1, byListing.size());
        assertEquals(message.getMessageId(), byPair.get(0).getMessageId());
        verify(messageRepository).findBySenderAndReceiver(sender, receiver);
        verify(messageRepository).findByListingListingId(listing.getListingId());
    }
}
