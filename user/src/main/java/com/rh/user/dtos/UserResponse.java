package com.rh.user.dtos;

import com.rh.user.models.UserEntity;
import lombok.Data;

import java.util.UUID;

@Data
public class UserResponse {

    private UUID id;
    private UUID employeeId;
    private String role;

    public UserResponse(UserEntity entity) {
        this.id = entity.getId();
        this.employeeId = entity.getEmployeeId();
        this.role = entity.getRole();
    }

}
