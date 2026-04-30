package com.studentmarketplace.backend.service;

import com.studentmarketplace.backend.repository.UserRepository;
import com.studentmarketplace.backend.model.User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

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
        user.setEmail(email);
        user.setName(name);
        user.setProvider(provider);
        user.setProviderID(providerId);

        return userRepository.save(user);
    }
}
