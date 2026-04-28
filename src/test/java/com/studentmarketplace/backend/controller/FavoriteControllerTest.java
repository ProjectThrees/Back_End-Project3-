package com.studentmarketplace.backend.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.studentmarketplace.backend.TestDataFactory;
import com.studentmarketplace.backend.model.Favorite;
import com.studentmarketplace.backend.model.Listing;
import com.studentmarketplace.backend.model.User;
import com.studentmarketplace.backend.service.FavoriteService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(FavoriteController.class)
@AutoConfigureMockMvc(addFilters = false)
class FavoriteControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private FavoriteService favoriteService;

    @Test
    void getAllFavoritesReturnsMappedDtos() throws Exception {
        User user = TestDataFactory.user(UUID.randomUUID(), "favorite@example.com");
        Listing listing = TestDataFactory.listing(UUID.randomUUID(), user);
        Favorite favorite = TestDataFactory.favorite(UUID.randomUUID(), user, listing);
        when(favoriteService.getAllFavorites()).thenReturn(List.of(favorite));

        mockMvc.perform(get("/favorites"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].listingId").value(listing.getListingId().toString()));
    }

    @Test
    void addFavoriteReturnsMappedDto() throws Exception {
        User user = TestDataFactory.user(UUID.randomUUID(), "favorite@example.com");
        Listing listing = TestDataFactory.listing(UUID.randomUUID(), user);
        Favorite favorite = TestDataFactory.favorite(UUID.randomUUID(), user, listing);
        when(favoriteService.addFavorite(eq(user.getUserId()), eq(listing.getListingId()))).thenReturn(favorite);

        mockMvc.perform(post("/favorites/{listingId}", listing.getListingId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(new FavoriteBody(user.getUserId()))))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.userId").value(user.getUserId().toString()));
    }

    @Test
    void deleteFavoriteReturnsNoContent() throws Exception {
        UUID userId = UUID.randomUUID();
        UUID listingId = UUID.randomUUID();
        doNothing().when(favoriteService).removeFavorite(userId, listingId);

        mockMvc.perform(delete("/favorites/{listingId}", listingId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(new FavoriteBody(userId))))
                .andExpect(status().isNoContent());
    }

    private record FavoriteBody(UUID userId) {
    }
}
