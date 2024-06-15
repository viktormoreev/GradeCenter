package com.GradeCenter.controllers;

import com.GradeCenter.dtos.CourseDto;
import com.GradeCenter.dtos.StudentCourseDto;
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
    public ResponseEntity<CourseDto> addCourse(@Valid @RequestBody Course course){
        return ResponseEntity.ok(courseService.saveCourse(course));
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

    @GetMapping("/studentId={id}")
    public ResponseEntity<List<StudentCourseDto>> fetchCourseByStudentId(@PathVariable("id") Long studentId){
        List<StudentCourseDto> courses = courseService.fetchCourseByStudentId(studentId);
        return ResponseEntity.ok(courses);
    }


    @GetMapping("/studyGroupId={id}")
    public ResponseEntity<List<CourseDto>> fetchCourseByStudyGroupId(@PathVariable("id") Long studyGroupId){
        List<CourseDto> courses = courseService.fetchCourseByStudyGroupId(studyGroupId);
        return ResponseEntity.ok(courses);
    }

    @PutMapping("/{id}")
    public ResponseEntity<CourseDto> updateCourseById(@PathVariable("id") Long courseId){
        return ResponseEntity.ok(courseService.updateCourseById(courseId));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteCourseById(@PathVariable("id")Long companyId){
        courseService.deleteCourseById(companyId);
        return ResponseEntity.ok("Course was successfully deleted!");
    }




}
