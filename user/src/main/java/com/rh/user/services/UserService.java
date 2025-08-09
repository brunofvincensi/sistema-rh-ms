package com.rh.user.services;

import com.rh.user.dtos.UserRequest;
import com.rh.user.dtos.UserUpdateRequest;
import com.rh.user.models.UserEntity;

public interface UserService {

    UserEntity create(UserRequest userRequest);

    UserEntity update(UserUpdateRequest userRequest);

}
