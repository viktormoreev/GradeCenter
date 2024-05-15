package com.GradeCenter.service;

import com.GradeCenter.dtos.CourseDto;
import com.GradeCenter.entity.Course;

import java.util.List;

public interface CourseService {
    List<CourseDto> fetchCourseList();

    Course saveSubject(Course course);

    CourseDto fetchCourseById(Long courseId);
}