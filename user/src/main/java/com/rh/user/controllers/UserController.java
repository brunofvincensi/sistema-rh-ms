package com.rh.user.controllers;

import com.rh.user.dtos.UserRequest;
import com.rh.user.dtos.UserResponse;
import com.rh.user.dtos.UserUpdateRequest;
import com.rh.user.models.UserEntity;
import com.rh.user.services.UserService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController("/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping
    public ResponseEntity<?> create(@RequestBody @Valid UserRequest userRequest) {
        UserEntity user = userService.create(userRequest);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> findById(@PathVariable("id") UUID userId) {
        UserResponse userResponse = userService.findById(userId);
        return ResponseEntity.ok(userResponse);
    }

    @PutMapping
    public ResponseEntity<?> update(@RequestBody @Valid UserUpdateRequest userRequest) {
        UserEntity user = userService.update(userRequest);
        return ResponseEntity.ok(user.getCpf());
    }

}
