package com.studentmarketplace.backend.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.studentmarketplace.backend.TestDataFactory;
import com.studentmarketplace.backend.model.Listing;
import com.studentmarketplace.backend.model.User;
import com.studentmarketplace.backend.service.ListingService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ListingController.class)
@AutoConfigureMockMvc(addFilters = false)
class ListingControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private ListingService listingService;

    @Test
    void getAllListingsReturnsMappedDtos() throws Exception {
        User user = TestDataFactory.user(UUID.randomUUID(), "seller@example.com");
        Listing listing = TestDataFactory.listing(UUID.randomUUID(), user);
        when(listingService.getAllListings()).thenReturn(List.of(listing));

        mockMvc.perform(get("/listings"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].title").value("Laptop"));
    }

    @Test
    void getListingReturnsMappedDto() throws Exception {
        User user = TestDataFactory.user(UUID.randomUUID(), "seller@example.com");
        Listing listing = TestDataFactory.listing(UUID.randomUUID(), user);
        when(listingService.getListingById(listing.getListingId())).thenReturn(Optional.of(listing));

        mockMvc.perform(get("/listings/{listingId}", listing.getListingId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Laptop"));
    }

    @Test
    void createListingReturnsCreatedDto() throws Exception {
        User user = TestDataFactory.user(UUID.randomUUID(), "seller@example.com");
        Listing listing = TestDataFactory.listing(UUID.randomUUID(), user);
        when(listingService.createListing(any(Listing.class))).thenReturn(listing);

        mockMvc.perform(post("/listings")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(new ListingBody(user.getUserId(), "Laptop", "Good condition", new BigDecimal("499.99"), "Electronics", "Used", "https://example.com/laptop.png", false))))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.userId").value(user.getUserId().toString()));
    }

    @Test
    void markAsSoldReturnsUpdatedDto() throws Exception {
        User user = TestDataFactory.user(UUID.randomUUID(), "seller@example.com");
        Listing listing = TestDataFactory.listing(UUID.randomUUID(), user);
        listing.setIsSold(true);
        when(listingService.markAsSold(listing.getListingId())).thenReturn(listing);

        mockMvc.perform(patch("/listings/{listingId}/sold", listing.getListingId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.isSold").value(true));
    }

    @Test
    void deleteListingReturnsNoContent() throws Exception {
        UUID listingId = UUID.randomUUID();
        doNothing().when(listingService).deleteListing(listingId);

        mockMvc.perform(delete("/listings/{listingId}", listingId))
                .andExpect(status().isNoContent());
    }

    private record ListingBody(
            UUID userId,
            String title,
            String description,
            BigDecimal price,
            String category,
            String condition,
            String imageUrl,
            Boolean isSold
    ) {
    }
}
