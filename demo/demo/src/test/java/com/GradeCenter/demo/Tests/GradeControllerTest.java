package com.GradeCenter.demo.Tests;

import com.GradeCenter.controllers.GradeController;
import com.GradeCenter.dtos.GradeDto;
import com.GradeCenter.dtos.GradeStudentViewDto;
import com.GradeCenter.exceptions.CourseNotFoundException;
import com.GradeCenter.exceptions.GradeNotFoundException;
import com.GradeCenter.exceptions.StudentNotFoundException;
import com.GradeCenter.service.GradeService;
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
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

class GradeControllerTest {

    @Mock
    private GradeService gradeService;

    @InjectMocks
    private GradeController gradeController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getAllGrades() {
        List<GradeDto> grades = Arrays.asList(new GradeDto(), new GradeDto());
        when(gradeService.getAllGrades()).thenReturn(grades);

        List<GradeDto> result = gradeController.getAllGrades();

        assertEquals(2, result.size());
        verify(gradeService).getAllGrades();
    }

    @Test
    void getGradesByStudentId() throws StudentNotFoundException {
        long studentId = 1L;
        List<GradeDto> grades = Arrays.asList(new GradeDto(), new GradeDto());
        when(gradeService.getGradesByStudentId(studentId)).thenReturn(grades);

        ResponseEntity<List<GradeDto>> response = gradeController.getGradesByStudentId(studentId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(2, response.getBody().size());
        verify(gradeService).getGradesByStudentId(studentId);
    }

    @Test
    void getGradesByStudentId_NoContent() throws StudentNotFoundException {
        long studentId = 1L;
        when(gradeService.getGradesByStudentId(studentId)).thenReturn(Arrays.asList());

        ResponseEntity<List<GradeDto>> response = gradeController.getGradesByStudentId(studentId);

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        verify(gradeService).getGradesByStudentId(studentId);
    }

    @Test
    void createGrade() throws StudentNotFoundException, CourseNotFoundException {
        GradeDto gradeDto = new GradeDto(null, 5.0, 1L, 1L);
        when(gradeService.createGrade(gradeDto)).thenReturn(gradeDto);

        ResponseEntity<GradeDto> response = gradeController.createGrade(gradeDto);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        verify(gradeService).createGrade(gradeDto);
    }

    @Test
    void createGrade_StudentOrCourseNotFound() throws StudentNotFoundException, CourseNotFoundException {
        GradeDto gradeDto = new GradeDto(null, 5.0, 1L, 1L);
        when(gradeService.createGrade(gradeDto)).thenThrow(new StudentNotFoundException("Student not found"));

        ResponseEntity<GradeDto> response = gradeController.createGrade(gradeDto);

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        verify(gradeService).createGrade(gradeDto);
    }

    @Test
    void updateGrade() throws GradeNotFoundException, StudentNotFoundException, CourseNotFoundException {
        Long gradeId = 1L;
        GradeDto gradeDto = new GradeDto(gradeId, 5.0, 1L, 1L);
        when(gradeService.updateGrade(gradeId, gradeDto)).thenReturn(gradeDto);

        ResponseEntity<GradeDto> response = gradeController.updateGrade(gradeId, gradeDto);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        verify(gradeService).updateGrade(gradeId, gradeDto);
    }

    @Test
    void updateGrade_NotFound() throws GradeNotFoundException, StudentNotFoundException, CourseNotFoundException {
        Long gradeId = 1L;
        GradeDto gradeDto = new GradeDto(gradeId, 5.0, 1L, 1L);
        when(gradeService.updateGrade(gradeId, gradeDto)).thenThrow(new GradeNotFoundException("Grade not found"));

        ResponseEntity<GradeDto> response = gradeController.updateGrade(gradeId, gradeDto);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        verify(gradeService).updateGrade(gradeId, gradeDto);
    }

    @Test
    void deleteGradeById() {
        Long gradeId = 1L;
        when(gradeService.deleteGradeById(gradeId)).thenReturn(true);

        ResponseEntity<String> response = gradeController.deleteGradeById(gradeId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Grade deleted successfully", response.getBody());
        verify(gradeService).deleteGradeById(gradeId);
    }

    @Test
    void deleteGradeById_NotFound() {
        Long gradeId = 1L;
        when(gradeService.deleteGradeById(gradeId)).thenReturn(false);

        ResponseEntity<String> response = gradeController.deleteGradeById(gradeId);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("Failed to delete grade", response.getBody());
        verify(gradeService).deleteGradeById(gradeId);
    }

    @Test
    void getPersonalStudentGrades() throws StudentNotFoundException {
        String userId = "user123";
        List<GradeStudentViewDto> grades = Arrays.asList(new GradeStudentViewDto(), new GradeStudentViewDto());

        // Mock SecurityContext and Authentication
        SecurityContext securityContext = mock(SecurityContext.class);
        Authentication authentication = mock(Authentication.class);
        Jwt jwt = mock(Jwt.class);

        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.getPrincipal()).thenReturn(jwt);
        when(jwt.getClaimAsString("sub")).thenReturn(userId);
        SecurityContextHolder.setContext(securityContext);

        when(gradeService.getPersonalStudentGrades(userId)).thenReturn(grades);

        ResponseEntity<List<GradeStudentViewDto>> response = gradeController.getPersonalStudentGrades();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(2, response.getBody().size());
        verify(gradeService).getPersonalStudentGrades(userId);
    }

    @Test
    void getPersonalStudentGrades_NoContent() throws StudentNotFoundException {
        String userId = "user123";

        // Mock SecurityContext and Authentication
        SecurityContext securityContext = mock(SecurityContext.class);
        Authentication authentication = mock(Authentication.class);
        Jwt jwt = mock(Jwt.class);

        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.getPrincipal()).thenReturn(jwt);
        when(jwt.getClaimAsString("sub")).thenReturn(userId);
        SecurityContextHolder.setContext(securityContext);

        when(gradeService.getPersonalStudentGrades(userId)).thenReturn(Collections.emptyList());

        ResponseEntity<List<GradeStudentViewDto>> response = gradeController.getPersonalStudentGrades();

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        assertNull(response.getBody());
        verify(gradeService).getPersonalStudentGrades(userId);
    }

