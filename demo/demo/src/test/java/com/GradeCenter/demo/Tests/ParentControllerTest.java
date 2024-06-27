package com.GradeCenter.demo.Tests;

import com.GradeCenter.controllers.ParentController;
import com.GradeCenter.dtos.ParentDto;
import com.GradeCenter.dtos.ParentUpdateDto;
import com.GradeCenter.dtos.UserIDRequest;
import com.GradeCenter.service.ParentService;
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

class ParentControllerTest {

    @Mock
    private ParentService parentService;

    @InjectMocks
    private ParentController parentController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getAllParents() {
        List<ParentDto> parents = Arrays.asList(new ParentDto(), new ParentDto());
        when(parentService.getAllParents()).thenReturn(parents);

        List<ParentDto> result = parentController.getAllParents();

        assertEquals(parents, result);
        verify(parentService).getAllParents();
    }

    @Test
    void getPersonalParent() {
        String userId = "testUserId";
        ParentDto parentDto = new ParentDto();

        // Mock SecurityContext and Authentication
        SecurityContext securityContext = mock(SecurityContext.class);
        Authentication authentication = mock(Authentication.class);
        Jwt jwt = mock(Jwt.class);

        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.getPrincipal()).thenReturn(jwt);
        when(jwt.getClaimAsString("sub")).thenReturn(userId);
        SecurityContextHolder.setContext(securityContext);

        when(parentService.getParentByUId(userId)).thenReturn(parentDto);

        ResponseEntity<ParentDto> response = parentController.getPersonalParent();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(parentDto, response.getBody());
        verify(parentService).getParentByUId(userId);
    }

    @Test
    void getParentById() {
        Long id = 1L;
        ParentDto parentDto = new ParentDto();
        when(parentService.getParentById(id)).thenReturn(parentDto);

        ResponseEntity<ParentDto> response = parentController.getParentById(id);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(parentDto, response.getBody());
        verify(parentService).getParentById(id);
    }

    @Test
    void getParentByUId() {
        String userId = "testUserId";
        ParentDto parentDto = new ParentDto();
        when(parentService.getParentByUId(userId)).thenReturn(parentDto);

        ResponseEntity<ParentDto> response = parentController.getParentByUId(userId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(parentDto, response.getBody());
        verify(parentService).getParentByUId(userId);
    }

    @Test
    void addParent() {
        UserIDRequest request = new UserIDRequest("testid");
        ParentDto parentDto = new ParentDto();
        when(parentService.addParent(request)).thenReturn(parentDto);

        ResponseEntity<ParentDto> response = parentController.addParent(request);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(parentDto, response.getBody());
        verify(parentService).addParent(request);
    }

    @Test
    void deleteParentId() {
        Long id = 1L;
        when(parentService.deleteParentID(id)).thenReturn(true);

        ResponseEntity<String> response = parentController.deleteParentId(id);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Parent deleted successfully", response.getBody());
        verify(parentService).deleteParentID(id);
    }

    @Test
    void deleteParentUID() {
        String userId = "testUserId";
        when(parentService.deleteParentUID(userId)).thenReturn(true);

        ResponseEntity<String> response = parentController.deleteParentUID(userId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Parent deleted successfully", response.getBody());
        verify(parentService).deleteParentUID(userId);
    }

    @Test
    void updateParentID() {
        Long id = 1L;
        ParentUpdateDto updateDto = new ParentUpdateDto();
        ParentDto parentDto = new ParentDto();
        when(parentService.updateParentID(id, updateDto)).thenReturn(parentDto);

        ResponseEntity<ParentDto> response = parentController.updateParentID(id, updateDto);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(parentDto, response.getBody());
        verify(parentService).updateParentID(id, updateDto);
    }

    @Test
    void updateParentUID() {
        String userId = "testUserId";
        ParentUpdateDto updateDto = new ParentUpdateDto();
        ParentDto parentDto = new ParentDto();
        when(parentService.updateParentUID(userId, updateDto)).thenReturn(parentDto);

        ResponseEntity<ParentDto> response = parentController.updateParentUID(userId, updateDto);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(parentDto, response.getBody());
        verify(parentService).updateParentUID(userId, updateDto);
    }
}