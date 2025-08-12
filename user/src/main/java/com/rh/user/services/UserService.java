package com.rh.user.services;

import com.rh.user.dtos.UserRequest;
import com.rh.user.dtos.UserResponse;
import com.rh.user.dtos.UserUpdateRequest;
import jakarta.validation.Valid;

import java.util.UUID;

public interface UserService {

    void create(UserRequest userRequest);

    void update(UUID userId, UserUpdateRequest userRequest);

    UserResponse findById(UUID userId);

    void updateByEmployeeId(UUID uuid, @Valid UserUpdateRequest userRequest);
}
