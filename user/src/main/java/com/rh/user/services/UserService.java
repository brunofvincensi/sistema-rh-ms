package com.rh.user.services;

import com.rh.user.dtos.UserRequest;
import com.rh.user.dtos.UserResponse;
import com.rh.user.dtos.UserUpdateRequest;
import jakarta.validation.Valid;

import java.util.UUID;

public interface UserService {

    UserResponse create(UserRequest userRequest);

    void deleteByEmployeeId(UUID uuid);

    UserResponse update(UUID userId, UserUpdateRequest userRequest);

    UserResponse findById(UUID userId);

    UserResponse updateByEmployeeId(UUID uuid, @Valid UserUpdateRequest userRequest);
}
