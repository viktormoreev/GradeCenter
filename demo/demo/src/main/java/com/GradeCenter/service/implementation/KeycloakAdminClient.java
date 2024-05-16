package com.GradeCenter.service.implementation;

import com.GradeCenter.dtos.KeycloakUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class KeycloakAdminClient {

    @Autowired
    private RestTemplate restTemplate;

    @Value("${keycloak.admin.url}")
    private String keycloakAdminUrl;

    @Value("${keycloak.admin.username}")
    private String keycloakAdminUsername;

    @Value("${keycloak.admin.password}")
    private String keycloakAdminPassword;

    public void createUser(String username, String password) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBasicAuth(keycloakAdminUsername, keycloakAdminPassword);

        String createUserUrl = keycloakAdminUrl + "/admin/realms/{realm}/users";
        KeycloakUser user = new KeycloakUser(username, password);

        HttpEntity<KeycloakUser> requestEntity = new HttpEntity<>(user, headers);
        ResponseEntity<String> responseEntity = restTemplate.postForEntity(createUserUrl, requestEntity, String.class, "GradeCenter");

        if (responseEntity.getStatusCode().is2xxSuccessful()) {
            System.out.println("User created successfully");
        } else {
            System.out.println("Failed to create user: " + responseEntity.getBody());
        }
    }

    // Define additional methods for interacting with Keycloak Admin API as needed
}