package com.GradeCenter.controllers;

import com.GradeCenter.dtos.GradeDto;
import com.GradeCenter.dtos.GradeStudentViewDto;
import com.GradeCenter.exceptions.CourseNotFoundException;
import com.GradeCenter.exceptions.GradeNotFoundException;
import com.GradeCenter.exceptions.StudentNotFoundException;
import com.GradeCenter.service.GradeService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/grades")
public class GradeController {

    @Autowired
    private GradeService gradeService;

    @GetMapping
    @PreAuthorize("hasAnyRole('admin', 'director', 'teacher')")
    public List<GradeDto> getAllGrades() {
        return gradeService.getAllGrades();
    }

    @GetMapping("/student/{studentId}")
    @PreAuthorize("hasAnyRole('admin', 'director', 'teacher')")
    public ResponseEntity<List<GradeDto>> getGradesByStudentId(@PathVariable long studentId) throws StudentNotFoundException {
        List<GradeDto> grades = gradeService.getGradesByStudentId(studentId);
        if (grades.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(grades);
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('admin', 'director', 'teacher')")
    public ResponseEntity<GradeDto> createGrade(@Valid @RequestBody GradeDto gradeDto) {
        try {
            GradeDto createdGrade = gradeService.createGrade(gradeDto);
            return ResponseEntity.ok(createdGrade);
        } catch (StudentNotFoundException | CourseNotFoundException e) {
            return ResponseEntity.noContent().build();
        }
    }

    @PutMapping("/id={id}")
    @PreAuthorize("hasAnyRole('admin', 'director', 'teacher')")
    public ResponseEntity<GradeDto> updateGrade(@PathVariable("id") Long id, @Valid @RequestBody GradeDto gradeDto) {
        try {
            GradeDto updatedGrade = gradeService.updateGrade(id, gradeDto);
            return ResponseEntity.ok(updatedGrade);
        } catch (GradeNotFoundException | StudentNotFoundException | CourseNotFoundException e) {
            return ResponseEntity.status(404).body(null);
        }
    }

    @DeleteMapping("/id={id}")
    @PreAuthorize("hasAnyRole('admin', 'director', 'teacher')")
    public ResponseEntity<String> deleteGradeById(@PathVariable("id") Long id) {
        boolean isDeleted = gradeService.deleteGradeById(id);
        if (isDeleted) {
            return ResponseEntity.ok("Grade deleted successfully");
        } else {
            return ResponseEntity.status(404).body("Failed to delete grade");
        }
    }

    @GetMapping("/me")
    @PreAuthorize("hasRole('student')")
    public ResponseEntity<List<GradeStudentViewDto>> getPersonalStudentGrades() throws StudentNotFoundException {
        Jwt jwt = (Jwt) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String userId = jwt.getClaimAsString("sub");

        List<GradeStudentViewDto> grades = gradeService.getPersonalStudentGrades(userId);
        if (grades.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(grades);
    }

    //@TODO TEST ENDPOINTS, ADD GRADES IN THE DEPENDING DTOS of courses and students, svurji gi kudeto trqvba
}
