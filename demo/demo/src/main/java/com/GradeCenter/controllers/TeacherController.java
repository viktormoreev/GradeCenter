package com.GradeCenter.controllers;

import com.GradeCenter.dtos.CourseDto;
import com.GradeCenter.dtos.TeacherDto;
import com.GradeCenter.entity.Teacher;
import com.GradeCenter.service.TeacherService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/teachers")
public class TeacherController {

    @Autowired
    TeacherService teacherService;

    @PostMapping
    public ResponseEntity<TeacherDto> addTeacher(@Valid @RequestBody Teacher teacher){
        return ResponseEntity.ok(teacherService.saveTeacher(teacher));
    }

    @GetMapping
    public ResponseEntity<List<TeacherDto>> fetchTeacherList(){
        List<TeacherDto> teacherDtoList = teacherService.fetchTeacherList();
        return ResponseEntity.ok(teacherDtoList);
    }

    @GetMapping("/{id}")
    public ResponseEntity<TeacherDto> fetchCourseById(@PathVariable("id") Long teacherId){
        TeacherDto teacherDto = teacherService.fetchTeacherById(teacherId);
        return ResponseEntity.ok(teacherDto);
    }
    
}
