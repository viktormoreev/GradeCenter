package com.GradeCenter.controllers;

import com.GradeCenter.dtos.SchoolDto;
import com.GradeCenter.entity.SchoolHour;
import com.GradeCenter.dtos.SchoolHourCreateRequest;
import com.GradeCenter.service.SchoolHourService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/school-hours")
public class SchoolHourController {

    @Autowired
    private SchoolHourService schoolHourService;

    @GetMapping
    public List<SchoolHour> getAllSchoolHours() {
        return schoolHourService.findAll();
    }

    @GetMapping("/id={id}")
    @PreAuthorize("hasAnyRole('admin', 'director')")
    public ResponseEntity<SchoolHour> getSchoolHourById(@PathVariable Long id) {
        SchoolHour schoolHour = schoolHourService.findById(id);
        return schoolHour != null ? ResponseEntity.ok(schoolHour) : ResponseEntity.notFound().build();
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('admin', 'director')")
    public ResponseEntity<SchoolHour> createSchoolHour(@RequestBody SchoolHourCreateRequest schoolHour) {
        SchoolHour createdSchoolHour = schoolHourService.save(schoolHour);
        return schoolHour != null ? ResponseEntity.ok(createdSchoolHour) : ResponseEntity.badRequest().build();
    }

    @PutMapping("/id={id}")
    @PreAuthorize("hasAnyRole('admin', 'director')")
    public ResponseEntity<SchoolHour> updateSchoolHour(@PathVariable Long id, @RequestBody SchoolHourCreateRequest schoolHourDetails) {
        SchoolHour updatedSchoolHour = schoolHourService.update(id, schoolHourDetails);
        return updatedSchoolHour != null ? ResponseEntity.ok(updatedSchoolHour) : ResponseEntity.notFound().build();
    }

    @DeleteMapping("/id={id}")
    @PreAuthorize("hasAnyRole('admin', 'director')")
    public ResponseEntity<String> deleteSchoolHour(@PathVariable Long id) {
        boolean deleted = schoolHourService.deleteById(id);
        return deleted ? ResponseEntity.ok("School hour deleted successfully") : ResponseEntity.notFound().build();
    }
}