package com.GradeCenter.demo.Tests;

import com.GradeCenter.controllers.CourseController;
import com.GradeCenter.dtos.*;
import com.GradeCenter.service.CourseService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(CourseController.class)
class CourseControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CourseService courseService;

    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        objectMapper = new ObjectMapper();
    }

    @Test
    void addCourse() throws Exception {
        CreateCourseDto createCourseDto = new CreateCourseDto();
        createCourseDto.setName("Test Course");
        createCourseDto.setCourseTypeId(1L);

        CourseDto courseDto = new CourseDto();
        courseDto.setName("Test Course");

        when(courseService.saveCourse(any(CreateCourseDto.class))).thenReturn(courseDto);

        mockMvc.perform(post("/subjects")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createCourseDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Test Course"));

        verify(courseService).saveCourse(any(CreateCourseDto.class));
    }

    @Test
    void fetchCourseList() throws Exception {
        List<CourseDto> courseDtos = Arrays.asList(
                new CourseDto("Course 1", null, null),
                new CourseDto("Course 2", null, null)
        );

        when(courseService.fetchCourseList()).thenReturn(courseDtos);

        mockMvc.perform(get("/subjects"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("Course 1"))
                .andExpect(jsonPath("$[1].name").value("Course 2"));

        verify(courseService).fetchCourseList();
    }

    @Test
    void fetchCourseById() throws Exception {
        Long courseId = 1L;
        CourseDto courseDto = new CourseDto("Test Course", null, null);

        when(courseService.fetchCourseById(courseId)).thenReturn(courseDto);

        mockMvc.perform(get("/subjects/{id}", courseId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Test Course"));

        verify(courseService).fetchCourseById(courseId);
    }

    @Test
    void fetchCourseByStudentId() throws Exception {
        Long studentId = 1L;
        List<StudentCourseDto> studentCourseDtos = Arrays.asList(
                new StudentCourseDto(),
                new StudentCourseDto()
        );

        when(courseService.fetchCourseByStudentId(studentId)).thenReturn(studentCourseDtos);

        mockMvc.perform(get("/subjects/studentId={id}", studentId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(2));

        verify(courseService).fetchCourseByStudentId(studentId);
    }

    @Test
    void fetchCourseByStudyGroupId() throws Exception {
        Long studyGroupId = 1L;
        List<CourseDto> courseDtos = Arrays.asList(
                new CourseDto("Course 1", null, null),
                new CourseDto("Course 2", null, null)
        );

        when(courseService.fetchCourseByStudyGroupId(studyGroupId)).thenReturn(courseDtos);

        mockMvc.perform(get("/subjects/studyGroupId={id}", studyGroupId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("Course 1"))
                .andExpect(jsonPath("$[1].name").value("Course 2"));

        verify(courseService).fetchCourseByStudyGroupId(studyGroupId);
    }

    @Test
    void updateCourseById() throws Exception {
        Long courseId = 1L;
        CreateCourseDto updateDto = new CreateCourseDto();
        updateDto.setName("Updated Course");
        updateDto.setCourseTypeId(2L);

        CourseDto updatedCourseDto = new CourseDto("Updated Course", null, null);

        when(courseService.updateCourseById(eq(courseId), any(CreateCourseDto.class))).thenReturn(updatedCourseDto);

        mockMvc.perform(put("/subjects/{id}", courseId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Updated Course"));

        verify(courseService).updateCourseById(eq(courseId), any(CreateCourseDto.class));
    }

    @Test
    void deleteCourseById() throws Exception {
        Long courseId = 1L;

        when(courseService.deleteCourseById(courseId)).thenReturn(true);

        mockMvc.perform(delete("/subjects/{id}", courseId))
                .andExpect(status().isOk())
                .andExpect(content().string("Course was successfully deleted!"));

        verify(courseService).deleteCourseById(courseId);
    }

    @Test
    void addCourseType() throws Exception {
        CourseTypeDto courseTypeDto = new CourseTypeDto();
        courseTypeDto.setName("New Course Type");

        when(courseService.addCourseType(any(CourseTypeDto.class))).thenReturn(courseTypeDto);

        mockMvc.perform(post("/subjects/type")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(courseTypeDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("New Course Type"));

        verify(courseService).addCourseType(any(CourseTypeDto.class));
    }

    // Add more tests for other endpoints...
}