package com.GradeCenter.service;

import com.GradeCenter.dtos.CourseDto;
import com.GradeCenter.dtos.CreateCourseDto;
import com.GradeCenter.dtos.CourseTypeDto;
import com.GradeCenter.dtos.StudentCourseDto;

import java.util.List;

public interface CourseService {
    List<CourseDto> fetchCourseList();

    CourseDto saveCourse(CreateCourseDto course);

    CourseDto fetchCourseById(Long courseId);

    CourseDto updateCourseById(Long courseId, CreateCourseDto courseDto);

    boolean deleteCourseById(Long courseId);

    List<StudentCourseDto> fetchCourseByStudentId(Long studentId);

    List<CourseDto> fetchCourseByStudyGroupId(Long studyGroupId);

    CourseTypeDto addCourseType(CourseTypeDto course);

    void deleteCourseTypeById(Long courseTypeId);

    CourseTypeDto updateCourseTypeById(Long courseTypeId, CourseTypeDto courseTypeDto);

    List<CourseTypeDto> fetchCourseTypeList();
}
