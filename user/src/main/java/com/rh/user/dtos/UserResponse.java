package com.rh.user.dtos;

import java.util.UUID;

public record UserResponse (UUID id,
                            UUID employeeId,
                            String role) {

}
