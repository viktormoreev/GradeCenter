package com.GradeCenter.controllers;

import com.GradeCenter.dtos.UserAuthorizationRequest;
import com.GradeCenter.dtos.UserRegistrationRequest;
import com.GradeCenter.service.implementation.KeycloakAdminClientService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.Map;

@RestController
@RequestMapping("api/v1/auth")
public class UserController {

    private final KeycloakAdminClientService keycloakAdminClientService;

    public UserController(KeycloakAdminClientService keycloakAdminClientService) {
        this.keycloakAdminClientService = keycloakAdminClientService;
    }

    @PostMapping("/register")
    //@PreAuthorize("hasRole('admin')")
    public ResponseEntity<String> registerUser(@RequestBody UserRegistrationRequest request) {

        ResponseEntity<String> response = keycloakAdminClientService.createUser(request.getUsername(), request.getPassword(), request.getEmail());
        if (response.getStatusCode().is2xxSuccessful()) {
            String userId = extractUserIdFromLocationHeader(response.getHeaders().getLocation());
            keycloakAdminClientService.assignRole(userId, request.getRole());
            return ResponseEntity.ok("User registered successfully");
        } else {
            return ResponseEntity.status(response.getStatusCode()).body("Failed to register user");
        }
    }

    @GetMapping("/login")
    public ResponseEntity<String> login(@RequestBody UserAuthorizationRequest userAuthorizationRequest) {
        ResponseEntity<String> response = keycloakAdminClientService.loginUser(userAuthorizationRequest.getUsername(), userAuthorizationRequest.getPassword());
        if (response.getStatusCode().is2xxSuccessful()) {
            return ResponseEntity.ok(response.getBody());
        } else {
            return ResponseEntity.status(response.getStatusCode()).body("Failed to login user");
        }
    }

    /*
    @PostMapping("/assign-role-student")
    @PreAuthorize("hasRole('admin')")
    public ResponseEntity<String> assignRoleStudent(@RequestBody UserAssignRoleTeacherRequest userAssignRoleRequest) {
        ResponseEntity<String> response = keycloakAdminClientService.assignRole(userAssignRoleRequest.getUserID(), userAssignRoleRequest.getRole());
        if (response.getStatusCode().is2xxSuccessful()) {
            return ResponseEntity.ok("Role assigned successfully");

            // Add the corresponding entity in the database

        } else {
            return ResponseEntity.status(response.getStatusCode()).body("Failed to assign role");
        }
    }*/

    /* Test controller, will remove later */
    @GetMapping("/roles")
    public Map<String, Object> getUserRoles(@AuthenticationPrincipal Jwt jwt) {
        String userId = jwt.getSubject();
        return keycloakAdminClientService.getUserRoleMappings(userId);
    }
    // <---------------------------------->

    private String extractUserIdFromLocationHeader(URI location) {
        String path = location.getPath();
        return path.substring(path.lastIndexOf('/') + 1);
    }

}