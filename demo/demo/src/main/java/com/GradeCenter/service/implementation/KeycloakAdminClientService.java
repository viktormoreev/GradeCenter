package com.GradeCenter.service.implementation;

import com.GradeCenter.dtos.UserInfoResponse;
import com.GradeCenter.dtos.UserUpdateCredentialsRequest;
import com.GradeCenter.service.StudentService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.*;

@Service
public class KeycloakAdminClientService {

    private static final Logger logger = LoggerFactory.getLogger(KeycloakAdminClientService.class);

    private static final List<String> VALID_ROLES = Arrays.asList("admin", "director", "teacher", "parent", "student");

    @Value("${keycloak.admin.url}")
    private String keycloakAdminUrl;

    @Value("${keycloak.admin.username}")
    private String keycloakAdminUsername;

    @Value("${keycloak.admin.password}")
    private String keycloakAdminPassword;

    @Value("${keycloak.realm}")
    private String keycloakRealm;

    private final RestTemplate restTemplate;

    @Autowired
    private StudentService studentService;

    public KeycloakAdminClientService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    /*
    * This method creates a new user in the Keycloak server.
    * It sends a POST request to the Keycloak server with the user details.
    * The user details include the username, password, and email.
    * The response from the server is returned as a ResponseEntity<String>.
     */

    public ResponseEntity<String> createUser(String username, String password) {
        logger.info("Creating user: {}", username);

        try {
            String token = getAdminAccessToken();
            logger.debug("Token received: {}", token);

            HttpHeaders headers = createHeadersWithToken(token);
            String createUserUrl = String.format("%s/admin/realms/%s/users", keycloakAdminUrl, keycloakRealm);
            Map<String, Object> user = Map.of(
                    "username", username,
                    "enabled", true,
                    "credentials", Collections.singletonList(Map.of(
                            "type", "password",
                            "value", password,
                            "temporary", false
                    ))
            );

            HttpEntity<Map<String, Object>> requestEntity = new HttpEntity<>(user, headers);
            return restTemplate.postForEntity(createUserUrl, requestEntity, String.class);
        } catch (Exception e) {
            logger.error("Error creating user: {}", e.getMessage(), e);
            throw new RuntimeException("Failed to create user", e);
        }
    }

    /*
    * This method logs in a user in the Keycloak server.
    * It sends a POST request to the Keycloak server with the user credentials.
    * The user credentials include the username and password.
    * The response from the server is returned as a ResponseEntity<String>.
     */

    public ResponseEntity<Map<String, Object>> loginUser(String username, String password) {
        logger.info("Logging in user: {}", username);

        try {
            String loginUrl = String.format("%s/realms/%s/protocol/openid-connect/token", keycloakAdminUrl, keycloakRealm);

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

            String body = String.format("grant_type=password&client_id=student-rest-api&username=%s&password=%s",
                    username, password);

            HttpEntity<String> requestEntity = new HttpEntity<>(body, headers);
            ResponseEntity<String> response = restTemplate.exchange(loginUrl, HttpMethod.POST, requestEntity, String.class);

            if (response.getStatusCode().is2xxSuccessful()) {
                String responseBody = response.getBody();
                String accessToken = extractAccessToken(responseBody);
                List<String> roles = extractRolesFromToken(accessToken);

                String role = roles.stream()
                        .filter(VALID_ROLES::contains)
                        .min((r1, r2) -> Integer.compare(VALID_ROLES.indexOf(r1), VALID_ROLES.indexOf(r2)))
                        .orElse("unknown");

                Map<String, Object> responseBodyMap = new HashMap<>();
                responseBodyMap.put("access_token", accessToken);
                responseBodyMap.put("role", role);

                return ResponseEntity.ok(responseBodyMap);
            } else {
                return ResponseEntity.status(response.getStatusCode()).body(null);
            }
        } catch (Exception e) {
            logger.error("Error logging in user: {}", e.getMessage(), e);
            throw new RuntimeException("Failed to login user", e);
        }
    }

