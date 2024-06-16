package com.GradeCenter.controllers;

import com.GradeCenter.dtos.QualificationDto;
import com.GradeCenter.exceptions.QualificationNotFoundException;
import com.GradeCenter.service.QualificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/qualifications")
public class QualificationController {

    @Autowired
    private QualificationService qualificationService;

    @GetMapping
    @PreAuthorize("hasAnyRole('admin', 'director', 'teacher')")
    public ResponseEntity<List<QualificationDto>> getAllQualifications() {
        List<QualificationDto> qualifications = qualificationService.getAllQualifications();
        return ResponseEntity.ok(qualifications);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('admin', 'director', 'teacher')")
    public ResponseEntity<QualificationDto> getQualificationById(@PathVariable Long id) {
        try {
            QualificationDto qualification = qualificationService.getQualificationById(id);
            return ResponseEntity.ok(qualification);
        } catch (QualificationNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('admin', 'director', 'teacher')")
    public ResponseEntity<QualificationDto> createQualification(@Valid @RequestBody QualificationDto qualificationDto) {
        QualificationDto createdQualification = qualificationService.createQualification(qualificationDto);
        return ResponseEntity.ok(createdQualification);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('admin', 'director', 'teacher')")
    public ResponseEntity<QualificationDto> updateQualification(@PathVariable Long id, @Valid @RequestBody QualificationDto qualificationDto) {
        try {
            QualificationDto updatedQualification = qualificationService.updateQualification(id, qualificationDto);
            return ResponseEntity.ok(updatedQualification);
        } catch (QualificationNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('admin', 'director', 'teacher')")
    public ResponseEntity<Void> deleteQualification(@PathVariable Long id) {
        qualificationService.deleteQualification(id);
        return ResponseEntity.noContent().build();
    }
}
