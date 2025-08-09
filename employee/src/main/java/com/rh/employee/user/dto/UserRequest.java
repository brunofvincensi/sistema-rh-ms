package com.rh.employee.user.dto;

import java.util.UUID;

public record UserRequest (UUID employeeId, String cpf, String password, String role) {
}
