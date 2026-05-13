package com.studentmarketplace.backend.config;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

/**
 * Intercepts mobile OAuth initiation requests to capture the app's deep-link redirect URI.
 *
 * Mobile clients (e.g. Expo Go) pass their current deep-link scheme as the
 * `mobile_redirect` query parameter on the `/oauth2/authorization/{provider}` URL.
 * This filter stores that value in the HTTP session so that
 * {@link OAuth2LoginSuccessHandler} can redirect to the correct URI after login,
 * regardless of whether the app is running in Expo Go (exp://) or as a standalone
 * build (myapp://).
 */
public class MobileRedirectFilter extends OncePerRequestFilter {

    static final String SESSION_KEY = "app_mobile_redirect";

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain
    ) throws ServletException, IOException {

        if (request.getServletPath().startsWith("/oauth2/authorization/")) {
            String mobileRedirect = request.getParameter("mobile_redirect");
            if (mobileRedirect != null && !mobileRedirect.isBlank()) {
                HttpSession session = request.getSession(true);
                session.setAttribute(SESSION_KEY, mobileRedirect);
            }
        }

        filterChain.doFilter(request, response);
    }
}
