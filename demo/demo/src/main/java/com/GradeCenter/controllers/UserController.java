package com.GradeCenter.controllers;

import com.GradeCenter.dtos.*;
import com.GradeCenter.service.implementation.KeycloakAdminClientService;
import com.GradeCenter.service.implementation.UserEntityHandlingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("api/v1/auth")
public class UserController {

    private final KeycloakAdminClientService keycloakAdminClientService;

    private final UserEntityHandlingService userEntityHandlingService;

    public UserController(KeycloakAdminClientService keycloakAdminClientService, UserEntityHandlingService userEntityHandlingService) {
        this.keycloakAdminClientService = keycloakAdminClientService;
        this.userEntityHandlingService = userEntityHandlingService;
    }

    @PostMapping("/register")
    public ResponseEntity<String> registerUser(@RequestBody UserRegistrationRequest request) {
        ApiResponse<String> response = keycloakAdminClientService.createUser(request.getUsername(), request.getPassword());

        if (!response.isSuccess()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response.getMessage());
        }

        String userId = response.getData();
        ApiResponse<String> roleResponse = userEntityHandlingService.assignRole(userId, request.getRole());
        if (!roleResponse.isSuccess()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(roleResponse.getMessage());
        }

        return ResponseEntity.ok("User registered and role assigned successfully");
    }

    @PostMapping("/login")
    public ResponseEntity<Map<String, Object>> login(@RequestBody UserAuthorizationRequest userAuthorizationRequest) {
        ApiResponse<Map<String, Object>> response = keycloakAdminClientService.loginUser(userAuthorizationRequest.getUsername(), userAuthorizationRequest.getPassword());
        if (response.isSuccess()) {
            return ResponseEntity.ok(response.getData());
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response.getData());
        }
    }

    @GetMapping("/info")
    public ResponseEntity<UserInfoResponse> getUserInfo(@AuthenticationPrincipal Jwt jwt) {
        ApiResponse<UserInfoResponse> response = keycloakAdminClientService.getUserInfo(jwt);
        if (response.isSuccess()) {
            return ResponseEntity.ok(response.getData());
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
    }

    @Deprecated
    @PostMapping("/assign-role")
    public ResponseEntity<String> assignRoleStudent(@RequestBody UserRoleRequest userAssignRoleRequest) {
        ApiResponse<String> response = userEntityHandlingService.assignRole(userAssignRoleRequest.getUserID(), userAssignRoleRequest.getRole());
        if (response.isSuccess()) {
            return ResponseEntity.ok(response.getMessage());
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response.getMessage());
        }
    }

    @Deprecated
    @PostMapping("/assign-role-username")
    public ResponseEntity<String> assignRoleByUsername(@RequestBody UserRoleUsernameRequest userAssignRoleUsernameRequest) {
        ApiResponse<String> response = userEntityHandlingService.assignRoleUsername(userAssignRoleUsernameRequest.getUsername(), userAssignRoleUsernameRequest.getRole());
        if (response.isSuccess()) {
            return ResponseEntity.ok(response.getMessage());
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response.getMessage());
        }
    }

    @DeleteMapping("/delete-user/uid={userId}")
    public ResponseEntity<String> deleteUser(@PathVariable String userId) {
        ApiResponse<String> response = userEntityHandlingService.deleteUser(userId);
        if (response.isSuccess()) {
            return ResponseEntity.ok(response.getMessage());
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response.getMessage());
        }
    }

    @DeleteMapping("/delete-user/username={username}")
    public ResponseEntity<String> deleteUserByUsername(@PathVariable String username) {
        ApiResponse<String> response = userEntityHandlingService.deleteUserByUsername(username);
        if (response.isSuccess()) {
            return ResponseEntity.ok(response.getMessage());
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response.getMessage());
        }
    }

    @PutMapping("/update-credentials")
    public ResponseEntity<String> updateUserCredentials(@RequestBody UserUpdateCredentialsRequest userUpdateCredentialsRequest) {
        ApiResponse<String> response = keycloakAdminClientService.updateUserCredentials(userUpdateCredentialsRequest);
        if (response.isSuccess()) {
            return ResponseEntity.ok(response.getMessage());
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response.getMessage());
        }
    }

    @PostMapping("/remove-role")
    public ResponseEntity<String> removeRole(@RequestBody UserRoleRequest userRemoveRoleRequest) {
        ApiResponse<String> response = userEntityHandlingService.removeRole(userRemoveRoleRequest.getUserID(), userRemoveRoleRequest.getRole());
        if (response.isSuccess()) {
            return ResponseEntity.ok(response.getMessage());
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response.getMessage());
        }
    }

    @GetMapping("/token")
    public ResponseEntity<Jwt> getToken(@AuthenticationPrincipal Jwt jwt) {
        return ResponseEntity.ok(jwt);
    }

    @GetMapping("/users")
    public ResponseEntity<Map<String, String>> getUsers() {
        ApiResponse<Map<String, String>> response = keycloakAdminClientService.getAllUsersAndRoles();
        if (response.isSuccess()) {
            return ResponseEntity.ok(response.getData());
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
    }

    @PatchMapping("/switch-role")
    public ResponseEntity<String> switchUserRole(@RequestBody SwitchUserRoleRequest switchUserRoleRequest) {
        ApiResponse<String> response = userEntityHandlingService.switchUserRole(
                switchUserRoleRequest.getUserID(),
                switchUserRoleRequest.getRole()
        );
        if (response.isSuccess()) {
            return ResponseEntity.ok(response.getMessage());
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response.getMessage());
        }
    }

    @PatchMapping("/switch-role-username")
    public ResponseEntity<String> switchUserRoleByUsername(@RequestBody SwitchUserUsernameRoleRequest switchUserRoleRequest) {
        ApiResponse<String> response = userEntityHandlingService.switchUserRoleByUsername(
                switchUserRoleRequest.getUsername(),
                switchUserRoleRequest.getRole()
        );
        if (response.isSuccess()) {
            return ResponseEntity.ok(response.getMessage());
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response.getMessage());
        }
    }
}
