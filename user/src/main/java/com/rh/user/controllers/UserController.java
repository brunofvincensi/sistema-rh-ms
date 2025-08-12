package com.rh.user.controllers;

import com.rh.user.dtos.UserRequest;
import com.rh.user.dtos.UserResponse;
import com.rh.user.dtos.UserUpdateRequest;
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
    public ResponseEntity<Void> create(@RequestBody @Valid UserRequest userRequest) {
        userService.create(userRequest);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> update(@PathVariable("id") String userId, @RequestBody @Valid UserUpdateRequest userRequest) {
        userService.update(UUID.fromString(userId), userRequest);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/employee/{id}")
    public ResponseEntity<Void> updateByIdEmployee(@PathVariable("id") String employeeId, @RequestBody @Valid UserUpdateRequest userRequest) {
        userService.updateByEmployeeId(UUID.fromString(employeeId), userRequest);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> findById(@PathVariable("id") String userId) {
        UserResponse userResponse = userService.findById(UUID.fromString(userId));
        return ResponseEntity.ok(userResponse);
    }

}
