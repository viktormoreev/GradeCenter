package com.GradeCenter.controllers;

import com.GradeCenter.dtos.TeacherDto;
import com.GradeCenter.dtos.UserIDRequest;
import com.GradeCenter.service.TeacherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/teachers")
public class TeacherController {

    @Autowired
    private TeacherService teacherService;

    @GetMapping
    @PreAuthorize("hasRole('admin')")
    public List<TeacherDto> getAllTeachers() {
        return teacherService.getAllTeachers();
    }

    @GetMapping("/me")
    @PreAuthorize("hasRole('teacher')")
    public ResponseEntity<TeacherDto> getPersonalTeacher() {
        Jwt jwt = (Jwt) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String userId = jwt.getClaimAsString("sub");

        Optional<TeacherDto> teacher = teacherService.getTeacherByUId(userId);
        if (teacher.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return teacher.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/id={id}")
    @PreAuthorize("hasRole('admin')")
    public ResponseEntity<TeacherDto> getTeacherById(@PathVariable("id") Long id) {
        Optional<TeacherDto> teacher = teacherService.getTeacherById(id);
        if (teacher.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return teacher.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/uid={id}")
    @PreAuthorize("hasRole('admin')")
    public ResponseEntity<TeacherDto> getTeacherByUId(@PathVariable("id") String userID) {
        Optional<TeacherDto> teacher = teacherService.getTeacherByUId(userID);
        if (teacher.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return teacher.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    @PreAuthorize("hasRole('admin')")
    public ResponseEntity<TeacherDto> addTeacher(@RequestBody UserIDRequest userIDRequest) {
        TeacherDto teacher = teacherService.addTeacher(userIDRequest);
        return ResponseEntity.ok(teacher);
    }

    @DeleteMapping("/id={id}")
    @PreAuthorize("hasRole('admin')")
    public ResponseEntity<String> deleteTeacherId(@PathVariable("id") Long id) {
        ResponseEntity<String> response = teacherService.deleteTeacherID(id);
        if (response.getStatusCode().is2xxSuccessful()) {
            return ResponseEntity.ok("Teacher deleted successfully");
        } else {
            return ResponseEntity.status(response.getStatusCode()).body("Failed to delete teacher");
        }
    }

    @DeleteMapping("/uid={id}")
    @PreAuthorize("hasRole('admin')")
    public ResponseEntity<String> deleteTeacherUID(@PathVariable("id") String userID) {
        ResponseEntity<String> response = teacherService.deleteTeacherUID(userID);
        if (response.getStatusCode().is2xxSuccessful()) {
            return ResponseEntity.ok("Teacher deleted successfully");
        } else {
            return ResponseEntity.status(response.getStatusCode()).body("Failed to delete teacher");
        }
    }

    @PutMapping("/id={id}")
    @PreAuthorize("hasRole('admin')")
    public ResponseEntity<TeacherDto> updateTeacherID(@PathVariable("id") Long id, @RequestBody TeacherDto teacherDto) {
        Optional<TeacherDto> response = teacherService.updateTeacherID(id, teacherDto);
        if (response.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(teacherDto);
    }

    @PutMapping("/uid={id}")
    @PreAuthorize("hasRole('admin')")
    public ResponseEntity<TeacherDto> updateTeacherUID(@PathVariable("id") String userID, @RequestBody TeacherDto teacherDto) {
        Optional<TeacherDto> response = teacherService.updateTeacherUID(userID, teacherDto);
        if (response.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(teacherDto);
    }
}
