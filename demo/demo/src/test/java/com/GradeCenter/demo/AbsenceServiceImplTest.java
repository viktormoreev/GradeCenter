package com.GradeCenter.demo;

import com.GradeCenter.dtos.AbsenceDto;
import com.GradeCenter.dtos.AbsenceStudentViewDto;
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
import com.GradeCenter.service.implementation.KeycloakAdminClientService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@SpringBootTest
public class AbsenceServiceImplTest {

    @InjectMocks
    private AbsenceServiceImpl absenceService;

    @Mock
    private EntityMapper entityMapper;

    @Mock
    private AbsenceRepository absenceRepository;

    @Mock
    private StudentRepository studentRepository;

    @Mock
    private CourseRepository courseRepository;

    @Mock
    private KeycloakAdminClientService keycloakAdminClientService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testGetAllAbsences() {
        List<Absence> absences = Arrays.asList(new Absence(), new Absence());
        List<AbsenceDto> absenceDtos = Arrays.asList(new AbsenceDto(), new AbsenceDto());

        when(absenceRepository.findAll()).thenReturn(absences);
        when(entityMapper.mapToAbsenceListDto(absences)).thenReturn(absenceDtos);

        List<AbsenceDto> result = absenceService.getAllAbsences();
        assertEquals(absenceDtos.size(), result.size());
    }

    @Test
    public void testCreateAbsence() throws StudentNotFoundException, CourseNotFoundException {
        AbsenceDto absenceDto = new AbsenceDto(1L, 1L, LocalDate.now());
        Student student = Student.builder().id(1L).userID("student1").build();
        Course course = Course.builder().id(1L).name("course1").build();
        Absence absence = Absence.builder().student(student).course(course).date(LocalDate.now()).build();
        Absence savedAbsence = Absence.builder().id(1L).student(student).course(course).date(LocalDate.now()).build();
        AbsenceDto savedAbsenceDto = new AbsenceDto(1L, 1L, LocalDate.now());

        when(studentRepository.findById(absenceDto.getStudentId())).thenReturn(Optional.of(student));
        when(courseRepository.findById(absenceDto.getCourseId())).thenReturn(Optional.of(course));
        when(entityMapper.mapToAbsenceEntity(absenceDto, student, course)).thenReturn(absence);
        when(absenceRepository.save(absence)).thenReturn(savedAbsence);
        when(entityMapper.mapToAbsenceDto(savedAbsence)).thenReturn(savedAbsenceDto);

        AbsenceDto result = absenceService.createAbsence(absenceDto);
        assertEquals(savedAbsenceDto, result);
    }

    @Test
    public void testCreateAbsenceStudentNotFound() {
        AbsenceDto absenceDto = new AbsenceDto(1L, 1L, LocalDate.now());

        when(studentRepository.findById(absenceDto.getStudentId())).thenReturn(Optional.empty());

        assertThrows(StudentNotFoundException.class, () -> {
            absenceService.createAbsence(absenceDto);
        });
    }

    @Test
    public void testCreateAbsenceCourseNotFound() {
        AbsenceDto absenceDto = new AbsenceDto(1L, 1L, LocalDate.now());
        Student student = Student.builder().id(1L).userID("student1").build();


        when(studentRepository.findById(absenceDto.getStudentId())).thenReturn(Optional.of(student));
        when(courseRepository.findById(absenceDto.getCourseId())).thenReturn(Optional.empty());

        assertThrows(CourseNotFoundException.class, () -> {
            absenceService.createAbsence(absenceDto);
        });
    }

    @Test
    public void testGetPersonalStudentAbsences() throws StudentNotFoundException {
        String userId = "testUser";
        Student student = Student.builder().id(1L).userID(userId).build();
        List<Absence> absences = Arrays.asList(new Absence(), new Absence());
        List<AbsenceStudentViewDto> absenceDtos = Arrays.asList(new AbsenceStudentViewDto(), new AbsenceStudentViewDto());

        when(studentRepository.findByUserID(userId)).thenReturn(Optional.of(student));
        when(absenceRepository.findByStudentId(student.getId())).thenReturn(Optional.of(absences));
        when(entityMapper.mapToAbsenceStudentViewDto(any(Absence.class))).thenReturn(absenceDtos.get(0), absenceDtos.get(1));

        List<AbsenceStudentViewDto> result = absenceService.getPersonalStudentAbsences(userId);
        assertEquals(absenceDtos.size(), result.size());
    }

