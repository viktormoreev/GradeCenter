package com.GradeCenter.controllers;

import com.GradeCenter.dtos.CourseDto;
import com.GradeCenter.entity.StudyGroup;
import com.GradeCenter.service.StudyGroupService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/studyGroup")
public class StudyGroupController {

    @Autowired
    StudyGroupService studyGroupService;

    @PostMapping
    public StudyGroup addStudyGroup(@Valid @RequestBody StudyGroup studyGroup){ // needs to be added to teachers
        return studyGroupService.saveStudyGroup(studyGroup);
    }

    @GetMapping
    public ResponseEntity<List<CourseDto>> fetchStudyGroupList(){
        List<CourseDto> courseList = studyGroupService.fetchCourseList();
        return ResponseEntity.ok(courseList);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CourseDto> fetchStudyGroupById(@PathVariable("id") Long courseId){
        CourseDto course = courseService.fetchCourseById(courseId);
        return ResponseEntity.ok(course);
    }

    @PutMapping("/{id}")
    public ResponseEntity<CourseDto> updateStudyGroupById(@PathVariable("id") Long courseId){
        return ResponseEntity.ok(courseService.updateCourseById(courseId));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteStudyGroupById(@PathVariable("id")Long companyId){
        courseService.deleteCourseById(companyId);
        return ResponseEntity.ok("Course was successfully deleted!");
    }

}
