package com.GradeCenter.demo.Tests;

import com.GradeCenter.dtos.*;
import com.GradeCenter.entity.*;
import com.GradeCenter.mapper.EntityMapper;
import com.GradeCenter.repository.ParentRepository;
import com.GradeCenter.repository.SchoolRepository;
import com.GradeCenter.repository.StudentRepository;
import com.GradeCenter.repository.StudyGroupRepository;
import com.GradeCenter.service.CourseService;
import com.GradeCenter.service.implementation.KeycloakAdminClientService;
import com.GradeCenter.service.implementation.StudentServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
public class StudentServiceImplTest {

    @InjectMocks
    private StudentServiceImpl studentService;

    @Mock
    private StudentRepository studentRepository;

    @Mock
    private ParentRepository parentRepository;

    @Mock
    private StudyGroupRepository studyGroupRepository;

    @Mock
    private EntityMapper entityMapper;

    @Mock
    private KeycloakAdminClientService keycloakAdminClientService;

    @Mock
    private CourseService courseService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetAllStudents() {
        // Arrange
        List<Student> students = Arrays.asList(new Student(), new Student());
        List<StudentDto> expectedDtos = Arrays.asList(new StudentDto(), new StudentDto());

        when(studentRepository.findAll()).thenReturn(students);
        when(entityMapper.mapToStudentListDto(students)).thenReturn(expectedDtos);

        // Act
        List<StudentDto> result = studentService.getAllStudents();

        // Assert
        assertEquals(expectedDtos, result);
        verify(studentRepository).findAll();
        verify(entityMapper).mapToStudentListDto(students);
    }

    @Test
    void testGetStudentByUId() {
        // Arrange
        String uid = "test-uid";
        Student student = new Student();
        StudentDto expectedDto = new StudentDto();

        when(studentRepository.findByUserID(uid)).thenReturn(Optional.of(student));
        when(entityMapper.mapToStudentDto(student)).thenReturn(expectedDto);

        // Act
        StudentDto result = studentService.getStudentByUId(uid);

        // Assert
        assertEquals(expectedDto, result);
        verify(studentRepository).findByUserID(uid);
        verify(entityMapper).mapToStudentDto(student);
    }

    @Test
    void testDeleteStudentUID() {
        // Arrange
        String uid = "test-uid";
        Student student = new Student();

        when(studentRepository.findByUserID(uid)).thenReturn(Optional.of(student));

        // Act
        boolean result = studentService.deleteStudentUID(uid);

        // Assert
        assertTrue(result);
        verify(studentRepository).findByUserID(uid);
        verify(studentRepository).delete(student);
    }

    @Test
    void testAddStudentToStudyGroup() {
        // Arrange
        Long studyGroupId = 1L;
        Long studentId = 2L;
        StudyGroup studyGroup = new StudyGroup();
        Student student = new Student();
        StudentDto expectedDto = new StudentDto();

        when(studyGroupRepository.findById(studyGroupId)).thenReturn(Optional.of(studyGroup));
        when(studentRepository.findById(studentId)).thenReturn(Optional.of(student));
        when(studentRepository.save(student)).thenReturn(student);
        when(entityMapper.mapToStudentDto(student)).thenReturn(expectedDto);

        // Act
        StudentDto result = studentService.addStudentToStudyGroup(studyGroupId, studentId);

        // Assert
        assertEquals(expectedDto, result);
        assertEquals(studyGroup, student.getClasses());
        verify(studyGroupRepository).findById(studyGroupId);
        verify(studentRepository).findById(studentId);
        verify(studentRepository).save(student);
        verify(entityMapper).mapToStudentDto(student);
    }
}