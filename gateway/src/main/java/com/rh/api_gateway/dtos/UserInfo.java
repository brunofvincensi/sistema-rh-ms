package com.rh.api_gateway.dtos;

import java.util.UUID;

public class UserInfo {

    private UUID id;
    private String role;
    private UUID employeeId;

    public UserInfo() {}

    public UserInfo(UUID id, String role, UUID employeeId) {
        this.id = id;
        this.role = role;
        this.employeeId = employeeId;
    }

    // Getters e Setters
    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }

    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }

    public UUID getEmployeeId() { return employeeId; }
    public void setEmployeeId(UUID employeeId) { this.employeeId = employeeId; }

}
