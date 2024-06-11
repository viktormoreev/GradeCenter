package com.GradeCenter.controllers;

import com.GradeCenter.dtos.*;
import com.GradeCenter.service.implementation.KeycloakAdminClientService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
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

        ResponseEntity<String> response = keycloakAdminClientService.createUser(request.getUsername(), request.getPassword());
        if (response.getStatusCode().is2xxSuccessful()) {
            String userId = extractUserIdFromLocationHeader(response.getHeaders().getLocation());
            return ResponseEntity.ok("User registered successfully");
        } else {
            return ResponseEntity.status(response.getStatusCode()).body("Failed to register user");
        }
    }

    @PostMapping("/login")
    public ResponseEntity<Map<String, Object>> login(@RequestBody UserAuthorizationRequest userAuthorizationRequest) {
        ResponseEntity<Map<String, Object>> response = keycloakAdminClientService.loginUser(userAuthorizationRequest.getUsername(), userAuthorizationRequest.getPassword());
        if (response.getStatusCode().is2xxSuccessful()) {
            return ResponseEntity.ok(response.getBody());
        } else {
            return ResponseEntity.status(response.getStatusCode()).body(response.getBody());
        }
    }


    @GetMapping("/info")
    public ResponseEntity<UserInfoResponse> getUserInfo(@AuthenticationPrincipal Jwt jwt) {
        return keycloakAdminClientService.getUserInfo(jwt);
    }


    @PostMapping("/assign-role")
    //@PreAuthorize("hasRole('admin')")
    public ResponseEntity<String> assignRoleStudent(@RequestBody UserRoleRequest userAssignRoleRequest) {
        ResponseEntity<String> response = keycloakAdminClientService.assignRole(userAssignRoleRequest.getUserID(), userAssignRoleRequest.getRole());
        if (response.getStatusCode().is2xxSuccessful()) {


            return ResponseEntity.ok("Role assigned successfully");

        } else {
            return ResponseEntity.status(response.getStatusCode()).body("Failed to assign role");
        }
    }


    @DeleteMapping("/delete-user/{userId}")
    @PreAuthorize("hasRole('admin')")
    public ResponseEntity<String> deleteUser(@PathVariable String userId) {
        ResponseEntity<String> response = keycloakAdminClientService.deleteUser(userId);
        return response;
    }

    @PutMapping("/update-credentials")
    @PreAuthorize("hasRole('admin')")
    public ResponseEntity<String> updateUserCredentials(@RequestBody UserUpdateCredentialsRequest userUpdateCredentialsRequest) {
        ResponseEntity<String> response = keycloakAdminClientService.updateUserCredentials(userUpdateCredentialsRequest);
        return response;
    }

    @PostMapping("/remove-role")
    @PreAuthorize("hasRole('admin')")
    public ResponseEntity<String> removeRole(@RequestBody UserRoleRequest userRemoveRoleRequest) {
        ResponseEntity<String> response = keycloakAdminClientService.removeRole(userRemoveRoleRequest.getUserID(), userRemoveRoleRequest.getRole());
        return response;
    }


    // A method that just returns the token back to the user
    @GetMapping("/token")
    public ResponseEntity<Jwt> getToken(@AuthenticationPrincipal Jwt jwt) {
        return ResponseEntity.ok(jwt);
    }


    private String extractUserIdFromLocationHeader(URI location) {
        String path = location.getPath();
        return path.substring(path.lastIndexOf('/') + 1);
    }

}