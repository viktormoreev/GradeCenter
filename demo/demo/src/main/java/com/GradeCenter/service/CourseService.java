package com.GradeCenter.service;

import com.GradeCenter.dtos.CourseDto;
import com.GradeCenter.dtos.StudentCourseDto;
import com.GradeCenter.entity.Course;

import java.util.List;

public interface CourseService {
    List<CourseDto> fetchCourseList();

    CourseDto saveCourse(Course course);

    CourseDto fetchCourseById(Long courseId);

    CourseDto updateCourseById(Long courseId);

    boolean deleteCourseById(Long courseId);

    List<StudentCourseDto> fetchCourseByStudentId(Long studentId);

    List<CourseDto> fetchCourseByStudyGroupId(Long studyGroupId);
}
