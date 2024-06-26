package com.GradeCenter.controllers;

import com.GradeCenter.dtos.AbsenceDto;
import com.GradeCenter.dtos.AbsenceStudentViewDto;
import com.GradeCenter.dtos.AbsenceTeacherViewDto;
import com.GradeCenter.exceptions.AbsenceNotFoundException;
import com.GradeCenter.exceptions.CourseNotFoundException;
import com.GradeCenter.exceptions.StudentNotFoundException;
import com.GradeCenter.service.AbsenceService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/absences")
public class AbsenceController {

    @Autowired
    private AbsenceService absenceService;

    @GetMapping
    @PreAuthorize("hasAnyRole('admin', 'director', 'teacher')")
    public List<AbsenceDto> getAllAbsences() {
        return absenceService.getAllAbsences();
    }

    @GetMapping("/me")
    @PreAuthorize("hasRole('student')")
    public ResponseEntity<List<AbsenceStudentViewDto>> getPersonalStudentAbsences() throws StudentNotFoundException {
        Jwt jwt = (Jwt) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String userId = jwt.getClaimAsString("sub");

        List<AbsenceStudentViewDto> absences = absenceService.getPersonalStudentAbsences(userId);
        if (absences.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(absences);
    }

    @GetMapping("/student/{studentId}")
    @PreAuthorize("hasAnyRole('admin', 'director', 'teacher')")
    public ResponseEntity<List<AbsenceDto>> getAbsencesByStudentId(@PathVariable long studentId) throws StudentNotFoundException {
        List<AbsenceDto> absences = absenceService.getAllAbsencesByStudentIdForAdmin(studentId);
        if (absences.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(absences);
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('admin', 'director', 'teacher')")
    public ResponseEntity<AbsenceDto> createAbsence(@Valid @RequestBody AbsenceDto absenceDto) throws CourseNotFoundException, StudentNotFoundException {
        AbsenceDto createdAbsence = absenceService.createAbsence(absenceDto);
        return ResponseEntity.ok(createdAbsence);
    }

    @DeleteMapping("/id={id}")
    @PreAuthorize("hasAnyRole('admin', 'director', 'teacher')")
    public ResponseEntity<String> deleteAbsenceById(@PathVariable("id") Long id) {
        boolean isDeleted = absenceService.deleteAbsenceById(id);
        if (isDeleted) {
            return ResponseEntity.ok("Absence deleted successfully");
        } else {
            return ResponseEntity.status(404).body("Failed to delete absence");
        }
    }

    @PutMapping("/id={id}")
    @PreAuthorize("hasAnyRole('admin', 'director', 'teacher')")
    public ResponseEntity<AbsenceDto> updateAbsence(@PathVariable("id") Long id, @Valid @RequestBody AbsenceDto absenceDto) {
        try {
            AbsenceDto updatedAbsence = absenceService.updateAbsence(id, absenceDto);
            return ResponseEntity.ok(updatedAbsence);
        } catch (AbsenceNotFoundException | StudentNotFoundException | CourseNotFoundException e) {
            return ResponseEntity.status(404).body(null);
        }
    }

    @GetMapping("/teacher/student/{studentId}")
    @PreAuthorize("hasAnyRole('admin', 'director', 'teacher')")
    public ResponseEntity<List<AbsenceTeacherViewDto>> getTeacherViewAbsencesByStudentId(@PathVariable long studentId) throws AbsenceNotFoundException {
        List<AbsenceTeacherViewDto> absences = absenceService.getTeacherViewAbsencesByStudentId(studentId);
        if (absences.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(absences);
    }


}
