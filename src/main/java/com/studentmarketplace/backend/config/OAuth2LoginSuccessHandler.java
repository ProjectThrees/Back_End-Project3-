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
            boolean webAuthFlow = hasWebAuthCookie(request);

            User user = oAuthUserService.proccessOAuthUser(provider, oAuthUser);
            String appJwt = jwtService.generateToken(user);

            String redirectUrL = webAuthFlow
                    ? buildWebRedirect("token", appJwt)
                    : "myapp://oauth-success?token=" + URLEncoder.encode(appJwt, StandardCharsets.UTF_8);
            clearWebAuthCookie(response);
            response.sendRedirect(redirectUrL);
        } catch (Exception ex) {
            String errorMessage = URLEncoder.encode(ex.getMessage(), StandardCharsets.UTF_8);
            boolean webAuthFlow = hasWebAuthCookie(request);
            String redirectUrl = webAuthFlow
                    ? buildWebRedirect("error", errorMessage)
                    : "myapp://oauth-error?message=" + errorMessage;
            clearWebAuthCookie(response);
            response.sendRedirect(redirectUrl);
        }
    }

    private boolean hasWebAuthCookie(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        if (cookies == null) {
            return false;
        }
        for (Cookie cookie : cookies) {
            if ("oauth_mode".equals(cookie.getName()) && "web".equals(cookie.getValue())) {
                return true;
            }
        }
        return false;
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
