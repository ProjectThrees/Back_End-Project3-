package com.studentmarketplace.backend.controller;

import com.studentmarketplace.backend.dto.ApiMapper;
import com.studentmarketplace.backend.dto.UserCreateRequestDto;
import com.studentmarketplace.backend.dto.UserResponseDto;
import com.studentmarketplace.backend.dto.UserUpdateRequestDto;
import com.studentmarketplace.backend.exception.NotFoundException;
import com.studentmarketplace.backend.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    UserController(UserService userService){
        this.userService = userService;
    }

    // GET /users
    @GetMapping
    public ResponseEntity<List<UserResponseDto>> getAllUsers(){
        return ResponseEntity.ok(userService.getAllUsers().stream().map(ApiMapper::toUserResponse).toList());
    }

    // GET /users/{id}
    @GetMapping("/{id}")
    public ResponseEntity<UserResponseDto> getUserById(@PathVariable UUID id){
        return ResponseEntity.ok(
                userService.getUserById(id)
                        .map(ApiMapper::toUserResponse)
                        .orElseThrow(() -> new NotFoundException("User not found with id: " + id))
        );
    }

    @PostMapping
    public ResponseEntity<UserResponseDto> createUser(@RequestBody UserCreateRequestDto userRequest) {
        return ResponseEntity.ok(ApiMapper.toUserResponse(userService.createUser(ApiMapper.toUser(userRequest))));
    }

    // GET /users/me — returns the currently authenticated user's profile
    @GetMapping("/me")
    public ResponseEntity<UserResponseDto> getMe(@AuthenticationPrincipal Jwt jwt) {
        UUID userId = UUID.fromString(jwt.getSubject());
        return ResponseEntity.ok(
                userService.getUserById(userId)
                        .map(ApiMapper::toUserResponse)
                        .orElseThrow(() -> new NotFoundException("User not found with id: " + userId))
        );
    }

    // PUT /users/{id}
    @PutMapping("/{id}")
    public ResponseEntity<UserResponseDto> updateUser(@PathVariable UUID id, @RequestBody UserUpdateRequestDto userRequest){
        return ResponseEntity.ok(ApiMapper.toUserResponse(userService.updateUser(id, ApiMapper.toUser(userRequest))));
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable UUID id){
        userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }
}
