package com.rh.employee.clients.user.models;

import java.util.UUID;

public record UserResponse (UUID id,
                            UUID employeeId,
                            String role) {

}
