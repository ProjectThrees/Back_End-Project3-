package com.studentmarketplace.backend.controller;

import com.studentmarketplace.backend.TestDataFactory;
import com.studentmarketplace.backend.model.User;
import com.studentmarketplace.backend.service.UserService;
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
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class UserControllerTest {

    @Mock
    private UserService userService;

    private UserController userController;

    @BeforeEach
    void setUp() {
        userController = new UserController(userService);
    }

    @Test
    void getAllUsers_returnsOk() {
        User user = TestDataFactory.user(UUID.randomUUID(), "user@example.com");
        when(userService.getAllUsers()).thenReturn(List.of(user));

        ResponseEntity<?> response = userController.getAllUsers();
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    void getUserById_returnsOk() {
        UUID userId = UUID.randomUUID();
        User user = TestDataFactory.user(userId, "lookup@example.com");
        when(userService.getUserById(userId)).thenReturn(Optional.of(user));

        ResponseEntity<?> response = userController.getUserById(userId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    void createUser_returnsOk() {
        User user = TestDataFactory.user(UUID.randomUUID(), "new@example.com");
        when(userService.createUser(any(User.class))).thenReturn(user);

        ResponseEntity<?> response = userController.createUser(
                new com.studentmarketplace.backend.dto.UserCreateRequestDto("Test User", "new@example.com", "USER", "ACTIVE")
        );

        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    void updateUser_returnsOk() {
        UUID userId = UUID.randomUUID();
        User user = TestDataFactory.user(userId, "updated@example.com");
        when(userService.updateUser(eq(userId), any(User.class))).thenReturn(user);

        ResponseEntity<?> response = userController.updateUser(
                userId,
                new com.studentmarketplace.backend.dto.UserUpdateRequestDto("Updated", "updated@example.com", "ADMIN", "ACTIVE")
        );

        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    void deleteUser_returnsNoContent() {
        UUID userId = UUID.randomUUID();
        doNothing().when(userService).deleteUser(userId);

        ResponseEntity<Void> response = userController.deleteUser(userId);
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
    }
}
