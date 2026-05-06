package com.studentmarketplace.backend.exception;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = GlobalExceptionHandlerTest.ExceptionThrowingController.class)
@Import(GlobalExceptionHandler.class)
class GlobalExceptionHandlerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void handlesNotFound() throws Exception {
        mockMvc.perform(get("/test-exceptions/not-found"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404));
    }

    @Test
    void handlesBadRequest() throws Exception {
        mockMvc.perform(get("/test-exceptions/bad-request"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400));
    }

    @Test
    void handlesConflict() throws Exception {
        mockMvc.perform(get("/test-exceptions/conflict"))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.status").value(409));
    }

    @Test
    void handlesUnauthorized() throws Exception {
        mockMvc.perform(get("/test-exceptions/unauthorized"))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.status").value(401));
    }

    @Test
    void handlesForbidden() throws Exception {
        mockMvc.perform(get("/test-exceptions/forbidden"))
                .andExpect(status().isForbidden())
                .andExpect(jsonPath("$.status").value(403));
    }

    @Test
    void handlesInternalServerError() throws Exception {
        mockMvc.perform(get("/test-exceptions/internal"))
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.status").value(500))
                .andExpect(jsonPath("$.message").value("An unexpected error occurred."));
    }

    @RestController
    @RequestMapping("/test-exceptions")
    static class ExceptionThrowingController {
        @GetMapping("/not-found")
        String notFound() {
            throw new NotFoundException("missing");
        }

        @GetMapping("/bad-request")
        String badRequest() {
            throw new BadRequestException("bad");
        }

        @GetMapping("/conflict")
        String conflict() {
            throw new ConflictException("conflict");
        }

        @GetMapping("/unauthorized")
        String unauthorized() {
            throw new UnauthorizedException("unauthorized");
        }

        @GetMapping("/forbidden")
        String forbidden() {
            throw new ForbiddenException("forbidden");
        }

        @GetMapping("/internal")
        String internal() {
            throw new RuntimeException("boom");
        }
    }
}
