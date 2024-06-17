package com.GradeCenter.demo;

import com.GradeCenter.dtos.SchoolCreateRequest;
import com.GradeCenter.dtos.SchoolDto;
import com.GradeCenter.dtos.SchoolNamesDto;
import com.GradeCenter.dtos.TeacherDto;
import com.GradeCenter.entity.Director;
import com.GradeCenter.entity.School;
import com.GradeCenter.entity.Teacher;
import com.GradeCenter.mapper.EntityMapper;
import com.GradeCenter.repository.DirectorRepository;
import com.GradeCenter.repository.SchoolRepository;
import com.GradeCenter.repository.TeacherRepository;
import com.GradeCenter.service.implementation.SchoolServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SchoolServiceImplTest {

    @Mock
    private SchoolRepository schoolRepository;

    @Mock
    private DirectorRepository directorRepository;

    @Mock
    private TeacherRepository teacherRepository;

    @Mock
    private EntityMapper entityMapper;

    @InjectMocks
    private SchoolServiceImpl schoolService;

    private School school1;
    private School school2;
    private SchoolDto schoolDto1;
    private SchoolDto schoolDto2;
    private SchoolNamesDto schoolNamesDto1;
    private SchoolNamesDto schoolNamesDto2;
    private Teacher teacher1;
    private Teacher teacher2;
    private TeacherDto teacherDto1;
    private TeacherDto teacherDto2;
    private Director director;

    @BeforeEach
    void setUp() {
        school1 = School.builder().name("School 1").address("Address 1").build();
        school2 = School.builder().name("School 2").address("Address 2").build();

        schoolDto1 = new SchoolDto();
        schoolDto1.setId(1L);
        schoolDto1.setName("School 1");
        schoolDto1.setAddress("Address 1");

        schoolDto2 = new SchoolDto();
        schoolDto2.setId(2L);
        schoolDto2.setName("School 2");
        schoolDto2.setAddress("Address 2");

        teacher1 = Teacher.builder().userID("1").school(school1).build();;
        teacher2 = Teacher.builder().userID("2").school(school1).build();;

        teacherDto1 = new TeacherDto();
        teacherDto1.setUserID("teacher1");

        teacherDto2 = new TeacherDto();
        teacherDto2.setUserID("teacher2");

        schoolNamesDto1 = new SchoolNamesDto();
        schoolNamesDto1.setId(1L);
        schoolNamesDto1.setName("School 1");
        schoolNamesDto1.setAddress("Address 1");
        schoolNamesDto1.setDirectorName("director1");
        schoolNamesDto1.setTeachersNames(Arrays.asList("teacher1", "teacher2"));

        schoolNamesDto2 = new SchoolNamesDto();
        schoolNamesDto2.setId(2L);
        schoolNamesDto2.setName("School 2");
        schoolNamesDto2.setAddress("Address 2");
        schoolNamesDto2.setDirectorName("director2");
        schoolNamesDto2.setTeachersNames(Arrays.asList("teacher3", "teacher4"));



        director = Director.builder().userID("1").build();
    }

    @Test
    void testGetAllSchools() {
        when(schoolRepository.findAll()).thenReturn(Arrays.asList(school1, school2));
        when(entityMapper.mapToSchoolListDto(Arrays.asList(school1, school2))).thenReturn(Arrays.asList(schoolDto1, schoolDto2));

        List<SchoolDto> result = schoolService.getAllSchools();

        assertEquals(2, result.size());
        verify(schoolRepository, times(1)).findAll();
        verify(entityMapper, times(1)).mapToSchoolListDto(Arrays.asList(school1, school2));
    }

    @Test
    void testGetAllSchoolsNames() {
        when(schoolRepository.findAll()).thenReturn(Arrays.asList(school1, school2));
        when(entityMapper.mapToSchoolNamesDtoList(Arrays.asList(school1, school2))).thenReturn(Arrays.asList(schoolNamesDto1, schoolNamesDto2));

        List<SchoolNamesDto> result = schoolService.getAllSchoolsNames();

        assertEquals(2, result.size());
        verify(schoolRepository, times(1)).findAll();
        verify(entityMapper, times(1)).mapToSchoolNamesDtoList(Arrays.asList(school1, school2));
    }

    @Test
    void testAddSchool() {
        SchoolCreateRequest request = new SchoolCreateRequest("New School", "New Address");
        School newSchool = School.builder().name("New School").address("New Address").build();
        SchoolDto newSchoolDto = new SchoolDto();
        newSchoolDto.setName("New School");
        newSchoolDto.setAddress("New Address");

        when(schoolRepository.save(any(School.class))).thenReturn(newSchool);
        when(entityMapper.mapToSchoolDto(newSchool)).thenReturn(newSchoolDto);

        SchoolDto result = schoolService.addSchool(request);

        assertNotNull(result);
        assertEquals(request.getName(), result.getName());
        assertEquals(request.getAddress(), result.getAddress());
        verify(schoolRepository, times(1)).save(any(School.class));
        verify(entityMapper, times(1)).mapToSchoolDto(newSchool);
    }

    @Test
    void testGetSchoolById() {
        when(schoolRepository.findById(1L)).thenReturn(Optional.of(school1));
        when(entityMapper.mapToSchoolDto(school1)).thenReturn(schoolDto1);

        SchoolDto result = schoolService.getSchoolById(1L);

        assertNotNull(result);
        assertEquals(school1.getName(), result.getName());
        verify(schoolRepository, times(1)).findById(1L);
        verify(entityMapper, times(1)).mapToSchoolDto(school1);
    }

    @Test
    void testDeleteSchool() {
        when(schoolRepository.existsById(1L)).thenReturn(true);

        boolean result = schoolService.deleteSchool(1L);

        assertTrue(result);
        verify(schoolRepository, times(1)).existsById(1L);
        verify(schoolRepository, times(1)).deleteById(1L);
    }

    @Test
    void testDeleteSchool_NotFound() {
        when(schoolRepository.existsById(1L)).thenReturn(false);

        boolean result = schoolService.deleteSchool(1L);

        assertFalse(result);
        verify(schoolRepository, times(1)).existsById(1L);
        verify(schoolRepository, never()).deleteById(1L);
    }

    @Test
    void testUpdateSchool() {
        SchoolDto updateDto = new SchoolDto();
        updateDto.setName("Updated School");
        updateDto.setAddress("Updated Address");
        when(schoolRepository.findById(1L)).thenReturn(Optional.of(school1));
        when(schoolRepository.save(any(School.class))).thenReturn(school1);
        when(entityMapper.mapToSchoolDto(school1)).thenReturn(updateDto);

        SchoolDto result = schoolService.updateSchool(1L, updateDto);

        assertNotNull(result);
        assertEquals("Updated School", result.getName());
        verify(schoolRepository, times(1)).findById(1L);
        verify(schoolRepository, times(1)).save(any(School.class));
        verify(entityMapper, times(1)).mapToSchoolDto(school1);
    }
}