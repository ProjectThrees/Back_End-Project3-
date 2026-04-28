package com.studentmarketplace.backend.repository;

import com.studentmarketplace.backend.TestDataFactory;
import com.studentmarketplace.backend.model.Listing;
import com.studentmarketplace.backend.model.Message;
import com.studentmarketplace.backend.model.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DataJpaTest
class MessageRepositoryTest {

    @Autowired
    private MessageRepository messageRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ListingRepository listingRepository;

    @Test
    void findBySenderAndReceiverAndListingIdReturnMatches() {
        User sender = userRepository.save(TestDataFactory.user(UUID.randomUUID(), "sender@example.com"));
        User receiver = userRepository.save(TestDataFactory.user(UUID.randomUUID(), "receiver@example.com"));
        Listing listing = listingRepository.save(TestDataFactory.listing(UUID.randomUUID(), sender));
        Message message = messageRepository.save(TestDataFactory.message(UUID.randomUUID(), sender, receiver, listing));

        List<Message> byPair = messageRepository.findBySenderAndReceiver(sender, receiver);
        List<Message> byListing = messageRepository.findByListingListingId(listing.getListingId());

        assertEquals(1, byPair.size());
        assertEquals(1, byListing.size());
        assertEquals(message.getMessageId(), byPair.get(0).getMessageId());
    }
}
