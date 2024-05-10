package com.GradeCenter.controllers;

import com.GradeCenter.dtos.SubjectDto;
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
    public Course addSubject(@Valid @RequestBody Course course){
        return courseService.saveSubject(course);
    }

    @GetMapping
    public ResponseEntity<List<SubjectDto>> fetchSubjectsList(){
        List<SubjectDto> subjectList = courseService.fetchSubjectsList();
        return ResponseEntity.ok(subjectList);
    }


}
