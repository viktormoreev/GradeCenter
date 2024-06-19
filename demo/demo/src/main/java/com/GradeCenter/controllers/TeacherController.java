package com.GradeCenter.controllers;

import com.GradeCenter.dtos.FetchTeacherDto;
import com.GradeCenter.dtos.TeacherDto;
import com.GradeCenter.dtos.TeacherUpdateDto;
import com.GradeCenter.dtos.UserIDRequest;
import com.GradeCenter.service.TeacherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/teachers")
public class TeacherController {

    @Autowired
    private TeacherService teacherService;

    @GetMapping
    //@PreAuthorize("hasRole('admin')")
    public List<FetchTeacherDto> getAllTeachers() {
        return teacherService.getAllTeachers();
    }

    @GetMapping("/me")
    //@PreAuthorize("hasRole('teacher')")
    public ResponseEntity<FetchTeacherDto> getPersonalTeacher() {
        Jwt jwt = (Jwt) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String userId = jwt.getClaimAsString("sub");

        FetchTeacherDto teacher = teacherService.getTeacherByUId(userId);
        if (teacher == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(teacher);
    }

    @GetMapping("/id={id}")
    //@PreAuthorize("hasRole('admin')")
    public ResponseEntity<FetchTeacherDto> getTeacherById(@PathVariable("id") Long id) {
        FetchTeacherDto teacher = teacherService.getTeacherById(id);
        if (teacher == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(teacher);
    }

    @GetMapping("/uid={id}")
    //@PreAuthorize("hasRole('admin')")
    public ResponseEntity<FetchTeacherDto> getTeacherByUId(@PathVariable("id") String userID) {
        FetchTeacherDto teacher = teacherService.getTeacherByUId(userID);
        if (teacher == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(teacher);
    }

    @PostMapping
    //@PreAuthorize("hasRole('admin')")
    public ResponseEntity<TeacherDto> addTeacher(@RequestBody UserIDRequest userIDRequest) {
        TeacherDto teacher = teacherService.addTeacher(userIDRequest);
        return ResponseEntity.ok(teacher);
    }

    @DeleteMapping("/id={id}")
    //@PreAuthorize("hasRole('admin')")
    public ResponseEntity<String> deleteTeacherId(@PathVariable("id") Long id) {
        boolean isDeleted = teacherService.deleteTeacherID(id);
        if (isDeleted) {
            return ResponseEntity.ok("Teacher deleted successfully");
        } else {
            return ResponseEntity.status(404).body("Failed to delete teacher");
        }
    }

    @DeleteMapping("/uid={id}")
    //@PreAuthorize("hasRole('admin')")
    public ResponseEntity<String> deleteTeacherUID(@PathVariable("id") String userID) {
        boolean isDeleted = teacherService.deleteTeacherUID(userID);
        if (isDeleted) {
            return ResponseEntity.ok("Teacher deleted successfully");
        } else {
            return ResponseEntity.status(404).body("Failed to delete teacher");
        }
    }

    @PatchMapping("/remove-teacher-school/id={id}")
    //@PreAuthorize("hasRole('admin')")
    public ResponseEntity<String> removeTeacherFromSchool(@PathVariable("id") Long id) {
        boolean isDeleted = teacherService.removeTeacherFromSchool(id);
        if (isDeleted) {
            return ResponseEntity.ok("Teacher removed from school successfully");
        } else {
            return ResponseEntity.status(404).body("Failed to remove teacher from school");
        }
    }

    @PutMapping("/id={id}")
    //@PreAuthorize("hasRole('admin')")
    public ResponseEntity<TeacherDto> updateTeacherID(@PathVariable("id") Long id, @RequestBody TeacherUpdateDto teacherUpdateDto) {
        TeacherDto updatedTeacher = teacherService.updateTeacherID(id, teacherUpdateDto);
        if (updatedTeacher == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(updatedTeacher);
    }

    @PutMapping("/uid={id}")
    @PreAuthorize("hasRole('admin')")
    public ResponseEntity<TeacherDto> updateTeacherUID(@PathVariable("id") String userID, @RequestBody TeacherUpdateDto teacherUpdateDto) {
        TeacherDto updatedTeacher = teacherService.updateTeacherUID(userID, teacherUpdateDto);
        if (updatedTeacher == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(updatedTeacher);
    }
}
