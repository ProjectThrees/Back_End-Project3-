package com.studentmarketplace.backend.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @GetMapping("/google")
    public Map<String, String> googleLogin() {
        return Map.of("url","/oauth/authorization/google");
    }

    @GetMapping("/github")
    public Map<String, String> githubLogin() {
        return Map.of("url","/oauth/authorization/github");
    }
}
