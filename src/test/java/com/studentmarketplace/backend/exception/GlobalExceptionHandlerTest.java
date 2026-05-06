package com.studentmarketplace.backend.exception;

import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class GlobalExceptionHandlerTest {

    private final GlobalExceptionHandler handler = new GlobalExceptionHandler();

    @Test
    void notFound_returns404() {
        ResponseEntity<ApiErrorResponse> response = handler.handleNotFound(
                new NotFoundException("missing"),
                mockRequest("/users/1")
        );
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    void badRequest_returns400() {
        ResponseEntity<ApiErrorResponse> response = handler.handleBadRequest(
                new BadRequestException("bad"),
                mockRequest("/listings")
        );
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    void conflict_returns409() {
        ResponseEntity<ApiErrorResponse> response = handler.handleConflict(
                new ConflictException("conflict"),
                mockRequest("/favorites/1")
        );
        assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
    }

    @Test
    void unauthorized_returns401() {
        AuthenticationException authEx = new AuthenticationException("auth failed") { };
        ResponseEntity<ApiErrorResponse> response = handler.handleUnauthorized(authEx, mockRequest("/messages"));
        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
    }

    @Test
    void forbidden_returns403() {
        ResponseEntity<ApiErrorResponse> response = handler.handleForbidden(
                new AccessDeniedException("denied"),
                mockRequest("/report")
        );
        assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());
    }

    @Test
    void internalError_returns500() {
        ResponseEntity<ApiErrorResponse> response = handler.handleUnhandled(
                new RuntimeException("boom"),
                mockRequest("/internal")
        );
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("An unexpected error occurred.", response.getBody().message());
    }

    private HttpServletRequest mockRequest(String path) {
        HttpServletRequest request = mock(HttpServletRequest.class);
        when(request.getRequestURI()).thenReturn(path);
        return request;
    }
}
