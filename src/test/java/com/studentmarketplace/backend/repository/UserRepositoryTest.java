package com.studentmarketplace.backend.repository;

import com.studentmarketplace.backend.TestDataFactory;
import com.studentmarketplace.backend.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserRepositoryTest {

    @Mock
    private UserRepository userRepository;

    private UUID userId;
    private User user;

    @BeforeEach
    void setUp() {
        userId = UUID.randomUUID();
        user = TestDataFactory.user(userId, "repo@example.com");
    }

    @Test
    void findByEmailReturnsUser() {
        when(userRepository.findByEmail("repo@example.com")).thenReturn(Optional.of(user));

        Optional<User> result = userRepository.findByEmail("repo@example.com");

        assertTrue(result.isPresent());
        assertEquals(user.getUserId(), result.get().getUserId());
        verify(userRepository).findByEmail("repo@example.com");
    }

    @Test
    void findByRoleAndStatusReturnMatchingUsers() {
        User inactiveAdmin = TestDataFactory.user(UUID.randomUUID(), "admin@example.com");
        inactiveAdmin.setRole("ADMIN");
        inactiveAdmin.setStatus("INACTIVE");
        when(userRepository.findByRole("ADMIN")).thenReturn(List.of(inactiveAdmin));
        when(userRepository.findByStatus("INACTIVE")).thenReturn(List.of(inactiveAdmin));

        List<User> admins = userRepository.findByRole("ADMIN");
        List<User> inactiveUsers = userRepository.findByStatus("INACTIVE");

        assertEquals(1, admins.size());
        assertEquals(1, inactiveUsers.size());
        assertEquals("admin@example.com", admins.get(0).getEmail());
        verify(userRepository).findByRole("ADMIN");
        verify(userRepository).findByStatus("INACTIVE");
    }
}
