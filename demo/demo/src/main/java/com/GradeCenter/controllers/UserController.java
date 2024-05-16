package com.GradeCenter.controllers;

import com.GradeCenter.dtos.KeycloakUser;
import com.GradeCenter.service.implementation.KeycloakAdminClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/v1/auth")
public class UserController {

    @Autowired
    private KeycloakAdminClient keycloakAdminClient;

    @PostMapping("/register")
    public ResponseEntity<String> registerUser(@RequestBody KeycloakUser registrationRequest) {
        try {
            keycloakAdminClient.createUser(registrationRequest.getUsername(), registrationRequest.getPassword());
            return ResponseEntity.ok("User registered successfully");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error registering user");
        }
    }

    @PostMapping("/hi")
    public ResponseEntity<String> sayHi() {
        return ResponseEntity.ok("Hi");
    }
}