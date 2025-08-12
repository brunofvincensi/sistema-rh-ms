package com.rh.employee.clients.user;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "user-service")
public interface UserApiClient {

    @PostMapping("/users")
    ResponseEntity<Void> createUser(@RequestBody UserRequest request);

    @PutMapping("/users/employee/{id}")
    ResponseEntity<Void> updateByIdEmployee(@PathVariable("id") String userId, @RequestBody UserUpdateRequest userRequest);

}
