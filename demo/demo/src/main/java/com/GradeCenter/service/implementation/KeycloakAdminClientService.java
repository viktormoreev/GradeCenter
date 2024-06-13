package com.GradeCenter.service.implementation;

import com.GradeCenter.dtos.ApiResponse;
import com.GradeCenter.dtos.UserInfoResponse;
import com.GradeCenter.dtos.UserUpdateCredentialsRequest;
import com.GradeCenter.service.StudentService;
import jakarta.ws.rs.core.Response;
import org.keycloak.OAuth2Constants;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.KeycloakBuilder;
import org.keycloak.admin.client.resource.UserResource;
import org.keycloak.representations.AccessTokenResponse;
import org.keycloak.representations.idm.CredentialRepresentation;
import org.keycloak.representations.idm.RoleRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;

import java.net.URI;
import java.util.*;

@Service
public class KeycloakAdminClientService {

    private static final Logger logger = LoggerFactory.getLogger(KeycloakAdminClientService.class);

    private static final List<String> VALID_ROLES = Arrays.asList("admin", "director", "teacher", "parent", "student");

    @Value("${keycloak.auth-server-url}")
    private String keycloakAdminUrl;

    @Value("${keycloak.realm}")
    private String keycloakRealm;

    @Value("${keycloak.resource}")
    private String clientId;

    @Value("${keycloak.admin.username}")
    private String keycloakAdminUsername;

    @Value("${keycloak.admin.password}")
    private String keycloakAdminPassword;

    @Autowired
    private Keycloak keycloak;

    @Autowired
    private StudentService studentService;

    public ApiResponse<Map<String, Object>> loginUser(String username, String password) {
        if (username == null || username.isEmpty() || password == null || password.isEmpty()) {
            return new ApiResponse<>(false, "Username or password cannot be null or empty", null);
        }

        try {
            AccessTokenResponse tokenResponse = keycloak.tokenManager().getAccessToken();

            UserRepresentation user = keycloak.realm(keycloakRealm).users().search(username).get(0);
            List<RoleRepresentation> roles = keycloak.realm(keycloakRealm)
                    .users()
                    .get(user.getId())
                    .roles()
                    .realmLevel()
                    .listAll();

            Map<String, Object> response = new HashMap<>();
            response.put("role", stripRoles(roles.stream().map(RoleRepresentation::getName).toList()));
            response.put("access_token", tokenResponse.getToken());


            return new ApiResponse<>(true, "Login successful", response);
        } catch (Exception e) {
            logger.error("Error logging in user: {}", e.getMessage(), e);
            return new ApiResponse<>(false, "Login failed: " + e.getMessage(), null);
        }
    }

    public ApiResponse<String> createUser(String username, String password) {
        logger.info("Creating user: {}", username);
        try {
            UserRepresentation user = new UserRepresentation();
            user.setUsername(username);
            user.setEnabled(true);

            CredentialRepresentation credential = new CredentialRepresentation();
            credential.setTemporary(false);
            credential.setType(CredentialRepresentation.PASSWORD);
            credential.setValue(password);

            user.setCredentials(Collections.singletonList(credential));

            Response response = keycloak.realm(keycloakRealm).users().create(user);
            if (response.getStatus() == 201) {
                String userId = extractUserIdFromLocationHeader(response.getLocation());
                return new ApiResponse<>(true, "User created successfully", userId);
            } else {
                return new ApiResponse<>(false, "Failed to create user: " + response.getStatusInfo().getReasonPhrase(), null);
            }
        } catch (Exception e) {
            logger.error("Error creating user: {}", e.getMessage(), e);
            return new ApiResponse<>(false, "Failed to create user", null);
        }
    }

