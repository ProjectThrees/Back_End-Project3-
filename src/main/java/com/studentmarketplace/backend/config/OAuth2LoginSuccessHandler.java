package com.studentmarketplace.backend.config;

import com.studentmarketplace.backend.model.User;
import com.studentmarketplace.backend.service.JwtService;
import com.studentmarketplace.backend.service.OAuthUserService;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;
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
    private final String webRedirectUrl;

    public OAuth2LoginSuccessHandler(
            OAuthUserService oAuthUserService,
            JwtService jwtService,
            @Value("${app.oauth.web.redirect-url}") String webRedirectUrl
    ) {
        this.oAuthUserService = oAuthUserService;
        this.jwtService = jwtService;
        this.webRedirectUrl = webRedirectUrl;
    }

    @Override
    public void onAuthenticationSuccess(
            HttpServletRequest request,
            HttpServletResponse response,
            Authentication authentication
    ) throws IOException, ServletException {
        try {
            OAuth2AuthenticationToken token = (OAuth2AuthenticationToken) authentication;
            String provider = token.getAuthorizedClientRegistrationId();
            OAuth2User oAuthUser = token.getPrincipal();
            boolean webAuthFlow = isWebAuthFlow(request);

            User user = oAuthUserService.proccessOAuthUser(provider, oAuthUser);
            String appJwt = jwtService.generateToken(user);

            String redirectUrL = webAuthFlow
                    ? buildWebRedirect("token", appJwt)
                    : "myapp://oauth-success?token=" + URLEncoder.encode(appJwt, StandardCharsets.UTF_8);
            clearWebAuthCookie(response);
            response.sendRedirect(redirectUrL);
        } catch (Exception ex) {
            String errorMessage = URLEncoder.encode(ex.getMessage(), StandardCharsets.UTF_8);
            boolean webAuthFlow = isWebAuthFlow(request);
            String redirectUrl = webAuthFlow
                    ? buildWebRedirect("error", errorMessage)
                    : "myapp://oauth-error?message=" + errorMessage;
            clearWebAuthCookie(response);
            response.sendRedirect(redirectUrl);
        }
    }

    private boolean isWebAuthFlow(HttpServletRequest request) {
        String referer = request.getHeader("Referer");
        String origin = request.getHeader("Origin");
        return (referer != null && referer.contains("front-end-project3.onrender.com"))
                || (origin != null && origin.contains("front-end-project3.onrender.com"))
                || (referer != null && referer.contains("localhost"))
                || (origin != null && origin.contains("localhost"));
    }


    private void clearWebAuthCookie(HttpServletResponse response) {
        Cookie cookie = new Cookie("oauth_mode", "");
        cookie.setPath("/");
        cookie.setMaxAge(0);
        response.addCookie(cookie);
    }

    private String buildWebRedirect(String key, String value) {
        String separator = webRedirectUrl.contains("?") ? "&" : "?";
        return webRedirectUrl + separator + key + "=" + URLEncoder.encode(value, StandardCharsets.UTF_8);
    }
}
