package com.GradeCenter.controllers;

import com.GradeCenter.dtos.CourseDto;
import com.GradeCenter.dtos.CreateCourseDto;
import com.GradeCenter.dtos.CourseTypeDto;
import com.GradeCenter.dtos.StudentCourseDto;
import com.GradeCenter.service.CourseService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/subjects")
public class CourseController {

    @Autowired
    private CourseService courseService;


    @PostMapping
    public ResponseEntity<CourseDto> addCourse(@Valid @RequestBody CreateCourseDto course) {
        return ResponseEntity.ok(courseService.saveCourse(course));
    }

    @GetMapping
    public ResponseEntity<List<CourseDto>> fetchCourseList() {
        List<CourseDto> courseList = courseService.fetchCourseList();
        return ResponseEntity.ok(courseList);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CourseDto> fetchCourseById(@PathVariable("id") Long courseId) {
        CourseDto course = courseService.fetchCourseById(courseId);
        return ResponseEntity.ok(course);
    }

    @GetMapping("/studentId={id}")
    public ResponseEntity<List<StudentCourseDto>> fetchCourseByStudentId(@PathVariable("id") Long studentId) {
        List<StudentCourseDto> courses = courseService.fetchCourseByStudentId(studentId);
        return ResponseEntity.ok(courses);
    }


    @GetMapping("/studyGroupId={id}")
    public ResponseEntity<List<CourseDto>> fetchCourseByStudyGroupId(@PathVariable("id") Long studyGroupId) {
        List<CourseDto> courses = courseService.fetchCourseByStudyGroupId(studyGroupId);
        return ResponseEntity.ok(courses);
    }

    @PutMapping("/{id}")
    public ResponseEntity<CourseDto> updateCourseById(@PathVariable("id") Long courseId, @RequestBody CreateCourseDto courseDto) {
        return ResponseEntity.ok(courseService.updateCourseById(courseId, courseDto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteCourseById(@PathVariable("id") Long courseId) {
        courseService.deleteCourseById(courseId);
        return ResponseEntity.ok("Course was successfully deleted!");
    }

    @PostMapping("/type")
    public ResponseEntity<CourseTypeDto> addCourseType(@Valid @RequestBody CourseTypeDto course) {
        return ResponseEntity.ok(courseService.addCourseType(course));
    }

    @DeleteMapping("/type/{id}")
    public ResponseEntity<String> deleteCourseTypeById(@PathVariable("id") Long courseTypeId) {
        courseService.deleteCourseTypeById(courseTypeId);
        return ResponseEntity.ok("Course Type was successfully deleted!");
    }

    @PutMapping("/type/{id}")
    public ResponseEntity<CourseTypeDto> updateCourseTypeById(@PathVariable("id") Long courseTypeId, @RequestBody CourseTypeDto courseTypeDto) {
        return ResponseEntity.ok(courseService.updateCourseTypeById(courseTypeId, courseTypeDto));
    }

    @GetMapping("/type")
    public ResponseEntity<List<CourseTypeDto>> fetchCourseTypeList() {
        return ResponseEntity.ok(courseService.fetchCourseTypeList());
    }


}
