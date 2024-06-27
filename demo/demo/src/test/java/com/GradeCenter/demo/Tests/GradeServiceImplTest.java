package com.GradeCenter.demo.Tests;

import com.GradeCenter.dtos.GradeDto;
import com.GradeCenter.dtos.GradeStudentViewDto;
import com.GradeCenter.entity.Course;
import com.GradeCenter.entity.Grade;
import com.GradeCenter.entity.Student;
import com.GradeCenter.exceptions.CourseNotFoundException;
import com.GradeCenter.exceptions.GradeNotFoundException;
import com.GradeCenter.exceptions.StudentNotFoundException;
import com.GradeCenter.mapper.EntityMapper;
import com.GradeCenter.repository.CourseRepository;
import com.GradeCenter.repository.GradeRepository;
import com.GradeCenter.repository.StudentRepository;
import com.GradeCenter.service.implementation.GradeServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

class GradeServiceImplTest {

    @Mock
    private GradeRepository gradeRepository;

    @Mock
    private StudentRepository studentRepository;

    @Mock
    private CourseRepository courseRepository;

    @Mock
    private EntityMapper entityMapper;

    @InjectMocks
    private GradeServiceImpl gradeService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getAllGrades() {
        Grade grade1 = new Grade();
        Grade grade2 = new Grade();
        List<Grade> grades = Arrays.asList(grade1, grade2);

        when(gradeRepository.findAll()).thenReturn(grades);
        when(entityMapper.mapToGradeDto(any(Grade.class))).thenReturn(new GradeDto());

        List<GradeDto> result = gradeService.getAllGrades();

        assertEquals(2, result.size());
        verify(gradeRepository).findAll();
        verify(entityMapper, times(2)).mapToGradeDto(any(Grade.class));
    }

    @Test
    void getGradesByStudentId() throws StudentNotFoundException {
        Long studentId = 1L;
        Grade grade1 = new Grade();
        Grade grade2 = new Grade();
        List<Grade> grades = Arrays.asList(grade1, grade2);

        when(gradeRepository.findByStudentId(studentId)).thenReturn(Optional.of(grades));
        when(entityMapper.mapToGradeDto(any(Grade.class))).thenReturn(new GradeDto());

        List<GradeDto> result = gradeService.getGradesByStudentId(studentId);

        assertEquals(2, result.size());
        verify(gradeRepository).findByStudentId(studentId);
        verify(entityMapper, times(2)).mapToGradeDto(any(Grade.class));
    }

    @Test
    void getGradesByStudentId_StudentNotFound() {
        Long studentId = 1L;
        when(gradeRepository.findByStudentId(studentId)).thenReturn(Optional.empty());

        assertThrows(StudentNotFoundException.class, () -> gradeService.getGradesByStudentId(studentId));
    }

    @Test
    void createGrade() throws StudentNotFoundException, CourseNotFoundException {
        GradeDto gradeDto = new GradeDto(null, 5.0, 1L, 1L);
        Student student = new Student();
        Course course = new Course();
        Grade grade = new Grade();

        when(studentRepository.findById(1L)).thenReturn(Optional.of(student));
        when(courseRepository.findById(1L)).thenReturn(Optional.of(course));
        when(entityMapper.mapToGradeEntity(gradeDto, student, course)).thenReturn(grade);
        when(gradeRepository.save(grade)).thenReturn(grade);
        when(entityMapper.mapToGradeDto(grade)).thenReturn(gradeDto);

        GradeDto result = gradeService.createGrade(gradeDto);

        assertNotNull(result);
        verify(gradeRepository).save(grade);
        verify(entityMapper).mapToGradeDto(grade);
    }

    @Test
    void updateGrade() throws GradeNotFoundException, StudentNotFoundException, CourseNotFoundException {
        Long gradeId = 1L;
        GradeDto gradeDto = new GradeDto(gradeId, 5.0, 1L, 1L);
        Grade existingGrade = new Grade();
        Student student = new Student();
        Course course = new Course();

        when(gradeRepository.findById(gradeId)).thenReturn(Optional.of(existingGrade));
        when(studentRepository.findById(1L)).thenReturn(Optional.of(student));
        when(courseRepository.findById(1L)).thenReturn(Optional.of(course));
        when(gradeRepository.save(existingGrade)).thenReturn(existingGrade);
        when(entityMapper.mapToGradeDto(existingGrade)).thenReturn(gradeDto);

        GradeDto result = gradeService.updateGrade(gradeId, gradeDto);

        assertNotNull(result);
        assertEquals(gradeDto.getGrade(), result.getGrade());
        verify(gradeRepository).save(existingGrade);
        verify(entityMapper).mapToGradeDto(existingGrade);
    }

    @Test
    void deleteGradeById() {
        Long gradeId = 1L;
        when(gradeRepository.existsById(gradeId)).thenReturn(true);

        boolean result = gradeService.deleteGradeById(gradeId);

        assertTrue(result);
        verify(gradeRepository).deleteById(gradeId);
    }

    @Test
    void deleteGradeById_GradeNotFound() {
        Long gradeId = 1L;
        when(gradeRepository.existsById(gradeId)).thenReturn(false);

        boolean result = gradeService.deleteGradeById(gradeId);

        assertFalse(result);
        verify(gradeRepository, never()).deleteById(gradeId);
    }

    @Test
    void getPersonalStudentGrades() throws StudentNotFoundException {
        String userId = "user123";
        Student student = new Student();
        student.setId(1L);
        Grade grade1 = new Grade();
        Grade grade2 = new Grade();
        List<Grade> grades = Arrays.asList(grade1, grade2);

        when(studentRepository.findByUserID(userId)).thenReturn(Optional.of(student));
        when(gradeRepository.findByStudentId(student.getId())).thenReturn(Optional.of(grades));
        when(grade1.getCourse()).thenReturn(new Course());
        when(grade2.getCourse()).thenReturn(new Course());

        List<GradeStudentViewDto> result = gradeService.getPersonalStudentGrades(userId);

        assertEquals(2, result.size());
        verify(studentRepository).findByUserID(userId);
        verify(gradeRepository).findByStudentId(student.getId());
    }
}