package com.GradeCenter.demo.Tests;

import com.GradeCenter.dtos.*;
import com.GradeCenter.entity.*;
import com.GradeCenter.mapper.EntityMapper;
import com.GradeCenter.repository.*;
import com.GradeCenter.service.implementation.CourseServiceImpl;
import jakarta.persistence.EntityNotFoundException;
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

class CourseServiceImplTest {

    @Mock
    private EntityMapper entityMapper;
    @Mock
    private CourseRepository courseRepository;
    @Mock
    private CourseTypeRepository courseTypeRepository;
    @Mock
    private StudyGroupRepository studyGroupRepository;
    @Mock
    private StudentRepository studentRepository;

    @InjectMocks
    private CourseServiceImpl courseService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void fetchCourseList() {
        List<Course> courses = Arrays.asList(new Course(), new Course());
        List<CourseDto> courseDtos = Arrays.asList(new CourseDto(), new CourseDto());

        when(courseRepository.findAll()).thenReturn(courses);
        when(entityMapper.mapToCourseListDto(courses)).thenReturn(courseDtos);

        List<CourseDto> result = courseService.fetchCourseList();

        assertEquals(courseDtos, result);
        verify(courseRepository).findAll();
        verify(entityMapper).mapToCourseListDto(courses);
    }

    @Test
    void saveCourse() {
        CreateCourseDto createCourseDto = new CreateCourseDto();
        createCourseDto.setName("Test Course");
        createCourseDto.setCourseTypeId(1L);

        CourseType courseType = new CourseType();
        Course course = new Course();
        CourseDto courseDto = new CourseDto();

        when(courseTypeRepository.findById(1L)).thenReturn(Optional.of(courseType));
        when(courseRepository.save(any(Course.class))).thenReturn(course);
        when(entityMapper.mapToCourseDto(course)).thenReturn(courseDto);

        CourseDto result = courseService.saveCourse(createCourseDto);

        assertEquals(courseDto, result);
        verify(courseTypeRepository).findById(1L);
        verify(courseRepository).save(any(Course.class));
        verify(entityMapper).mapToCourseDto(course);
    }

    @Test
    void fetchCourseById_existingCourse() {
        Long courseId = 1L;
        Course course = new Course();
        CourseDto courseDto = new CourseDto();

        when(courseRepository.findById(courseId)).thenReturn(Optional.of(course));
        when(entityMapper.mapToCourseDto(course)).thenReturn(courseDto);

        CourseDto result = courseService.fetchCourseById(courseId);

        assertEquals(courseDto, result);
        verify(courseRepository).findById(courseId);
        verify(entityMapper).mapToCourseDto(course);
    }

    @Test
    void fetchCourseById_nonExistingCourse() {
        Long courseId = 1L;

        when(courseRepository.findById(courseId)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> courseService.fetchCourseById(courseId));
        verify(courseRepository).findById(courseId);
    }

    @Test
    void updateCourseById_existingCourse() {
        Long courseId = 1L;
        CreateCourseDto updateDto = new CreateCourseDto();
        updateDto.setName("Updated Course");
        updateDto.setCourseTypeId(2L);

        Course existingCourse = new Course();
        CourseType newCourseType = new CourseType();
        CourseDto updatedCourseDto = new CourseDto();

        when(courseRepository.findById(courseId)).thenReturn(Optional.of(existingCourse));
        when(courseTypeRepository.findById(2L)).thenReturn(Optional.of(newCourseType));
        when(courseRepository.save(existingCourse)).thenReturn(existingCourse);
        when(entityMapper.mapToCourseDto(existingCourse)).thenReturn(updatedCourseDto);

        CourseDto result = courseService.updateCourseById(courseId, updateDto);

        assertEquals(updatedCourseDto, result);
        verify(courseRepository).findById(courseId);
        verify(courseTypeRepository).findById(2L);
        verify(courseRepository).save(existingCourse);
        verify(entityMapper).mapToCourseDto(existingCourse);
    }

    @Test
    void deleteCourseById() {
        Long courseId = 1L;

        boolean result = courseService.deleteCourseById(courseId);

        assertTrue(result);
        verify(courseRepository).deleteById(courseId);
    }

    @Test
    void fetchCourseByStudentId_existingStudentWithCourses() {
        Long studentId = 1L;
        Student student = new Student();
        StudyGroup studyGroup = new StudyGroup();
        student.setClasses(studyGroup);
        List<Course> courses = Arrays.asList(new Course(), new Course());
        studyGroup.setCourses(courses);
        List<StudentCourseDto> studentCourseDtos = Arrays.asList(new StudentCourseDto(), new StudentCourseDto());

        when(studentRepository.findById(studentId)).thenReturn(Optional.of(student));
        when(studyGroupRepository.findById(studyGroup.getId())).thenReturn(Optional.of(studyGroup));
        when(entityMapper.mapToStudentCourseListDto(courses, studentId)).thenReturn(studentCourseDtos);

        List<StudentCourseDto> result = courseService.fetchCourseByStudentId(studentId);

        assertEquals(studentCourseDtos, result);
        verify(studentRepository).findById(studentId);
        verify(studyGroupRepository).findById(studyGroup.getId());
        verify(entityMapper).mapToStudentCourseListDto(courses, studentId);
    }


}