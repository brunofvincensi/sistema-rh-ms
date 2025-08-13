package com.rh.employee.clients.user.models;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UserUpdateRequest {

    String cpf;
    String role;

}
