package com.GradeCenter.demo.Tests;

import com.GradeCenter.dtos.SchoolCreateRequest;
import com.GradeCenter.dtos.SchoolDto;
import com.GradeCenter.dtos.SchoolNamesDto;
import com.GradeCenter.dtos.TeacherDto;
import com.GradeCenter.entity.School;
import com.GradeCenter.entity.Teacher;
import com.GradeCenter.mapper.EntityMapper;
import com.GradeCenter.repository.DirectorRepository;
import com.GradeCenter.repository.SchoolRepository;
import com.GradeCenter.repository.StudyGroupRepository;
import com.GradeCenter.repository.TeacherRepository;
import com.GradeCenter.service.implementation.SchoolServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class SchoolServiceImplTest {

    @Mock
    private SchoolRepository schoolRepository;

    @Mock
    private DirectorRepository directorRepository;

    @Mock
    private TeacherRepository teacherRepository;

    @Mock
    private StudyGroupRepository studyGroupRepository;

    @Mock
    private EntityMapper entityMapper;

    @InjectMocks
    private SchoolServiceImpl schoolService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getAllSchools() {
        List<School> schools = Arrays.asList(new School(), new School());
        when(schoolRepository.findAll()).thenReturn(schools);
        when(entityMapper.mapToSchoolListDto(schools)).thenReturn(Arrays.asList(new SchoolDto(), new SchoolDto()));

        List<SchoolDto> result = schoolService.getAllSchools();

        assertNotNull(result);
        assertEquals(2, result.size());
        verify(schoolRepository).findAll();
        verify(entityMapper).mapToSchoolListDto(schools);
    }

    @Test
    void getAllSchoolsNames() {
        List<School> schools = Arrays.asList(new School(), new School());
        when(schoolRepository.findAll()).thenReturn(schools);
        when(entityMapper.mapToSchoolNamesDtoList(schools)).thenReturn(Arrays.asList(new SchoolNamesDto(), new SchoolNamesDto()));

        List<SchoolNamesDto> result = schoolService.getAllSchoolsNames();

        assertNotNull(result);
        assertEquals(2, result.size());
        verify(schoolRepository).findAll();
        verify(entityMapper).mapToSchoolNamesDtoList(schools);
    }

    @Test
    void addSchool() {
        SchoolCreateRequest request = new SchoolCreateRequest("Test School", "123 Test St");
        School school = new School();
        school.setName(request.getName());
        school.setAddress(request.getAddress());

        when(schoolRepository.save(any(School.class))).thenReturn(school);
        when(entityMapper.mapToSchoolDto(school)).thenReturn(new SchoolDto());

        SchoolDto result = schoolService.addSchool(request);

        assertNotNull(result);
        verify(schoolRepository).save(any(School.class));
        verify(entityMapper).mapToSchoolDto(school);
    }

    @Test
    void getSchoolById() {
        Long id = 1L;
        School school = new School();
        when(schoolRepository.findById(id)).thenReturn(Optional.of(school));
        when(entityMapper.mapToSchoolDto(school)).thenReturn(new SchoolDto());

        SchoolDto result = schoolService.getSchoolById(id);

        assertNotNull(result);
        verify(schoolRepository).findById(id);
        verify(entityMapper).mapToSchoolDto(school);
    }

    @Test
    void deleteSchool() {
        Long id = 1L;
        School school = new School();
        school.setTeachers(Arrays.asList(new Teacher(), new Teacher()));
        school.setStudyGroups(Arrays.asList());

        when(schoolRepository.findById(id)).thenReturn(Optional.of(school));

        boolean result = schoolService.deleteSchool(id);

        assertTrue(result);
        verify(schoolRepository).findById(id);
        verify(teacherRepository, times(2)).save(any(Teacher.class));
        verify(schoolRepository).deleteById(id);
    }

    @Test
    void updateSchool() {
        Long id = 1L;
        SchoolDto updateDto = new SchoolDto();
        updateDto.setName("Updated School");
        updateDto.setAddress("456 Update St");

        School existingSchool = new School();
        when(schoolRepository.findById(id)).thenReturn(Optional.of(existingSchool));
        when(schoolRepository.save(any(School.class))).thenReturn(existingSchool);
        when(entityMapper.mapToSchoolDto(existingSchool)).thenReturn(updateDto);

        SchoolDto result = schoolService.updateSchool(id, updateDto);

        assertNotNull(result);
        assertEquals("Updated School", result.getName());
        assertEquals("456 Update St", result.getAddress());
        verify(schoolRepository).findById(id);
        verify(schoolRepository).save(existingSchool);
        verify(entityMapper).mapToSchoolDto(existingSchool);
    }

    @Test
    void getTeachersBySchoolId() {
        Long schoolId = 1L;
        List<Teacher> teachers = Arrays.asList(new Teacher(), new Teacher());
        when(teacherRepository.findBySchoolId(schoolId)).thenReturn(teachers);
        when(entityMapper.mapToTeacherListDto(teachers)).thenReturn(Arrays.asList(new TeacherDto(), new TeacherDto()));

        List<TeacherDto> result = schoolService.getTeachersBySchoolId(schoolId);

        assertNotNull(result);
        assertEquals(2, result.size());
        verify(teacherRepository).findBySchoolId(schoolId);
        verify(entityMapper).mapToTeacherListDto(teachers);
    }
}