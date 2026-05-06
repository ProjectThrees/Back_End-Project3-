package com.studentmarketplace.backend.service;

import com.studentmarketplace.backend.TestDataFactory;
import com.studentmarketplace.backend.exception.ConflictException;
import com.studentmarketplace.backend.exception.NotFoundException;
import com.studentmarketplace.backend.model.User;
import com.studentmarketplace.backend.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;

    private UUID userId;
    private User user;

    @BeforeEach
    void setUp() {
        userId = UUID.randomUUID();
        user = TestDataFactory.user(userId, "user@example.com");
    }

    @Test
    void createUserSetsDefaultsAndCreatedAt() {
        User newUser = new User();
        newUser.setName("New User");
        newUser.setEmail("new@example.com");

        when(userRepository.existsByEmail("new@example.com")).thenReturn(false);
        when(userRepository.save(any(User.class))).thenAnswer(invocation -> invocation.getArgument(0));

        User created = userService.createUser(newUser);

        assertEquals("USER", created.getRole());
        assertEquals("ACTIVE", created.getStatus());
        assertNotNull(created.getCreatedAt());
        verify(userRepository).save(newUser);
    }

    @Test
    void createUserRejectsDuplicateEmail() {
        when(userRepository.existsByEmail(user.getEmail())).thenReturn(true);

        assertThrows(ConflictException.class, () -> userService.createUser(user));
        verify(userRepository, never()).save(any());
    }

    @Test
    void updateUserUpdatesExistingUser() {
        User updated = new User();
        updated.setName("Updated");
        updated.setEmail("updated@example.com");
        updated.setRole("ADMIN");
        updated.setStatus("INACTIVE");

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(userRepository.save(any(User.class))).thenAnswer(invocation -> invocation.getArgument(0));

        User result = userService.updateUser(userId, updated);

        assertEquals("Updated", result.getName());
        assertEquals("updated@example.com", result.getEmail());
        assertEquals("ADMIN", result.getRole());
        assertEquals("INACTIVE", result.getStatus());
    }

    @Test
    void updateUserThrowsWhenMissing() {
        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> userService.updateUser(userId, user));
    }

    @Test
    void deleteUserDeletesExistingUser() {
        when(userRepository.existsById(userId)).thenReturn(true);

        userService.deleteUser(userId);

        verify(userRepository).deleteById(userId);
    }

    @Test
    void deleteUserThrowsWhenMissing() {
        when(userRepository.existsById(userId)).thenReturn(false);

        assertThrows(NotFoundException.class, () -> userService.deleteUser(userId));
        verify(userRepository, never()).deleteById(any());
    }
}
