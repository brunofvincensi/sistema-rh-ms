package com.rh.user.services;

import com.rh.user.dtos.UserRequest;
import com.rh.user.dtos.UserResponse;
import com.rh.user.dtos.UserUpdateRequest;

import java.util.UUID;

public interface UserService {

    void create(UserRequest userRequest);

    void update(UserUpdateRequest userRequest);

    UserResponse findById(UUID userId);

}