    @Test
    public void testDeleteAbsenceById() {
        Long absenceId = 1L;

        when(absenceRepository.existsById(absenceId)).thenReturn(true);

        boolean result = absenceService.deleteAbsenceById(absenceId);
        assertTrue(result);
        verify(absenceRepository, times(1)).deleteById(absenceId);
    }

    @Test
    public void testDeleteAbsenceByIdNotFound() {
        Long absenceId = 1L;

        when(absenceRepository.existsById(absenceId)).thenReturn(false);

        boolean result = absenceService.deleteAbsenceById(absenceId);
        assertFalse(result);
        verify(absenceRepository, never()).deleteById(absenceId);
    }

    @Test
    public void testUpdateAbsence() throws StudentNotFoundException, CourseNotFoundException, AbsenceNotFoundException {
        Long absenceId = 1L;
        AbsenceDto absenceDto = new AbsenceDto(1L, 1L, LocalDate.now());
        Absence existingAbsence = Absence.builder().id(absenceId).build();
        Student student = Student.builder().id(1L).userID("student1").build();
        Course course = Course.builder().id(1L).name("course1").build();
        Absence updatedAbsence = Absence.builder().id(absenceId).student(student).course(course).date(LocalDate.now()).build();
        AbsenceDto updatedAbsenceDto = new AbsenceDto(1L, 1L, LocalDate.now());

        when(absenceRepository.findById(absenceId)).thenReturn(Optional.of(existingAbsence));
        when(studentRepository.findById(absenceDto.getStudentId())).thenReturn(Optional.of(student));
        when(courseRepository.findById(absenceDto.getCourseId())).thenReturn(Optional.of(course));
        when(absenceRepository.save(existingAbsence)).thenReturn(updatedAbsence);
        when(entityMapper.mapToAbsenceDto(updatedAbsence)).thenReturn(updatedAbsenceDto);

        AbsenceDto result = absenceService.updateAbsence(absenceId, absenceDto);
        assertEquals(updatedAbsenceDto, result);
    }

    @Test
    public void testUpdateAbsenceNotFound() {
        Long absenceId = 1L;
        AbsenceDto absenceDto = new AbsenceDto(1L, 1L, LocalDate.now());

        when(absenceRepository.findById(absenceId)).thenReturn(Optional.empty());

        assertThrows(AbsenceNotFoundException.class, () -> {
            absenceService.updateAbsence(absenceId, absenceDto);
        });
    }

    @Test
    public void testUpdateAbsenceStudentNotFound() {
        Long absenceId = 1L;
        AbsenceDto absenceDto = new AbsenceDto(1L, 1L, LocalDate.now());
        Absence existingAbsence = Absence.builder().id(absenceId).build();

        when(absenceRepository.findById(absenceId)).thenReturn(Optional.of(existingAbsence));
        when(studentRepository.findById(absenceDto.getStudentId())).thenReturn(Optional.empty());

        assertThrows(StudentNotFoundException.class, () -> {
            absenceService.updateAbsence(absenceId, absenceDto);
        });
    }

    @Test
    public void testUpdateAbsenceCourseNotFound() {
        Long absenceId = 1L;
        AbsenceDto absenceDto = new AbsenceDto(1L, 1L, LocalDate.now());
        Absence existingAbsence = Absence.builder().id(absenceId).build();
        Student student = Student.builder().id(1L).userID("student1").build();

        when(absenceRepository.findById(absenceId)).thenReturn(Optional.of(existingAbsence));
        when(studentRepository.findById(absenceDto.getStudentId())).thenReturn(Optional.of(student));
        when(courseRepository.findById(absenceDto.getCourseId())).thenReturn(Optional.empty());

        assertThrows(CourseNotFoundException.class, () -> {
            absenceService.updateAbsence(absenceId, absenceDto);
        });
    }
}