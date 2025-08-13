package com.rh.employee.clients.user;

import com.rh.employee.clients.user.models.UserRequest;
import com.rh.employee.clients.user.models.UserResponse;
import com.rh.employee.clients.user.models.UserUpdateRequest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@FeignClient(name = "user-service")
public interface UserApiClient {

    @PostMapping("/users")
    ResponseEntity<UserResponse> createUser(@RequestBody UserRequest request);

    @PutMapping("/users/employee/{id}")
    ResponseEntity<UserResponse> updateByEmployeeId(@PathVariable("id") String employeeId, @RequestBody UserUpdateRequest userRequest);

    @DeleteMapping("/users/employee/{id}")
    ResponseEntity<Void> deleteUser(@PathVariable("id") String employeeId);
}
