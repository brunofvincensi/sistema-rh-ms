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

@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping
    public ResponseEntity<UserResponse> create(@RequestBody @Valid UserRequest userRequest) {
        UserResponse userResponse = userService.create(userRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(userResponse);
    }

    @DeleteMapping("/users/employee/{id}")
    ResponseEntity<Void> deleteUser(@PathVariable("id") String employeeId) {
        userService.deleteByEmployeeId(UUID.fromString(employeeId));
        return ResponseEntity.ok().build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<UserResponse> update(@PathVariable("id") String userId, @RequestBody @Valid UserUpdateRequest userRequest) {
        UserResponse userResponse = userService.update(UUID.fromString(userId), userRequest);
        return ResponseEntity.ok(userResponse);
    }

    @PutMapping("/employee/{id}")
    public ResponseEntity<UserResponse> updateByIdEmployee(@PathVariable("id") String employeeId, @RequestBody @Valid UserUpdateRequest userRequest) {
        UserResponse userResponse = userService.updateByEmployeeId(UUID.fromString(employeeId), userRequest);
        return ResponseEntity.ok(userResponse);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> findById(@PathVariable("id") String userId) {
        UserResponse userResponse = userService.findById(UUID.fromString(userId));
        return ResponseEntity.ok(userResponse);
    }

}
