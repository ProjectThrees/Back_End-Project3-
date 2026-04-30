package com.studentmarketplace.backend.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.studentmarketplace.backend.TestDataFactory;
import com.studentmarketplace.backend.model.Listing;
import com.studentmarketplace.backend.model.Message;
import com.studentmarketplace.backend.model.User;
import com.studentmarketplace.backend.service.MessageService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(MessageController.class)
@AutoConfigureMockMvc(addFilters = false)
class MessageControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private MessageService messageService;

    @Test
    void getAllMessagesReturnsMappedDtos() throws Exception {
        User sender = TestDataFactory.user(UUID.randomUUID(), "sender@example.com");
        User receiver = TestDataFactory.user(UUID.randomUUID(), "receiver@example.com");
        Listing listing = TestDataFactory.listing(UUID.randomUUID(), sender);
        Message message = TestDataFactory.message(UUID.randomUUID(), sender, receiver, listing);
        when(messageService.getAllMessages()).thenReturn(List.of(message));

        mockMvc.perform(get("/messages"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].content").value("Is this still available?"));
    }

    @Test
    void sendMessageReturnsCreatedDto() throws Exception {
        User sender = TestDataFactory.user(UUID.randomUUID(), "sender@example.com");
        User receiver = TestDataFactory.user(UUID.randomUUID(), "receiver@example.com");
        Listing listing = TestDataFactory.listing(UUID.randomUUID(), sender);
        Message message = TestDataFactory.message(UUID.randomUUID(), sender, receiver, listing);
        when(messageService.sendMessage(any(Message.class))).thenReturn(message);

        mockMvc.perform(post("/messages")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(new MessageBody(sender.getUserId(), receiver.getUserId(), listing.getListingId(), "Is this still available?"))))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.senderId").value(sender.getUserId().toString()));
    }

    @Test
    void deleteMessageReturnsNoContent() throws Exception {
        UUID messageId = UUID.randomUUID();
        doNothing().when(messageService).deleteMessage(messageId);

        mockMvc.perform(delete("/messages/{messageId}", messageId))
                .andExpect(status().isNoContent());
    }

    private record MessageBody(UUID senderId, UUID receiverId, UUID listingId, String content) {
    }
}
