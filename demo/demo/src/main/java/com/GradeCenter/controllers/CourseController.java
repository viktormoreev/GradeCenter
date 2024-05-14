package com.GradeCenter.controllers;

import com.GradeCenter.dtos.CourseDto;
import com.GradeCenter.entity.Course;
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
    CourseService courseService;

    @PostMapping
    public ResponseEntity<Course> addCourse(@Valid @RequestBody Course course){
        return ResponseEntity.ok(courseService.saveSubject(course));
    }

    @GetMapping
    public ResponseEntity<List<CourseDto>> fetchCourseList(){
        List<CourseDto> courseList = courseService.fetchCourseList();
        return ResponseEntity.ok(courseList);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CourseDto> fetchCourseById(@PathVariable("id") Long courseId){
        CourseDto course = courseService.fetchCourseById(courseId);
        return ResponseEntity.ok(course);
    }


}
