package com.GradeCenter.demo.Tests;

import com.GradeCenter.controllers.AbsenceController;
import com.GradeCenter.dtos.AbsenceDto;
import com.GradeCenter.dtos.AbsenceStudentViewDto;
import com.GradeCenter.dtos.AbsenceTeacherViewDto;
import com.GradeCenter.exceptions.AbsenceNotFoundException;
import com.GradeCenter.exceptions.CourseNotFoundException;
import com.GradeCenter.exceptions.StudentNotFoundException;
import com.GradeCenter.service.AbsenceService;
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

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class AbsenceControllerTest {

    @Mock
    private AbsenceService absenceService;

    @InjectMocks
    private AbsenceController absenceController;

    @Mock
    private SecurityContext securityContext;

    @Mock
    private Authentication authentication;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        SecurityContextHolder.setContext(securityContext);
    }

    @Test
    void getAllAbsences_shouldReturnAllAbsences() {
        List<AbsenceDto> absences = Arrays.asList(new AbsenceDto(), new AbsenceDto());
        when(absenceService.getAllAbsences()).thenReturn(absences);

        List<AbsenceDto> result = absenceController.getAllAbsences();

        assertEquals(absences, result);
        verify(absenceService).getAllAbsences();
    }

    @Test
    void getPersonalStudentAbsences_shouldReturnAbsencesForStudent() throws StudentNotFoundException {
        // Arrange
        List<AbsenceStudentViewDto> absences = Arrays.asList(new AbsenceStudentViewDto(), new AbsenceStudentViewDto());
        Jwt jwt = mock(Jwt.class);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.getPrincipal()).thenReturn(jwt);
        when(jwt.getClaimAsString("sub")).thenReturn("user123");
        when(absenceService.getPersonalStudentAbsences("user123")).thenReturn(absences);

        ResponseEntity<List<AbsenceStudentViewDto>> response = absenceController.getPersonalStudentAbsences();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(absences, response.getBody());
        verify(absenceService).getPersonalStudentAbsences("user123");
    }

    @Test
    void createAbsence_shouldCreateAndReturnAbsence() throws CourseNotFoundException, StudentNotFoundException {
        AbsenceDto absenceDto = new AbsenceDto(1L, null, 1L, LocalDate.now());
        AbsenceDto createdAbsence = new AbsenceDto(1L, 1L, 1L, LocalDate.now());
        when(absenceService.createAbsence(absenceDto)).thenReturn(createdAbsence);

        ResponseEntity<AbsenceDto> response = absenceController.createAbsence(absenceDto);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(createdAbsence, response.getBody());
        verify(absenceService).createAbsence(absenceDto);
    }

    @Test
    void deleteAbsenceById_shouldDeleteAbsence() {
        Long absenceId = 1L;
        when(absenceService.deleteAbsenceById(absenceId)).thenReturn(true);

        ResponseEntity<String> response = absenceController.deleteAbsenceById(absenceId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Absence deleted successfully", response.getBody());
        verify(absenceService).deleteAbsenceById(absenceId);
    }

    @Test
    void updateAbsence_shouldUpdateAndReturnAbsence() throws AbsenceNotFoundException, StudentNotFoundException, CourseNotFoundException {
        Long absenceId = 1L;
        AbsenceDto absenceDto = new AbsenceDto(1L, 1L, 1L, LocalDate.now());
        AbsenceDto updatedAbsence = new AbsenceDto(1L, 1L, 1L, LocalDate.now().plusDays(1));
        when(absenceService.updateAbsence(absenceId, absenceDto)).thenReturn(updatedAbsence);

        ResponseEntity<AbsenceDto> response = absenceController.updateAbsence(absenceId, absenceDto);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(updatedAbsence, response.getBody());
        verify(absenceService).updateAbsence(absenceId, absenceDto);
    }

    @Test
    void getPersonalStudentAbsences_shouldReturnNoContentWhenNoAbsences() throws StudentNotFoundException {
        Jwt jwt = mock(Jwt.class);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.getPrincipal()).thenReturn(jwt);
        when(jwt.getClaimAsString("sub")).thenReturn("user123");
        when(absenceService.getPersonalStudentAbsences("user123")).thenReturn(Collections.emptyList());

        ResponseEntity<List<AbsenceStudentViewDto>> response = absenceController.getPersonalStudentAbsences();

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        assertNull(response.getBody());
        verify(absenceService).getPersonalStudentAbsences("user123");
    }

    @Test
    void getAbsencesByStudentId_shouldReturnAbsencesForStudent() throws StudentNotFoundException {
        long studentId = 1L;
        List<AbsenceDto> absences = Arrays.asList(new AbsenceDto(), new AbsenceDto());
        when(absenceService.getAllAbsencesByStudentIdForAdmin(studentId)).thenReturn(absences);

        ResponseEntity<List<AbsenceDto>> response = absenceController.getAbsencesByStudentId(studentId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(absences, response.getBody());
        verify(absenceService).getAllAbsencesByStudentIdForAdmin(studentId);
    }

    @Test
    void getAbsencesByStudentId_shouldReturnNotFoundWhenNoAbsences() throws StudentNotFoundException {
        long studentId = 1L;
        when(absenceService.getAllAbsencesByStudentIdForAdmin(studentId)).thenReturn(Collections.emptyList());

        ResponseEntity<List<AbsenceDto>> response = absenceController.getAbsencesByStudentId(studentId);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNull(response.getBody());
        verify(absenceService).getAllAbsencesByStudentIdForAdmin(studentId);
    }

    @Test
    void deleteAbsenceById_shouldReturnNotFoundWhenAbsenceDoesNotExist() {
        Long absenceId = 1L;
        when(absenceService.deleteAbsenceById(absenceId)).thenReturn(false);

        ResponseEntity<String> response = absenceController.deleteAbsenceById(absenceId);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("Failed to delete absence", response.getBody());
        verify(absenceService).deleteAbsenceById(absenceId);
    }

    @Test
    void updateAbsence_shouldReturnNotFoundWhenAbsenceDoesNotExist() throws AbsenceNotFoundException, StudentNotFoundException, CourseNotFoundException {

        Long absenceId = 1L;
        AbsenceDto absenceDto = new AbsenceDto(1L, 1L, 1L, LocalDate.now());
        when(absenceService.updateAbsence(absenceId, absenceDto)).thenThrow(new AbsenceNotFoundException("Absence not found"));

        ResponseEntity<AbsenceDto> response = absenceController.updateAbsence(absenceId, absenceDto);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNull(response.getBody());
        verify(absenceService).updateAbsence(absenceId, absenceDto);
    }

    @Test
    void getTeacherViewAbsencesByStudentId_shouldReturnAbsencesForStudent() throws AbsenceNotFoundException {
        long studentId = 1L;
        List<AbsenceTeacherViewDto> absences = Arrays.asList(new AbsenceTeacherViewDto(), new AbsenceTeacherViewDto());
        when(absenceService.getTeacherViewAbsencesByStudentId(studentId)).thenReturn(absences);

        ResponseEntity<List<AbsenceTeacherViewDto>> response = absenceController.getTeacherViewAbsencesByStudentId(studentId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(absences, response.getBody());
        verify(absenceService).getTeacherViewAbsencesByStudentId(studentId);
    }

    @Test
    void getTeacherViewAbsencesByStudentId_shouldReturnNotFoundWhenNoAbsences() throws AbsenceNotFoundException {
        long studentId = 1L;
        when(absenceService.getTeacherViewAbsencesByStudentId(studentId)).thenReturn(Collections.emptyList());

        ResponseEntity<List<AbsenceTeacherViewDto>> response = absenceController.getTeacherViewAbsencesByStudentId(studentId);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNull(response.getBody());
        verify(absenceService).getTeacherViewAbsencesByStudentId(studentId);
    }
}