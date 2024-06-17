package com.GradeCenter.controllers;

import com.GradeCenter.dtos.SchoolCreateRequest;
import com.GradeCenter.dtos.SchoolDto;
import com.GradeCenter.dtos.SchoolNamesDto;
import com.GradeCenter.dtos.TeacherDto;
import com.GradeCenter.service.SchoolService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/schools")
public class SchoolController {

    @Autowired
    private SchoolService schoolService;


    @GetMapping
    @PreAuthorize("hasRole('admin')")
    public List<SchoolDto> getAllSchools() {
        return schoolService.getAllSchools();
    }

    @GetMapping("/names")
    @PreAuthorize("hasRole('admin')")
    public List<SchoolNamesDto> getAllSchoolsNames() {
        return schoolService.getAllSchoolsNames();
    }


    @GetMapping("/id={id}")
    @PreAuthorize("hasRole('admin')")
    public ResponseEntity<SchoolDto> getSchoolById(@PathVariable("id") Long id) {
        SchoolDto school = schoolService.getSchoolById(id);
        if (school == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(school);
    }

    @PostMapping
    @PreAuthorize("hasRole('admin')")
    public ResponseEntity<SchoolDto> addSchool(@RequestBody SchoolCreateRequest schoolCreateRequest) {
        SchoolDto school = schoolService.addSchool(schoolCreateRequest);
        return ResponseEntity.ok(school);
    }

    @DeleteMapping("/id={id}")
    @PreAuthorize("hasRole('admin')")
    public ResponseEntity<String> deleteSchool(@PathVariable("id") Long id) {
        boolean isDeleted = schoolService.deleteSchool(id);
        if (isDeleted) {
            return ResponseEntity.ok("School deleted successfully");
        } else {
            return ResponseEntity.status(404).body("Failed to delete school");
        }
    }

    @PutMapping("/id={id}")
    @PreAuthorize("hasRole('admin')")
    public ResponseEntity<SchoolDto> updateSchool(@PathVariable("id") Long id, @RequestBody SchoolDto schoolDto) {
        SchoolDto updatedSchool = schoolService.updateSchool(id, schoolDto);
        if (updatedSchool == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(updatedSchool);
    }

    @GetMapping("/id={id}/teachers")
    @PreAuthorize("hasAnyRole('admin', 'director')")
    public ResponseEntity<List<TeacherDto>> getTeachersBySchoolId(@PathVariable("id") Long schoolId) {
        List<TeacherDto> teachers = schoolService.getTeachersBySchoolId(schoolId);
        return ResponseEntity.ok(teachers);
    }
}
