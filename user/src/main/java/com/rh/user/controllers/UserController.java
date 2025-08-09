package com.rh.user.controllers;

import com.rh.user.dtos.UserRequest;
import com.rh.user.dtos.UserUpdateRequest;
import com.rh.user.models.UserEntity;
import com.rh.user.services.UserService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController("/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/users")
    public ResponseEntity<?> create(@RequestBody @Valid UserRequest userRequest) {
        UserEntity user = userService.create(userRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(user.getCpf());
    }

    @PutMapping("/users")
    public ResponseEntity<?> update(@RequestBody @Valid UserUpdateRequest userRequest) {
        UserEntity user = userService.update(userRequest);
        return ResponseEntity.status(HttpStatus.OK).body(user.getCpf());
    }

}
