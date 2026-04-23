package com.studentmarketplace.backend.controller;

import com.studentmarketplace.backend.service.UserService;
import com.studentmarketplace.backend.model.User;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    UserController(UserService userService){
        this.userService = userService;
    }

    // GET /users
    @GetMapping
    public ResponseEntity<List<User>> getAllUsers(){
        return ResponseEntity.ok(userService.getAllUsers());
    }

    // GET /users/{id}
    @GetMapping("/{id}")
    public ResponseEntity<Optional<User>> getUserById(@PathVariable UUID id){
        return ResponseEntity.ok(userService.getUserById(id));
    }

    // GET /users/me (Implement this when we can get current user)
    /*
    @GetMapping("/me")
    public ResponseEntity<User> getCurrentUser(){
        return ResponseEntity.ok(userService.getCurrentUser());
    }
     */

    // PUT /users/me
    @PutMapping("/me")
    public ResponseEntity<User> updateCurrentUser(@PathVariable UUID id,@RequestBody User user){
        return ResponseEntity.ok(userService.updateUser(id, user));
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable UUID id){
        userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }
}
