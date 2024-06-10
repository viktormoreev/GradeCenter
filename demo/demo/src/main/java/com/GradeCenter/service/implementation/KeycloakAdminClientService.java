package com.GradeCenter.service.implementation;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

@Service
public class KeycloakAdminClientService {

    private static final Logger logger = LoggerFactory.getLogger(KeycloakAdminClientService.class);

    @Value("${keycloak.admin.url}")
    private String keycloakAdminUrl;

    @Value("${keycloak.admin.username}")
    private String keycloakAdminUsername;

    @Value("${keycloak.admin.password}")
    private String keycloakAdminPassword;

    @Value("${keycloak.realm}")
    private String keycloakRealm;

    private final RestTemplate restTemplate;

    public KeycloakAdminClientService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    /*
    * This method creates a new user in the Keycloak server.
    * It sends a POST request to the Keycloak server with the user details.
    * The user details include the username, password, and email.
    * The response from the server is returned as a ResponseEntity<String>.
     */

    public ResponseEntity<String> createUser(String username, String password, String email) {
        logger.info("Creating user: {}", username);

        try {
            String token = getAdminAccessToken();
            logger.debug("Token received: {}", token);

            HttpHeaders headers = createHeadersWithToken(token);
            String createUserUrl = String.format("%s/admin/realms/%s/users", keycloakAdminUrl, keycloakRealm);
            Map<String, Object> user = Map.of(
                    "username", username,
                    "email", email,
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

    public ResponseEntity<String> loginUser(String username, String password) {
        logger.info("Logging in user: {}", username);

        try {
            String loginUrl = String.format("%s/realms/%s/protocol/openid-connect/token", keycloakAdminUrl, keycloakRealm);

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

            String body = String.format("grant_type=password&client_id=student-rest-api&username=%s&password=%s",
                     username, password);

            HttpEntity<String> requestEntity = new HttpEntity<>(body, headers);
            return restTemplate.exchange(loginUrl, HttpMethod.POST, requestEntity, String.class);
        } catch (Exception e) {
            logger.error("Error logging in user: {}", e.getMessage(), e);
            throw new RuntimeException("Failed to login user", e);
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

            // Step 1: Get the role details
            String roleUrl = String.format("%s/admin/realms/%s/roles/%s", keycloakAdminUrl, keycloakRealm, roleName);
            ResponseEntity<Map> roleResponse = restTemplate.exchange(roleUrl, HttpMethod.GET, new HttpEntity<>(headers), Map.class);

            if (roleResponse.getStatusCode().is2xxSuccessful()) {
                Map<String, Object> role = roleResponse.getBody();
                String roleId = (String) role.get("id");

                // Step 2: Prepare the role representation array
                Map<String, Object> roleRepresentation = new HashMap<>();
                roleRepresentation.put("id", roleId);
                roleRepresentation.put("name", roleName);
                Map<String, Object>[] roles = new Map[]{roleRepresentation};

                // Step 3: Assign the role to the user
                String assignRoleUrl = String.format("%s/admin/realms/%s/users/%s/role-mappings/realm", keycloakAdminUrl, keycloakRealm, userId);
                HttpEntity<Map<String, Object>[]> requestEntity = new HttpEntity<>(roles, headers);
                restTemplate.postForEntity(assignRoleUrl, requestEntity, String.class);

            } else {
                logger.error("Failed to get role: HTTP Status {}", roleResponse.getStatusCode());
                throw new RuntimeException("Failed to get role: " + roleResponse.getStatusCode());
            }

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

    /*
    * This method gets the role mappings for a user in the Keycloak server.
    * It sends a GET request to the Keycloak server with the user ID.
    * The user ID is the unique identifier of the user in the Keycloak server.
    * The response from the server is returned as a Map<String, Object>.
     */

    public Map<String, Object> getUserRoleMappings(String userId) {
        try {
            String token = getAdminAccessToken();
            logger.debug("Getting role mappings for user {}", userId);
            HttpHeaders headers = createHeadersWithToken(token);

            String getRoleMappingsUrl = String.format("%s/admin/realms/%s/users/%s/role-mappings", keycloakAdminUrl, keycloakRealm, userId);
            ResponseEntity<Map> roleMappingsResponse = restTemplate.exchange(getRoleMappingsUrl, HttpMethod.GET, new HttpEntity<>(headers), Map.class);

            if (roleMappingsResponse.getStatusCode().is2xxSuccessful()) {
                return roleMappingsResponse.getBody();
            } else {
                logger.error("Failed to get role mappings: HTTP Status {}", roleMappingsResponse.getStatusCode());
                throw new RuntimeException("Failed to get role mappings: " + roleMappingsResponse.getStatusCode());
            }
        } catch (Exception e) {
            logger.error("Error getting role mappings: {}", e.getMessage(), e);
            throw new RuntimeException("Failed to get role mappings", e);
        }
    }

    /*
    * This method creates HTTP headers with the access token.
     */

    private HttpHeaders createHeadersWithToken(String token) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(token);
        return headers;
    }
}