    @Test
    void getPersonalStudentGrades_StudentNotFoundException() throws StudentNotFoundException {
        String userId = "user123";

        // Mock SecurityContext and Authentication
        SecurityContext securityContext = mock(SecurityContext.class);
        Authentication authentication = mock(Authentication.class);
        Jwt jwt = mock(Jwt.class);

        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.getPrincipal()).thenReturn(jwt);
        when(jwt.getClaimAsString("sub")).thenReturn(userId);
        SecurityContextHolder.setContext(securityContext);

        when(gradeService.getPersonalStudentGrades(userId)).thenThrow(new StudentNotFoundException("Student not found"));

        ResponseEntity<List<GradeStudentViewDto>> response = gradeController.getPersonalStudentGrades();

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNull(response.getBody());
        verify(gradeService).getPersonalStudentGrades(userId);
    }

    @Test
    void createGrade_ValidationError() throws CourseNotFoundException, StudentNotFoundException {
        GradeDto invalidGradeDto = new GradeDto(null, 7.0, 1L, 1L);

        ResponseEntity<GradeDto> response = gradeController.createGrade(invalidGradeDto);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertNull(response.getBody());
        verify(gradeService, never()).createGrade(any(GradeDto.class));
    }

    @Test
    void updateGrade_ValidationError() throws GradeNotFoundException, CourseNotFoundException, StudentNotFoundException {
        Long gradeId = 1L;
        GradeDto invalidGradeDto = new GradeDto(gradeId, 1.5, 1L, 1L);

        ResponseEntity<GradeDto> response = gradeController.updateGrade(gradeId, invalidGradeDto);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertNull(response.getBody());
        verify(gradeService, never()).updateGrade(anyLong(), any(GradeDto.class));
    }
}
