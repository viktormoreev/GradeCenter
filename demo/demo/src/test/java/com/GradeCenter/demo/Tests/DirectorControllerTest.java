package com.GradeCenter.demo.Tests;

import com.GradeCenter.controllers.DirectorController;
import com.GradeCenter.dtos.DirectorDto;
import com.GradeCenter.dtos.DirectorUpdateDto;
import com.GradeCenter.dtos.SchoolDto;
import com.GradeCenter.dtos.UserIDRequest;
import com.GradeCenter.service.DirectorService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class DirectorControllerTest {

    @Mock
    private DirectorService directorService;

    @InjectMocks
    private DirectorController directorController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getAllDirectors() {
        DirectorDto director1 = new DirectorDto();
        DirectorDto director2 = new DirectorDto();
        List<DirectorDto> directors = Arrays.asList(director1, director2);

        when(directorService.getAllDirectors()).thenReturn(directors);

        List<DirectorDto> result = directorController.getAllDirectors();

        assertEquals(directors, result);
        verify(directorService).getAllDirectors();
    }

    @Test
    void getPersonalDirector() {
        String userId = "testUserId";
        DirectorDto directorDto = new DirectorDto();

        // Mock SecurityContextHolder
        Jwt jwt = mock(Jwt.class);
        when(jwt.getClaimAsString("sub")).thenReturn(userId);

        Authentication authentication = mock(Authentication.class);
        when(authentication.getPrincipal()).thenReturn(jwt);

        SecurityContext securityContext = mock(SecurityContext.class);
        when(securityContext.getAuthentication()).thenReturn(authentication);

        SecurityContextHolder.setContext(securityContext);

        when(directorService.getDirectorByUId(userId)).thenReturn(directorDto);

        ResponseEntity<DirectorDto> response = directorController.getPersonalDirector();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(directorDto, response.getBody());
        verify(directorService).getDirectorByUId(userId);
    }

    @Test
    void getDirectorById() {
        Long id = 1L;
        DirectorDto directorDto = new DirectorDto();

        when(directorService.getDirectorById(id)).thenReturn(directorDto);

        ResponseEntity<DirectorDto> response = directorController.getDirectorById(id);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(directorDto, response.getBody());
        verify(directorService).getDirectorById(id);
    }

    @Test
    void addDirector() {
        UserIDRequest userIDRequest = new UserIDRequest("testid");
        DirectorDto directorDto = new DirectorDto();

        when(directorService.addDirector(userIDRequest)).thenReturn(directorDto);

        ResponseEntity<DirectorDto> response = directorController.addDirector(userIDRequest);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(directorDto, response.getBody());
        verify(directorService).addDirector(userIDRequest);
    }

    @Test
    void addDirectorToSchool() {
        Long directorId = 1L;
        Long schoolId = 1L;
        SchoolDto schoolDto = new SchoolDto();

        when(directorService.addDirectorToSchool(directorId, schoolId)).thenReturn(schoolDto);

        ResponseEntity<SchoolDto> response = directorController.addDirectorToSchool(directorId, schoolId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(schoolDto, response.getBody());
        verify(directorService).addDirectorToSchool(directorId, schoolId);
    }

    @Test
    void fireDirectorFromSchool() {
        Long schoolId = 1L;
        SchoolDto schoolDto = new SchoolDto();

        when(directorService.removeDirectorFromSchool(schoolId)).thenReturn(schoolDto);

        ResponseEntity<SchoolDto> response = directorController.fireDirectorFromSchool(schoolId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(schoolDto, response.getBody());
        verify(directorService).removeDirectorFromSchool(schoolId);
    }

    @Test
    void getDirectorByUId() {
        String userID = "testUserId";
        DirectorDto directorDto = new DirectorDto();

        when(directorService.getDirectorByUId(userID)).thenReturn(directorDto);

        ResponseEntity<DirectorDto> response = directorController.getDirectorByUId(userID);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(directorDto, response.getBody());
        verify(directorService).getDirectorByUId(userID);
    }

    @Test
    void getDirectorByUId_NotFound() {
        String userID = "nonExistentUserId";

        when(directorService.getDirectorByUId(userID)).thenReturn(null);

        ResponseEntity<DirectorDto> response = directorController.getDirectorByUId(userID);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        verify(directorService).getDirectorByUId(userID);
    }

    @Test
    void deleteDirectorUID() {
        String userID = "testUserId";

        when(directorService.deleteDirectorUID(userID)).thenReturn(true);

        ResponseEntity<String> response = directorController.deleteDirectorUID(userID);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Director deleted successfully", response.getBody());
        verify(directorService).deleteDirectorUID(userID);
    }

    @Test
    void deleteDirectorUID_NotFound() {
        String userID = "nonExistentUserId";

        when(directorService.deleteDirectorUID(userID)).thenReturn(false);

        ResponseEntity<String> response = directorController.deleteDirectorUID(userID);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("Failed to delete director", response.getBody());
        verify(directorService).deleteDirectorUID(userID);
    }

    @Test
    void deleteDirectorId() {
        Long id = 1L;

        when(directorService.deleteDirectorID(id)).thenReturn(true);

        ResponseEntity<String> response = directorController.deleteDirectorId(id);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Director deleted successfully", response.getBody());
        verify(directorService).deleteDirectorID(id);
    }

    @Test
    void deleteDirectorId_NotFound() {
        Long id = 1L;

        when(directorService.deleteDirectorID(id)).thenReturn(false);

        ResponseEntity<String> response = directorController.deleteDirectorId(id);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("Failed to delete director", response.getBody());
        verify(directorService).deleteDirectorID(id);
    }

    @Test
    void updateDirectorID() {
        Long id = 1L;
        DirectorUpdateDto updateDto = new DirectorUpdateDto(2L);
        DirectorDto updatedDirectorDto = new DirectorDto();

        when(directorService.updateDirectorID(id, updateDto)).thenReturn(updatedDirectorDto);

        ResponseEntity<DirectorDto> response = directorController.updateDirectorID(id, updateDto);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(updatedDirectorDto, response.getBody());
        verify(directorService).updateDirectorID(id, updateDto);
    }

    @Test
    void updateDirectorID_NotFound() {
        Long id = 1L;
        DirectorUpdateDto updateDto = new DirectorUpdateDto(2L);

        when(directorService.updateDirectorID(id, updateDto)).thenReturn(null);

        ResponseEntity<DirectorDto> response = directorController.updateDirectorID(id, updateDto);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        verify(directorService).updateDirectorID(id, updateDto);
    }

    @Test
    void updateDirectorUID() {
        String userID = "testUserId";
        DirectorUpdateDto updateDto = new DirectorUpdateDto(2L);
        DirectorDto updatedDirectorDto = new DirectorDto();

        when(directorService.updateDirectorUID(userID, updateDto)).thenReturn(updatedDirectorDto);

        ResponseEntity<DirectorDto> response = directorController.updateDirectorUID(userID, updateDto);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(updatedDirectorDto, response.getBody());
        verify(directorService).updateDirectorUID(userID, updateDto);
    }

    @Test
    void updateDirectorUID_NotFound() {
        String userID = "nonExistentUserId";
        DirectorUpdateDto updateDto = new DirectorUpdateDto(2L);

        when(directorService.updateDirectorUID(userID, updateDto)).thenReturn(null);

        ResponseEntity<DirectorDto> response = directorController.updateDirectorUID(userID, updateDto);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        verify(directorService).updateDirectorUID(userID, updateDto);
    }
}