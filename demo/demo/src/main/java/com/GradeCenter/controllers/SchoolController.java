package com.GradeCenter.controllers;

import com.GradeCenter.dtos.SchoolDto;
import com.GradeCenter.entity.School;
import com.GradeCenter.service.SchoolService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/schools")
public class SchoolController {

    @Autowired
    SchoolService schoolService;

    @PostMapping
    public ResponseEntity<SchoolDto> addSchool(@Valid @RequestBody School school){
        return ResponseEntity.ok(schoolService.saveSchool(school));
    }

    @GetMapping
    public ResponseEntity<List<SchoolDto>> fetchSchoolList(){
        List<SchoolDto> schoolDtoList = schoolService.fetchSchoolList();
        return ResponseEntity.ok(schoolDtoList);
    }

    @GetMapping("/{id}")
    public ResponseEntity<SchoolDto> fetchCourseById(@PathVariable("id") Long schoolId){
        SchoolDto schoolDto = schoolService.fetchSchoolById(schoolId);
        return ResponseEntity.ok(schoolDto);
    }

}
