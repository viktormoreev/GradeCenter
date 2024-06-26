package com.GradeCenter.demo.Tests;

import com.GradeCenter.dtos.AbsenceDto;
import com.GradeCenter.dtos.AbsenceStudentViewDto;
import com.GradeCenter.dtos.AbsenceTeacherViewDto;
import com.GradeCenter.entity.Absence;
import com.GradeCenter.entity.Course;
import com.GradeCenter.entity.Student;
import com.GradeCenter.exceptions.AbsenceNotFoundException;
import com.GradeCenter.exceptions.CourseNotFoundException;
import com.GradeCenter.exceptions.StudentNotFoundException;
import com.GradeCenter.mapper.EntityMapper;
import com.GradeCenter.repository.AbsenceRepository;
import com.GradeCenter.repository.CourseRepository;
import com.GradeCenter.repository.StudentRepository;
import com.GradeCenter.service.implementation.AbsenceServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class AbsenceServiceImplTest {

    @Mock
    private EntityMapper entityMapper;

    @Mock
    private AbsenceRepository absenceRepository;

    @Mock
    private StudentRepository studentRepository;

    @Mock
    private CourseRepository courseRepository;

    @InjectMocks
    private AbsenceServiceImpl absenceService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getAllAbsences_shouldReturnAllAbsences() {
        // Arrange
        List<Absence> absences = Arrays.asList(new Absence(), new Absence());
        List<AbsenceDto> absenceDtos = Arrays.asList(new AbsenceDto(), new AbsenceDto());
        when(absenceRepository.findAll()).thenReturn(absences);
        when(entityMapper.mapToAbsenceListDto(absences)).thenReturn(absenceDtos);

        // Act
        List<AbsenceDto> result = absenceService.getAllAbsences();

        // Assert
        assertEquals(absenceDtos, result);
        verify(absenceRepository).findAll();
        verify(entityMapper).mapToAbsenceListDto(absences);
    }

    @Test
    void createAbsence_shouldCreateAndReturnAbsence() throws StudentNotFoundException, CourseNotFoundException {
        // Arrange
        AbsenceDto absenceDto = new AbsenceDto(1L, null, 1L, LocalDate.now());
        Student student = new Student();
        Course course = new Course();
        Absence absence = new Absence();
        Absence savedAbsence = new Absence();
        AbsenceDto savedAbsenceDto = new AbsenceDto();

        when(studentRepository.findById(1L)).thenReturn(Optional.of(student));
        when(courseRepository.findById(1L)).thenReturn(Optional.of(course));
        when(entityMapper.mapToAbsenceEntity(absenceDto, student, course)).thenReturn(absence);
        when(absenceRepository.save(absence)).thenReturn(savedAbsence);
        when(entityMapper.mapToAbsenceDto(savedAbsence)).thenReturn(savedAbsenceDto);

        // Act
        AbsenceDto result = absenceService.createAbsence(absenceDto);

        // Assert
        assertEquals(savedAbsenceDto, result);
        verify(studentRepository).findById(1L);
        verify(courseRepository).findById(1L);
        verify(entityMapper).mapToAbsenceEntity(absenceDto, student, course);
        verify(absenceRepository).save(absence);
        verify(entityMapper).mapToAbsenceDto(savedAbsence);
    }

    @Test
    void getPersonalStudentAbsences_shouldReturnAbsencesForStudent() throws StudentNotFoundException {
        // Arrange
        String userId = "user123";
        Student student = new Student();
        student.setId(1L);
        List<Absence> absences = Arrays.asList(new Absence(), new Absence());
        List<AbsenceStudentViewDto> absenceStudentViewDtos = Arrays.asList(new AbsenceStudentViewDto(), new AbsenceStudentViewDto());

        when(studentRepository.findByUserID(userId)).thenReturn(Optional.of(student));
        when(absenceRepository.findByStudentId(1L)).thenReturn(Optional.of(absences));
        when(entityMapper.mapToAbsenceStudentViewDto(any(Absence.class))).thenReturn(new AbsenceStudentViewDto());

        // Act
        List<AbsenceStudentViewDto> result = absenceService.getPersonalStudentAbsences(userId);

        // Assert
        assertEquals(2, result.size());
        verify(studentRepository).findByUserID(userId);
        verify(absenceRepository).findByStudentId(1L);
        verify(entityMapper, times(2)).mapToAbsenceStudentViewDto(any(Absence.class));
    }

    @Test
    void getAllAbsencesByStudentIdForAdmin_shouldReturnAbsences() throws StudentNotFoundException {
        // Arrange
        long studentId = 1L;
        List<Absence> absences = Arrays.asList(new Absence(), new Absence());
        List<AbsenceDto> absenceDtos = Arrays.asList(new AbsenceDto(), new AbsenceDto());

        when(studentRepository.existsById(studentId)).thenReturn(true);
        when(absenceRepository.findByStudentId(studentId)).thenReturn(Optional.of(absences));
        when(entityMapper.mapToAbsenceDto(any(Absence.class))).thenReturn(new AbsenceDto());

        // Act
        List<AbsenceDto> result = absenceService.getAllAbsencesByStudentIdForAdmin(studentId);

        // Assert
        assertEquals(2, result.size());
        verify(studentRepository).existsById(studentId);
        verify(absenceRepository).findByStudentId(studentId);
        verify(entityMapper, times(2)).mapToAbsenceDto(any(Absence.class));
    }

    @Test
    void getAllAbsencesByStudentIdForAdmin_shouldThrowExceptionWhenStudentNotFound() {
        // Arrange
        long studentId = 1L;
        when(studentRepository.existsById(studentId)).thenReturn(false);

        // Act & Assert
        assertThrows(StudentNotFoundException.class, () -> absenceService.getAllAbsencesByStudentIdForAdmin(studentId));
        verify(studentRepository).existsById(studentId);
    }

    @Test
    void deleteAbsenceById_shouldReturnTrueWhenAbsenceExists() {
        // Arrange
        Long absenceId = 1L;
        when(absenceRepository.existsById(absenceId)).thenReturn(true);

        // Act
        boolean result = absenceService.deleteAbsenceById(absenceId);

        // Assert
        assertTrue(result);
        verify(absenceRepository).existsById(absenceId);
        verify(absenceRepository).deleteById(absenceId);
    }

    @Test
    void deleteAbsenceById_shouldReturnFalseWhenAbsenceDoesNotExist() {
        // Arrange
        Long absenceId = 1L;
        when(absenceRepository.existsById(absenceId)).thenReturn(false);

        // Act
        boolean result = absenceService.deleteAbsenceById(absenceId);

        // Assert
        assertFalse(result);
        verify(absenceRepository).existsById(absenceId);
        verify(absenceRepository, never()).deleteById(absenceId);
    }

    @Test
    void updateAbsence_shouldUpdateAndReturnAbsence() throws StudentNotFoundException, CourseNotFoundException, AbsenceNotFoundException {
        // Arrange
        Long absenceId = 1L;
        AbsenceDto absenceDto = new AbsenceDto(1L, absenceId, 1L, LocalDate.now());
        Absence existingAbsence = new Absence();
        Student student = new Student();
        Course course = new Course();
        Absence updatedAbsence = new Absence();
        AbsenceDto updatedAbsenceDto = new AbsenceDto();

        when(absenceRepository.findById(absenceId)).thenReturn(Optional.of(existingAbsence));
        when(studentRepository.findById(1L)).thenReturn(Optional.of(student));
        when(courseRepository.findById(1L)).thenReturn(Optional.of(course));
        when(absenceRepository.save(existingAbsence)).thenReturn(updatedAbsence);
        when(entityMapper.mapToAbsenceDto(updatedAbsence)).thenReturn(updatedAbsenceDto);

        // Act
        AbsenceDto result = absenceService.updateAbsence(absenceId, absenceDto);

        // Assert
        assertEquals(updatedAbsenceDto, result);
        verify(absenceRepository).findById(absenceId);
        verify(studentRepository).findById(1L);
        verify(courseRepository).findById(1L);
        verify(absenceRepository).save(existingAbsence);
        verify(entityMapper).mapToAbsenceDto(updatedAbsence);
    }

    @Test
    void getTeacherViewAbsencesByStudentId_shouldReturnAbsences() throws AbsenceNotFoundException {
        // Arrange
        long studentId = 1L;
        List<Absence> absences = Arrays.asList(new Absence(), new Absence());
        List<AbsenceTeacherViewDto> absenceTeacherViewDtos = Arrays.asList(new AbsenceTeacherViewDto(), new AbsenceTeacherViewDto());

        when(absenceRepository.findByStudentId(studentId)).thenReturn(Optional.of(absences));
        when(entityMapper.mapToAbsenceTeacherViewDtoList(absences)).thenReturn(absenceTeacherViewDtos);

        // Act
        List<AbsenceTeacherViewDto> result = absenceService.getTeacherViewAbsencesByStudentId(studentId);

        // Assert
        assertEquals(absenceTeacherViewDtos, result);
        verify(absenceRepository).findByStudentId(studentId);
        verify(entityMapper).mapToAbsenceTeacherViewDtoList(absences);
    }

    @Test
    void getTeacherViewAbsencesByStudentId_shouldThrowExceptionWhenAbsencesNotFound() {
        // Arrange
        long studentId = 1L;
        when(absenceRepository.findByStudentId(studentId)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(AbsenceNotFoundException.class, () -> absenceService.getTeacherViewAbsencesByStudentId(studentId));
        verify(absenceRepository).findByStudentId(studentId);
    }
}