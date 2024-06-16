package com.GradeCenter.demo;

import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.resource.RealmResource;
import org.keycloak.admin.client.resource.UsersResource;
import org.mockito.Mockito;
import com.GradeCenter.dtos.ApiResponse;
import com.GradeCenter.dtos.UserIDRequest;
import com.GradeCenter.service.DirectorService;
import com.GradeCenter.service.ParentService;
import com.GradeCenter.service.StudentService;
import com.GradeCenter.service.TeacherService;
import com.GradeCenter.service.implementation.KeycloakAdminClientService;
import com.GradeCenter.service.implementation.UserEntityHandlingService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.keycloak.representations.idm.UserRepresentation;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.keycloak.admin.client.resource.UserResource;
import org.keycloak.representations.idm.RoleRepresentation;
import org.mockito.ArgumentCaptor;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

//Depricated needs work
@ExtendWith(MockitoExtension.class)
class UserEntityHandlingServiceTest {
/*
    @Mock
    private KeycloakAdminClientService keycloakAdminClientService;

    @Mock
    private DirectorService directorService;

    @Mock
    private TeacherService teacherService;

    @Mock
    private ParentService parentService;

    @Mock
    private StudentService studentService;

    @InjectMocks
    private UserEntityHandlingService userEntityHandlingService;
    @Mock
    private Keycloak keycloak; // Mock the Keycloak instance

    @Mock
    private RealmResource realmResource; // Mock the RealmResource

    @Mock
    private UsersResource usersResource; // Mock the UsersResource



    @BeforeEach
    void setUp() {
        // Setup the chain of mocks
        when(keycloakAdminClientService.keycloak).thenReturn(keycloak);
        when(keycloak.realm(anyString())).thenReturn(realmResource);
        when(realmResource.users()).thenReturn(usersResource);
    }

    @Test
    void testMethod() {
        // Example usage
        when(usersResource.search(eq("someUser"))).thenReturn(new ArrayList<>()); // Use the mock
        // Your test logic here
    }

    @Test
    void switchUserRoleByUsername_UserNotFound() {
        ApiResponse<String> response = userEntityHandlingService.switchUserRoleByUsername("nonexistentUser", "teacher");
        assertFalse(response.isSuccess());
        assertEquals("User not found", response.getMessage());
        verify(keycloakAdminClientService.keycloak.realm(anyString()).users(), times(1)).search(eq("nonexistentUser"));
    }


    @Test
    void switchUserRoleByUsername_SuccessfulRoleSwitch() {
        // Assume createEntityForRole performs as expected
        doNothing().when(teacherService).addTeacher(any(UserIDRequest.class));

        ApiResponse<String> response = userEntityHandlingService.switchUserRoleByUsername("existingUser", "teacher");

        assertTrue(response.isSuccess());
        assertEquals("Role switched successfully", response.getMessage());
        verify(teacherService, times(1)).addTeacher(any(UserIDRequest.class));
    }

    @Test
    void assignRole_UserNotFound() {
        when(keycloakAdminClientService.keycloak.realm(anyString()).users().search(eq("nonexistentUser"))).thenReturn(Collections.emptyList());

        ApiResponse<String> response = userEntityHandlingService.assignRoleUsername("nonexistentUser", "director");

        assertFalse(response.isSuccess());
        assertEquals("User not found", response.getMessage());
    }

    @Test
    void removeRole_SuccessfulRemoval() {
        doNothing().when(directorService).deleteDirectorUID(anyString());

        ApiResponse<String> response = userEntityHandlingService.removeRole("123", "director");

        assertTrue(response.isSuccess());
        assertEquals("Role removed successfully", response.getMessage());
        verify(directorService, times(1)).deleteDirectorUID(eq("123"));
    }

    // Test error handling when Keycloak throws an exception
    @Test
    void switchUserRoleByUsername_KeycloakError() {
        when(keycloakAdminClientService.keycloak.realm(anyString()).users().search(eq("errorUser"))).thenThrow(new RuntimeException("Keycloak error"));

        ApiResponse<String> response = userEntityHandlingService.switchUserRoleByUsername("errorUser", "teacher");

        assertFalse(response.isSuccess());
        assertTrue(response.getMessage().contains("Failed to switch role"));
    }
*/
    // Additional tests for error handling, incorrect role names, etc.
}
