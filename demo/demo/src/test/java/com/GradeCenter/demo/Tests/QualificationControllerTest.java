package com.GradeCenter.demo.Tests;

import com.GradeCenter.controllers.QualificationController;
import com.GradeCenter.dtos.QualificationDto;
import com.GradeCenter.exceptions.QualificationNotFoundException;
import com.GradeCenter.service.QualificationService;
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
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

class QualificationControllerTest {

    @Mock
    private QualificationService qualificationService;

    @InjectMocks
    private QualificationController qualificationController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        setupMockSecurityContext();
    }

    private void setupMockSecurityContext() {
        SecurityContext securityContext = mock(SecurityContext.class);
        Authentication authentication = mock(Authentication.class);
        Jwt jwt = mock(Jwt.class);

        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.getPrincipal()).thenReturn(jwt);
        when(jwt.getClaimAsString("sub")).thenReturn("testUserId");
        SecurityContextHolder.setContext(securityContext);
    }

    @Test
    void getAllQualifications_ShouldReturnListOfQualifications() {
        List<QualificationDto> qualifications = Arrays.asList(new QualificationDto(), new QualificationDto());
        when(qualificationService.getAllQualifications()).thenReturn(qualifications);

        ResponseEntity<List<QualificationDto>> response = qualificationController.getAllQualifications();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(qualifications, response.getBody());
        verify(qualificationService).getAllQualifications();
    }

    @Test
    void getQualificationById_ExistingId_ShouldReturnQualification() throws QualificationNotFoundException {
        Long id = 1L;
        QualificationDto qualificationDto = new QualificationDto();
        when(qualificationService.getQualificationById(id)).thenReturn(qualificationDto);

        ResponseEntity<QualificationDto> response = qualificationController.getQualificationById(id);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(qualificationDto, response.getBody());
        verify(qualificationService).getQualificationById(id);
    }

    @Test
    void getQualificationById_NonExistingId_ShouldReturnNotFound() throws QualificationNotFoundException {
        Long id = 1L;
        when(qualificationService.getQualificationById(id)).thenThrow(new QualificationNotFoundException("Qualification not found"));

        ResponseEntity<QualificationDto> response = qualificationController.getQualificationById(id);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        verify(qualificationService).getQualificationById(id);
    }

    @Test
    void createQualification_ValidInput_ShouldReturnCreatedQualification() {
        QualificationDto inputDto = new QualificationDto();
        QualificationDto createdDto = new QualificationDto();
        when(qualificationService.createQualification(any(QualificationDto.class))).thenReturn(createdDto);

        ResponseEntity<QualificationDto> response = qualificationController.createQualification(inputDto);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(createdDto, response.getBody());
        verify(qualificationService).createQualification(any(QualificationDto.class));
    }

    @Test
    void updateQualification_ExistingIdAndValidInput_ShouldReturnUpdatedQualification() throws QualificationNotFoundException {
        Long id = 1L;
        QualificationDto inputDto = new QualificationDto();
        QualificationDto updatedDto = new QualificationDto();
        when(qualificationService.updateQualification(eq(id), any(QualificationDto.class))).thenReturn(updatedDto);

        ResponseEntity<QualificationDto> response = qualificationController.updateQualification(id, inputDto);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(updatedDto, response.getBody());
        verify(qualificationService).updateQualification(eq(id), any(QualificationDto.class));
    }

    @Test
    void updateQualification_NonExistingId_ShouldReturnNotFound() throws QualificationNotFoundException {
        Long id = 1L;
        QualificationDto inputDto = new QualificationDto();
        when(qualificationService.updateQualification(eq(id), any(QualificationDto.class)))
                .thenThrow(new QualificationNotFoundException("Qualification not found"));

        ResponseEntity<QualificationDto> response = qualificationController.updateQualification(id, inputDto);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        verify(qualificationService).updateQualification(eq(id), any(QualificationDto.class));
    }

    @Test
    void deleteQualification_ShouldReturnNoContent() {
        Long id = 1L;
        doNothing().when(qualificationService).deleteQualification(id);

        ResponseEntity<Void> response = qualificationController.deleteQualification(id);

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        verify(qualificationService).deleteQualification(id);
    }
}