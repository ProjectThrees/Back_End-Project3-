package com.studentmarketplace.backend.repository;

import com.studentmarketplace.backend.TestDataFactory;
import com.studentmarketplace.backend.model.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @Test
    void findByEmailReturnsUser() {
        User user = userRepository.save(TestDataFactory.user(UUID.randomUUID(), "repo@example.com"));

        Optional<User> result = userRepository.findByEmail("repo@example.com");

        assertTrue(result.isPresent());
        assertEquals(user.getUserId(), result.get().getUserId());
    }

    @Test
    void findByRoleAndStatusReturnMatchingUsers() {
        userRepository.save(TestDataFactory.user(UUID.randomUUID(), "active@example.com"));

        User inactiveAdmin = TestDataFactory.user(UUID.randomUUID(), "admin@example.com");
        inactiveAdmin.setRole("ADMIN");
        inactiveAdmin.setStatus("INACTIVE");
        userRepository.save(inactiveAdmin);

        List<User> admins = userRepository.findByRole("ADMIN");
        List<User> inactiveUsers = userRepository.findByStatus("INACTIVE");

        assertEquals(1, admins.size());
        assertEquals(1, inactiveUsers.size());
        assertEquals("admin@example.com", admins.get(0).getEmail());
    }
}
