package com.GradeCenter.controllers;

import com.GradeCenter.dtos.UserAssignRoleRequest;
import com.GradeCenter.dtos.UserAuthorizationRequest;
import com.GradeCenter.dtos.UserInfoResponse;
import com.GradeCenter.dtos.UserRegistrationRequest;
import com.GradeCenter.service.implementation.KeycloakAdminClientService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("api/v1/auth")
public class UserController {

    private final KeycloakAdminClientService keycloakAdminClientService;

    private static final List<String> VALID_ROLES = Arrays.asList("admin", "director", "teacher", "parent"  ,"student");

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
    public ResponseEntity<String> login(@RequestBody UserAuthorizationRequest userAuthorizationRequest) {
        ResponseEntity<String> response = keycloakAdminClientService.loginUser(userAuthorizationRequest.getUsername(), userAuthorizationRequest.getPassword());
        if (response.getStatusCode().is2xxSuccessful()) {
            return ResponseEntity.ok(response.getBody());
        } else {
            return ResponseEntity.status(response.getStatusCode()).body("Failed to login user");
        }
    }

    @PostMapping("/assign-role-student")
    @PreAuthorize("hasRole('admin')")
    public ResponseEntity<String> assignRoleStudent(@RequestBody UserAssignRoleRequest userAssignRoleRequest) {
        ResponseEntity<String> response = keycloakAdminClientService.assignRole(userAssignRoleRequest.getUserID(), userAssignRoleRequest.getRole());
        if (response.getStatusCode().is2xxSuccessful()) {


            return ResponseEntity.ok("Role assigned successfully");

        } else {
            return ResponseEntity.status(response.getStatusCode()).body("Failed to assign role");
        }
    }


    @GetMapping("/info")
    public ResponseEntity<UserInfoResponse> getUserInfo(@AuthenticationPrincipal Jwt jwt) {
        String username = jwt.getClaimAsString("preferred_username");
        String userId = jwt.getClaimAsString("sub");
        Map<String, Object> rolesMap = jwt.getClaim("realm_access");
        List<String> roles = (List<String>) rolesMap.get("roles");

        String role = roles.stream()
                .filter(VALID_ROLES::contains)
                .min((r1, r2) -> Integer.compare(VALID_ROLES.indexOf(r1), VALID_ROLES.indexOf(r2)))
                .orElse("unknown");

        UserInfoResponse userInfoResponse = new UserInfoResponse(username, userId, role);
        return ResponseEntity.ok(userInfoResponse);
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