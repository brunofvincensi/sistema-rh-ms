package com.rh.employee.clients.user;

import com.rh.employee.configs.FeignConfig;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(
        name = "user-api",
        url = "${user.api.url}",  // Ex: http://api-gateway:8080/user-service
        configuration = FeignConfig.class
)
public interface UserApiClient {

    @PostMapping("/users")
    ResponseEntity<Void> createUser(@RequestBody UserRequest request);

}