    public ApiResponse<String> assignRole(String userId, String roleName) {
        if (userId == null || userId.isEmpty() || roleName == null || roleName.isEmpty()) {
            return new ApiResponse<>(false, "User ID or role name cannot be null or empty", null);
        }

        try {
            RoleRepresentation role = keycloak.realm(keycloakRealm).roles().get(roleName).toRepresentation();
            if (role == null) {
                return new ApiResponse<>(false, "Role not found", null);
            }

            UserResource userResource = keycloak.realm(keycloakRealm).users().get(userId);
            if (userResource == null) {
                return new ApiResponse<>(false, "User not found", null);
            }

            userResource.roles().realmLevel().add(Collections.singletonList(role));
            return new ApiResponse<>(true, "Role assigned successfully", null);
        } catch (Exception e) {
            logger.error("Error assigning role: {}", e.getMessage(), e);
            return new ApiResponse<>(false, "Failed to assign role: " + e.getMessage(), null);
        }
    }


    public ApiResponse<String> assignRoleUsername(String username, String roleName) {
        try {
            UserRepresentation user = keycloak.realm(keycloakRealm).users().search(username).get(0);
            return assignRole(user.getId(), roleName);
        } catch (Exception e) {
            logger.error("Error assigning role by username: {}", e.getMessage(), e);
            return new ApiResponse<>(false, "Failed to assign role by username", null);
        }
    }

    public ApiResponse<String> deleteUser(String userId) {
        try {
            keycloak.realm(keycloakRealm).users().delete(userId);
            return new ApiResponse<>(true, "User deleted successfully", null);
        } catch (Exception e) {
            logger.error("Error deleting user: {}", e.getMessage(), e);
            return new ApiResponse<>(false, "Failed to delete user", null);
        }
    }

    public ApiResponse<String> updateUserCredentials(UserUpdateCredentialsRequest userUpdateCredentialsRequest) {
        try {
            UserRepresentation user = keycloak.realm(keycloakRealm).users().get(userUpdateCredentialsRequest.getUserID()).toRepresentation();

            if (userUpdateCredentialsRequest.getUsername() != null && !userUpdateCredentialsRequest.getUsername().isEmpty()) {
                user.setUsername(userUpdateCredentialsRequest.getUsername());
            }

            if (userUpdateCredentialsRequest.getPassword() != null && !userUpdateCredentialsRequest.getPassword().isEmpty()) {
                CredentialRepresentation credential = new CredentialRepresentation();
                credential.setTemporary(false);
                credential.setType(CredentialRepresentation.PASSWORD);
                credential.setValue(userUpdateCredentialsRequest.getPassword());
                user.setCredentials(Collections.singletonList(credential));
            }

            keycloak.realm(keycloakRealm).users().get(userUpdateCredentialsRequest.getUserID()).update(user);
            return new ApiResponse<>(true, "User credentials updated successfully", null);
        } catch (Exception e) {
            logger.error("Error updating user credentials: {}", e.getMessage(), e);
            return new ApiResponse<>(false, "Failed to update user credentials", null);
        }
    }

    public ApiResponse<String> removeRole(String userId, String roleName) {
        try {
            RoleRepresentation role = keycloak.realm(keycloakRealm).roles().get(roleName).toRepresentation();
            keycloak.realm(keycloakRealm).users().get(userId).roles().realmLevel().remove(Collections.singletonList(role));
            return new ApiResponse<>(true, "Role removed successfully", null);
        } catch (Exception e) {
            logger.error("Error removing role: {}", e.getMessage(), e);
            return new ApiResponse<>(false, "Failed to remove role", null);
        }
    }

    public ApiResponse<UserInfoResponse> getUserInfo(Jwt jwt) {
        String username = jwt.getClaimAsString("preferred_username");
        String userId = jwt.getClaimAsString("sub");
        String role = stripRoles(jwt.getClaim("realm_access"));

        UserInfoResponse userInfoResponse = new UserInfoResponse(username, userId, role);
        return new ApiResponse<>(true, "User info retrieved successfully", userInfoResponse);
    }

    public String stripRoles(List<String> rolesMap) {
        return rolesMap.stream()
                .filter(VALID_ROLES::contains)
                .min((r1, r2) -> Integer.compare(VALID_ROLES.indexOf(r1), VALID_ROLES.indexOf(r2)))
                .orElse("unknown");
    }

