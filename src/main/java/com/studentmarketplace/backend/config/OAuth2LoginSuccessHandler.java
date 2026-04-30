package com.studentmarketplace.backend.config;

import com.studentmarketplace.backend.model.User;
import com.studentmarketplace.backend.service.JwtService;
import com.studentmarketplace.backend.service.OAuthUserService;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

@Component
public class OAuth2LoginSuccessHandler implements AuthenticationSuccessHandler {

    private final OAuthUserService oAuthUserService;
    private final JwtService jwtService;

    public OAuth2LoginSuccessHandler(OAuthUserService oAuthUserService, JwtService jwtService) {
        this.oAuthUserService = oAuthUserService;
        this.jwtService = jwtService;
    }

    @Override
    public void onAuthenticationSuccess(
            HttpServletRequest request,
            HttpServletResponse response,
            Authentication authentication
    ) throws IOException, ServletException {
        OAuth2AuthenticationToken token = (OAuth2AuthenticationToken) authentication;
        String provider = token.getAuthorizedClientRegistrationId();
        OAuth2User oAuthUser = token.getPrincipal();

        User user = oAuthUserService.proccessOAuthUser(provider, oAuthUser);
        String appJwt = jwtService.generateToken(user);

        String redirectUrL = "myapp://oauth-success?token=" + URLEncoder.encode(appJwt, StandardCharsets.UTF_8);
        response.sendRedirect(redirectUrL);
    }
}