    private String extractAccessToken(String responseBody) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            Map<String, Object> map = mapper.readValue(responseBody, Map.class);
            return (String) map.get("access_token");
        } catch (Exception e) {
            throw new RuntimeException("Failed to parse access token from response", e);
        }
    }

    private List<String> extractRolesFromToken(String token) {
        try {
            String[] parts = token.split("\\.");
            String payload = new String(Base64.getUrlDecoder().decode(parts[1]));
            ObjectMapper mapper = new ObjectMapper();
            Map<String, Object> payloadMap = mapper.readValue(payload, Map.class);
            Map<String, Object> realmAccess = (Map<String, Object>) payloadMap.get("realm_access");
            return (List<String>) realmAccess.get("roles");
        } catch (Exception e) {
            throw new RuntimeException("Failed to extract roles from token", e);
        }
    }

    /*
    * This method assigns a role to a user in the Keycloak server.
    * It sends a POST request to the Keycloak server with the user ID and role name.
    * The user ID is the unique identifier of the user in the Keycloak server.
    * The role name is the name of the role that needs to be assigned to the user.
     */

    public ResponseEntity<String> assignRole(String userId, String roleName) {
        try {
            String token = getAdminAccessToken();
            logger.debug("Adding role {} to user {}", roleName, userId);
            HttpHeaders headers = createHeadersWithToken(token);

            String roleUrl = String.format("%s/admin/realms/%s/roles/%s", keycloakAdminUrl, keycloakRealm, roleName);
            ResponseEntity<Map> roleResponse = restTemplate.exchange(roleUrl, HttpMethod.GET, new HttpEntity<>(headers), Map.class);

            if (!roleResponse.getStatusCode().is2xxSuccessful()) {
                logger.error("Failed to get role: HTTP Status {}", roleResponse.getStatusCode());
                throw new RuntimeException("Failed to get role: " + roleResponse.getStatusCode());
            }

            Map<String, Object> role = roleResponse.getBody();
            String roleId = (String) role.get("id");

            Map<String, Object> roleRepresentation = new HashMap<>();
            roleRepresentation.put("id", roleId);
            roleRepresentation.put("name", roleName);
            Map<String, Object>[] roles = new Map[]{roleRepresentation};

            String assignRoleUrl = String.format("%s/admin/realms/%s/users/%s/role-mappings/realm", keycloakAdminUrl, keycloakRealm, userId);
            HttpEntity<Map<String, Object>[]> requestEntity = new HttpEntity<>(roles, headers);
            restTemplate.postForEntity(assignRoleUrl, requestEntity, String.class);

            /*switch (roleName) {
                case "student":
                    studentService.addStudent(new UserIDRequest(userId));
                    break;
                case "parent":
                    studentService.addParent(new UserIDRequest(userId));
                    break;
                case "teacher":
                    studentService.addTeacher(new UserIDRequest(userId));
                    break;
                case "admin":
                    studentService.addAdmin(new UserIDRequest(userId));
                    break;

            }*/

        } catch (Exception e) {
            logger.error("Error assigning role: {}", e.getMessage(), e);
            throw new RuntimeException("Failed to assign role", e);
        }
        return ResponseEntity.ok("Role assigned successfully");
    }

    public ResponseEntity<String> assignRoleUsername(String username, String roleName) {
        try {
            String token = getAdminAccessToken();
            logger.debug("Adding role {} to user {}", roleName, username);
            HttpHeaders headers = createHeadersWithToken(token);

            // Step 1: Get the user ID from the username
            String getUserIdUrl = String.format("%s/admin/realms/%s/users?username=%s", keycloakAdminUrl, keycloakRealm, username);
            ResponseEntity<Map[]> userResponse = restTemplate.exchange(getUserIdUrl, HttpMethod.GET, new HttpEntity<>(headers), Map[].class);

            if (!userResponse.getStatusCode().is2xxSuccessful() || userResponse.getBody() == null || userResponse.getBody().length == 0) {
                logger.error("Failed to get user ID: HTTP Status {}", userResponse.getStatusCode());
                logger.error("User response body: {}", (Object) userResponse.getBody());
                throw new RuntimeException("Failed to get user ID: " + userResponse.getStatusCode());
            }

            String userId = (String) userResponse.getBody()[0].get("id");

            // Step 2: Get the role ID from the role name
            String roleUrl = String.format("%s/admin/realms/%s/roles/%s", keycloakAdminUrl, keycloakRealm, roleName);
            ResponseEntity<Map> roleResponse = restTemplate.exchange(roleUrl, HttpMethod.GET, new HttpEntity<>(headers), Map.class);

            if (!roleResponse.getStatusCode().is2xxSuccessful()) {
                logger.error("Failed to get role: HTTP Status {}", roleResponse.getStatusCode());
                throw new RuntimeException("Failed to get role: " + roleResponse.getStatusCode());
            }

            Map<String, Object> role = roleResponse.getBody();
            String roleId = (String) role.get("id");

            Map<String, Object> roleRepresentation = new HashMap<>();
            roleRepresentation.put("id", roleId);
            roleRepresentation.put("name", roleName);
            Map<String, Object>[] roles = new Map[]{roleRepresentation};

            // Step 3: Assign the role to the user
            String assignRoleUrl = String.format("%s/admin/realms/%s/users/%s/role-mappings/realm", keycloakAdminUrl, keycloakRealm, userId);
            HttpEntity<Map<String, Object>[]> requestEntity = new HttpEntity<>(roles, headers);
            restTemplate.postForEntity(assignRoleUrl, requestEntity, String.class);

        } catch (Exception e) {
            logger.error("Error assigning role: {}", e.getMessage(), e);
            throw new RuntimeException("Failed to assign role", e);
        }
        return ResponseEntity.ok("Role assigned successfully");
    }

    /*
    * This method gets the access token for the Keycloak admin user.
    * It sends a POST request to the Keycloak server with the admin username and password.
    * The response from the server is returned as a String.
     */

    private String getAdminAccessToken() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        String url = String.format("%s/realms/master/protocol/openid-connect/token", keycloakAdminUrl);
        String body = String.format("grant_type=password&client_id=admin-cli&username=%s&password=%s", keycloakAdminUsername, keycloakAdminPassword);
        HttpEntity<String> request = new HttpEntity<>(body, headers);

        logger.info("Requesting access token from {}", url);
        ResponseEntity<Map> response = restTemplate.postForEntity(url, request, Map.class);
        logger.info("Access token request response: {}", response.getStatusCode());

        if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {
            return (String) response.getBody().get("access_token");
        } else {
            logger.error("Failed to get access token: {}", response.getStatusCode());
            throw new RuntimeException("Failed to get access token: " + response.getStatusCode());
        }
    }

    public ResponseEntity<String> deleteUser(String userId) {
        try {
            String token = getAdminAccessToken();
            logger.info("Deleting user: {}", userId);
            HttpHeaders headers = createHeadersWithToken(token);

            String deleteUserUrl = String.format("%s/admin/realms/%s/users/%s", keycloakAdminUrl, keycloakRealm, userId);
            HttpEntity<Void> requestEntity = new HttpEntity<>(headers);

            restTemplate.exchange(deleteUserUrl, HttpMethod.DELETE, requestEntity, String.class);
            return ResponseEntity.ok("User deleted successfully");
        } catch (Exception e) {
            logger.error("Error deleting user: {}", e.getMessage(), e);
            throw new RuntimeException("Failed to delete user", e);
        }
    }

    /*
     * Updates a user's credentials in Keycloak
     */
    public ResponseEntity<String> updateUserCredentials(UserUpdateCredentialsRequest userUpdateCredentialsRequest) {
        try {
            String token = getAdminAccessToken();
            HttpHeaders headers = createHeadersWithToken(token);


            if (userUpdateCredentialsRequest.getUsername() != null && !userUpdateCredentialsRequest.getUsername().isEmpty()) {
                updateUsername(userUpdateCredentialsRequest.getUserID(), userUpdateCredentialsRequest.getUsername(), headers);
            }

            if (userUpdateCredentialsRequest.getPassword() != null && !userUpdateCredentialsRequest.getPassword().isEmpty()) {
                updatePassword(userUpdateCredentialsRequest.getUserID(), userUpdateCredentialsRequest.getPassword(), headers);
            }

            return ResponseEntity.ok("User credentials updated successfully");
        } catch (Exception e) {
            logger.error("Error updating user credentials: {}", e.getMessage(), e);
            throw new RuntimeException("Failed to update user credentials", e);
        }
    }

    private void updateUsername(String userId, String newUsername, HttpHeaders headers) {
        try {
            logger.info("Updating username for user: {}", userId);

            String updateUsernameUrl = String.format("%s/admin/realms/%s/users/%s", keycloakAdminUrl, keycloakRealm, userId);
            Map<String, Object> user = new HashMap<>();
            user.put("username", newUsername);

            HttpEntity<Map<String, Object>> requestEntity = new HttpEntity<>(user, headers);
            restTemplate.put(updateUsernameUrl, requestEntity);

            logger.info("Username updated successfully for user: {}", userId);
        } catch (Exception e) {
            logger.error("Error updating username: {}", e.getMessage(), e);
            throw new RuntimeException("Failed to update username", e);
        }
    }

    private void updatePassword(String userId, String newPassword, HttpHeaders headers) {
        try {
            logger.info("Updating password for user: {}", userId);

            String updatePasswordUrl = String.format("%s/admin/realms/%s/users/%s/reset-password", keycloakAdminUrl, keycloakRealm, userId);
            Map<String, Object> credential = new HashMap<>();
            credential.put("type", "password");
            credential.put("value", newPassword);
            credential.put("temporary", false);

            HttpEntity<Map<String, Object>> requestEntity = new HttpEntity<>(credential, headers);
            restTemplate.put(updatePasswordUrl, requestEntity);

            logger.info("Password updated successfully for user: {}", userId);
        } catch (Exception e) {
            logger.error("Error updating password: {}", e.getMessage(), e);
            throw new RuntimeException("Failed to update password", e);
        }
    }

    /*
     * Removes a role from a user in Keycloak
     */
    public ResponseEntity<String> removeRole(String userId, String roleName) {
        try {
            String token = getAdminAccessToken();
            logger.info("Removing role {} from user {}", roleName, userId);
            HttpHeaders headers = createHeadersWithToken(token);

            String roleUrl = String.format("%s/admin/realms/%s/roles/%s", keycloakAdminUrl, keycloakRealm, roleName);
            ResponseEntity<Map> roleResponse = restTemplate.exchange(roleUrl, HttpMethod.GET, new HttpEntity<>(headers), Map.class);

            if (!roleResponse.getStatusCode().is2xxSuccessful()) {
                logger.error("Failed to get role: HTTP Status {}", roleResponse.getStatusCode());
                throw new RuntimeException("Failed to get role: " + roleResponse.getStatusCode());
            }

            Map<String, Object> role = roleResponse.getBody();
            String roleId = (String) role.get("id");

            Map<String, Object> roleRepresentation = new HashMap<>();
            roleRepresentation.put("id", roleId);
            roleRepresentation.put("name", roleName);
            Map<String, Object>[] roles = new Map[]{roleRepresentation};

            String removeRoleUrl = String.format("%s/admin/realms/%s/users/%s/role-mappings/realm", keycloakAdminUrl, keycloakRealm, userId);
            HttpEntity<Map<String, Object>[]> requestEntity = new HttpEntity<>(roles, headers);
            restTemplate.exchange(removeRoleUrl, HttpMethod.DELETE, requestEntity, String.class);

            return ResponseEntity.ok("Role removed successfully");
        } catch (Exception e) {
            logger.error("Error removing role: {}", e.getMessage(), e);
            throw new RuntimeException("Failed to remove role", e);
        }
    }

    public ResponseEntity<UserInfoResponse> getUserInfo(Jwt jwt) {
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



    private HttpHeaders createHeadersWithToken(String token) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(token);
        return headers;
    }
}
