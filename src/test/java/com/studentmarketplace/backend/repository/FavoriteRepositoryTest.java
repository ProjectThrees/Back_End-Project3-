package com.studentmarketplace.backend.repository;

import com.studentmarketplace.backend.TestDataFactory;
import com.studentmarketplace.backend.model.Favorite;
import com.studentmarketplace.backend.model.Listing;
import com.studentmarketplace.backend.model.User;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class FavoriteRepositoryTest {

    @Mock
    private FavoriteRepository favoriteRepository;

    @Test
    void existsFindAndDeleteByUserAndListingWork() {
        User user = TestDataFactory.user(UUID.randomUUID(), "favorite@example.com");
        Listing listing = TestDataFactory.listing(UUID.randomUUID(), user);
        Favorite favorite = TestDataFactory.favorite(UUID.randomUUID(), user, listing);
        when(favoriteRepository.existsByUserAndListing(user, listing)).thenReturn(true, false);
        when(favoriteRepository.findByUser(user)).thenReturn(List.of(favorite));

        assertTrue(favoriteRepository.existsByUserAndListing(user, listing));

        List<Favorite> byUser = favoriteRepository.findByUser(user);
        assertEquals(1, byUser.size());
        assertEquals(favorite.getFavoriteId(), byUser.get(0).getFavoriteId());

        favoriteRepository.deleteByUserAndListing(user, listing);
        assertFalse(favoriteRepository.existsByUserAndListing(user, listing));

        verify(favoriteRepository).findByUser(user);
        verify(favoriteRepository).deleteByUserAndListing(user, listing);
    }
}
