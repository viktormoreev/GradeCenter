package com.GradeCenter.demo.Tests;

import com.GradeCenter.controllers.SchoolController;
import com.GradeCenter.dtos.SchoolCreateRequest;
import com.GradeCenter.dtos.SchoolDto;
import com.GradeCenter.dtos.SchoolNamesDto;
import com.GradeCenter.dtos.TeacherDto;
import com.GradeCenter.service.SchoolService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class SchoolControllerTest {

    @Mock
    private SchoolService schoolService;

    @InjectMocks
    private SchoolController schoolController;

    private SchoolDto schoolDto;
    private SchoolCreateRequest schoolCreateRequest;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        schoolDto = new SchoolDto();
        schoolDto.setId(1L);
        schoolDto.setName("Test School");
        schoolDto.setAddress("123 Test St");

        schoolCreateRequest = new SchoolCreateRequest("New School", "456 New St");
    }

    @Test
    void getAllSchools() {
        List<SchoolDto> schools = Arrays.asList(schoolDto);
        when(schoolService.getAllSchools()).thenReturn(schools);

        List<SchoolDto> response = schoolController.getAllSchools();

        assertEquals(schools, response);
        verify(schoolService).getAllSchools();
    }

    @Test
    void getAllSchoolsNames() {
        List<SchoolNamesDto> schoolNames = Arrays.asList(new SchoolNamesDto());
        when(schoolService.getAllSchoolsNames()).thenReturn(schoolNames);

        List<SchoolNamesDto> response = schoolController.getAllSchoolsNames();

        assertEquals(schoolNames, response);
        verify(schoolService).getAllSchoolsNames();
    }

    @Test
    void getSchoolById() {
        when(schoolService.getSchoolById(1L)).thenReturn(schoolDto);

        ResponseEntity<SchoolDto> response = schoolController.getSchoolById(1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(schoolDto, response.getBody());
        verify(schoolService).getSchoolById(1L);
    }

    @Test
    void addSchool() {
        when(schoolService.addSchool(any(SchoolCreateRequest.class))).thenReturn(schoolDto);

        ResponseEntity<SchoolDto> response = schoolController.addSchool(schoolCreateRequest);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(schoolDto, response.getBody());
        verify(schoolService).addSchool(schoolCreateRequest);
    }

    @Test
    void deleteSchool() {
        when(schoolService.deleteSchool(1L)).thenReturn(true);

        ResponseEntity<String> response = schoolController.deleteSchool(1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("School deleted successfully", response.getBody());
        verify(schoolService).deleteSchool(1L);
    }

    @Test
    void updateSchool() {
        when(schoolService.updateSchool(eq(1L), any(SchoolDto.class))).thenReturn(schoolDto);

        ResponseEntity<SchoolDto> response = schoolController.updateSchool(1L, schoolDto);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(schoolDto, response.getBody());
        verify(schoolService).updateSchool(eq(1L), any(SchoolDto.class));
    }

    @Test
    void getTeachersBySchoolId() {
        List<TeacherDto> teachers = Arrays.asList(new TeacherDto());
        when(schoolService.getTeachersBySchoolId(1L)).thenReturn(teachers);

        ResponseEntity<List<TeacherDto>> response = schoolController.getTeachersBySchoolId(1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(teachers, response.getBody());
        verify(schoolService).getTeachersBySchoolId(1L);
    }
}