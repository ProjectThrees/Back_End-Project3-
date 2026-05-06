package com.studentmarketplace.backend.service;

import com.studentmarketplace.backend.repository.UserRepository;
import com.studentmarketplace.backend.model.User;
import com.studentmarketplace.backend.exception.BadRequestException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class OAuthUserService {
    private final UserRepository userRepository;

    public OAuthUserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User proccessOAuthUser(String provider, OAuth2User oauthUser) {
        String email = null;
        String name = null;
        String providerId = null;

        if("google".equals(provider)) {
            email = oauthUser.getAttribute("email");
            name = oauthUser.getAttribute("name");
            providerId = oauthUser.getAttribute("sub");
        } else if("github".equals(provider)) {
            email = oauthUser.getAttribute("email");
            name = oauthUser.getAttribute("name");
            Object id = oauthUser.getAttribute("id");
            providerId = id != null ? id.toString() : null;

            if(name == null) {
                name = oauthUser.getAttribute("login");
            }
        }

        User user = userRepository.findByEmail(email).orElseGet(User::new);
        if (email == null || email.isBlank()) {
            throw new BadRequestException("OAuth provider did not return an email address.");
        }
        if (name == null || name.isBlank()) {
            name = email;
        }

        user.setEmail(email);
        user.setName(name);
        user.setProvider(provider);
        user.setProviderID(providerId);
        if (user.getCreatedAt() == null) {
            user.setCreatedAt(LocalDateTime.now());
        }

        return userRepository.save(user);
    }
}