    public ApiResponse<Map<String, String>> getAllUsersAndRoles() {
        try {
            List<UserRepresentation> users = keycloak.realm(keycloakRealm).users().list();
            Map<String, String> userRolesMap = new HashMap<>();

            for (UserRepresentation user : users) {
                List<RoleRepresentation> roles = keycloak.realm(keycloakRealm)
                        .users()
                        .get(user.getId())
                        .roles()
                        .realmLevel()
                        .listAll();
                List<String> roleNames = new ArrayList<>();
                for (RoleRepresentation role : roles) {
                    roleNames.add(role.getName());
                }
                userRolesMap.put(user.getUsername(), stripRoles(roleNames));
            }
            return new ApiResponse<>(true, "Users and roles retrieved successfully", userRolesMap);
        } catch (Exception e) {
            logger.error("Error getting users and roles: {}", e.getMessage(), e);
            return new ApiResponse<>(false, "Failed to get users and roles", null);
        }
    }

    public ApiResponse<String> switchUserRole(String userId, String newRoleName) {
        if (userId == null || userId.isEmpty() || newRoleName == null || newRoleName.isEmpty()) {
            return new ApiResponse<>(false, "User ID or new role name cannot be null or empty", null);
        }

        if (!VALID_ROLES.contains(newRoleName)) {
            return new ApiResponse<>(false, "Invalid new role name", null);
        }

        try {
            UserResource userResource = keycloak.realm(keycloakRealm).users().get(userId);
            List<RoleRepresentation> currentRoles = userResource.roles().realmLevel().listAll();
            List<RoleRepresentation> rolesToRemove = new ArrayList<>();

            for (RoleRepresentation role : currentRoles) {
                if (VALID_ROLES.contains(role.getName())) {
                    rolesToRemove.add(role);
                }
            }

            if (!rolesToRemove.isEmpty()) {
                userResource.roles().realmLevel().remove(rolesToRemove);
            }

            RoleRepresentation newRole = keycloak.realm(keycloakRealm).roles().get(newRoleName).toRepresentation();
            userResource.roles().realmLevel().add(Collections.singletonList(newRole));

            return new ApiResponse<>(true, "Role switched successfully", null);
        } catch (Exception e) {
            logger.error("Error switching role: {}", e.getMessage(), e);
            return new ApiResponse<>(false, "Failed to switch role: " + e.getMessage(), null);
        }
    }

    public ApiResponse<String> switchUserRoleByUsername(String username, String newRoleName) {
        if (username == null || username.isEmpty() || newRoleName == null || newRoleName.isEmpty()) {
            return new ApiResponse<>(false, "Username or new role name cannot be null or empty", null);
        }

        if (!VALID_ROLES.contains(newRoleName)) {
            return new ApiResponse<>(false, "Invalid new role name", null);
        }

        try {
            // Search for the user by username
            List<UserRepresentation> users = keycloak.realm(keycloakRealm).users().search(username);
            if (users == null || users.isEmpty()) {
                return new ApiResponse<>(false, "User not found", null);
            }

            UserRepresentation user = users.get(0);
            UserResource userResource = keycloak.realm(keycloakRealm).users().get(user.getId());
            List<RoleRepresentation> currentRoles = userResource.roles().realmLevel().listAll();
            List<RoleRepresentation> rolesToRemove = new ArrayList<>();

            for (RoleRepresentation role : currentRoles) {
                if (VALID_ROLES.contains(role.getName())) {
                    rolesToRemove.add(role);
                }
            }

            // Remove current roles
            if (!rolesToRemove.isEmpty()) {
                userResource.roles().realmLevel().remove(rolesToRemove);
            }

            // Assign new role
            RoleRepresentation newRole = keycloak.realm(keycloakRealm).roles().get(newRoleName).toRepresentation();
            userResource.roles().realmLevel().add(Collections.singletonList(newRole));

            return new ApiResponse<>(true, "Role switched successfully", null);
        } catch (Exception e) {
            logger.error("Error switching role: {}", e.getMessage(), e);
            return new ApiResponse<>(false, "Failed to switch role: " + e.getMessage(), null);
        }
    }


    private String extractUserIdFromLocationHeader(URI location) {
        if (location != null) {
            String path = location.getPath();
            return path.substring(path.lastIndexOf('/') + 1);
        }
        throw new RuntimeException("Location header is missing or invalid");
    }

}
