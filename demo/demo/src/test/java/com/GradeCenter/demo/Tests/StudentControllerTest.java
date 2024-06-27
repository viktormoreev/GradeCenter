package com.GradeCenter.demo.Tests;

import com.GradeCenter.controllers.StudentController;
import com.GradeCenter.dtos.*;
import com.GradeCenter.service.StudentService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
public class StudentControllerTest {

    @InjectMocks
    private StudentController studentController;

    @Mock
    private StudentService studentService;

    @Mock
    private Authentication authentication;

    @Mock
    private SecurityContext securityContext;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetAllStudents() {
        List<StudentDto> expectedStudents = Arrays.asList(new StudentDto(), new StudentDto());
        when(studentService.getAllStudents()).thenReturn(expectedStudents);

        List<StudentDto> result = studentController.getAllStudents();

        assertEquals(expectedStudents, result);
        verify(studentService).getAllStudents();
    }

    @Test
    void testGetFullStudentById() {
        Long id = 1L;
        StudentFullReturnDto expectedStudent = new StudentFullReturnDto();
        when(studentService.getFullStudentById(id)).thenReturn(expectedStudent);

        ResponseEntity<StudentFullReturnDto> result = studentController.getFullStudentById(id);

        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertEquals(expectedStudent, result.getBody());
        verify(studentService).getFullStudentById(id);
    }

    @Test
    void testGetPersonalStudent() {
        String userId = "test-user-id";
        StudentDto expectedStudent = new StudentDto();
        Jwt jwt = mock(Jwt.class);

        when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);
        when(authentication.getPrincipal()).thenReturn(jwt);
        when(jwt.getClaimAsString("sub")).thenReturn(userId);
        when(studentService.getStudentByUId(userId)).thenReturn(expectedStudent);

        ResponseEntity<StudentDto> result = studentController.getPersonalStudent();

        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertEquals(expectedStudent, result.getBody());
        verify(studentService).getStudentByUId(userId);
    }

    @Test
    void testAddStudent() {
        UserIDRequest request = new UserIDRequest("testid");
        StudentDto expectedStudent = new StudentDto();
        when(studentService.addStudent(request)).thenReturn(expectedStudent);

        ResponseEntity<StudentDto> result = studentController.addStudent(request);

        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertEquals(expectedStudent, result.getBody());
        verify(studentService).addStudent(request);
    }

    @Test
    void testDeleteStudentId() {
        Long id = 1L;
        when(studentService.deleteStudentID(id)).thenReturn(true);

        ResponseEntity<String> result = studentController.deleteStudentId(id);

        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertEquals("Student deleted successfully", result.getBody());
        verify(studentService).deleteStudentID(id);
    }

}