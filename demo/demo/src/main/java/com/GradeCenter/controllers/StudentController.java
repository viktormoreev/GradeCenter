package com.GradeCenter.controllers;

import com.GradeCenter.dtos.*;
import com.GradeCenter.service.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/students")
public class StudentController {

    @Autowired
    private StudentService studentService;

    @GetMapping
    @PreAuthorize("hasRole('admin')")
    public List<StudentDto> getAllStudents() {
        return studentService.getAllStudents();
    }

    @GetMapping("/full")
    @PreAuthorize("hasRole('admin')")
    public List<StudentFullReturnDto> getAllStudentsFull() {
        return studentService.getAllStudentsFull();
    }

    @GetMapping("full/id={id}")
    @PreAuthorize("hasRole('admin')")
    public ResponseEntity<StudentFullReturnDto> getFullStudentById(@PathVariable("id") Long id) {
        StudentFullReturnDto student = studentService.getFullStudentById(id);
        if (student == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(student);
    }

    @GetMapping("/me")
    @PreAuthorize("hasRole('student')")
    public ResponseEntity<StudentDto> getPersonalStudent() {
        Jwt jwt = (Jwt) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String userId = jwt.getClaimAsString("sub");

        StudentDto student = studentService.getStudentByUId(userId);
        if (student == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(student);
    }

    @GetMapping("/id={id}")
    @PreAuthorize("hasRole('admin')")
    public ResponseEntity<StudentDto> getStudentById(@PathVariable("id") Long id) {
        StudentDto student = studentService.getStudentById(id);
        if (student == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(student);
    }

    @GetMapping("/uid={id}")
    @PreAuthorize("hasRole('admin')")
    public ResponseEntity<StudentDto> getStudentByUId(@PathVariable("id") String userID) {
        StudentDto student = studentService.getStudentByUId(userID);
        if (student == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(student);
    }

    @PostMapping
    @PreAuthorize("hasRole('admin')")
    public ResponseEntity<StudentDto> addStudent(@RequestBody UserIDRequest userIDRequest) {
        StudentDto student = studentService.addStudent(userIDRequest);
        return ResponseEntity.ok(student);
    }

    @PostMapping("/uid={studentId}/toStudyGroup/{studyGroupId}")
    @PreAuthorize("hasRole('admin')")
    public ResponseEntity<StudentDto> addStudentToStudyGroup(@PathVariable("studentId") String userID, @PathVariable("studyGroupId") Long studyGroupId) {
        StudentDto student = studentService.addStudentToStudyGroup(studyGroupId, userID);
        return ResponseEntity.ok(student);
    }

    @DeleteMapping("/id={id}")
    @PreAuthorize("hasRole('admin')")
    public ResponseEntity<String> deleteStudentId(@PathVariable("id") Long id) {
        boolean isDeleted = studentService.deleteStudentID(id);
        if (isDeleted) {
            return ResponseEntity.ok("Student deleted successfully");
        } else {
            return ResponseEntity.status(404).body("Failed to delete student");
        }
    }

    @DeleteMapping("/uid={id}")
    @PreAuthorize("hasRole('admin')")
    public ResponseEntity<String> deleteStudentUID(@PathVariable("id") String userID) {
        boolean isDeleted = studentService.deleteStudentUID(userID);
        if (isDeleted) {
            return ResponseEntity.ok("Student deleted successfully");
        } else {
            return ResponseEntity.status(404).body("Failed to delete student");
        }
    }

    @PutMapping("/id={id}")
    @PreAuthorize("hasRole('admin')")
    public ResponseEntity<StudentDto> updateStudentID(@PathVariable("id") Long id, @RequestBody StudentUpdateDto studentDto) {
        StudentDto updatedStudent = studentService.updateStudentID(id, studentDto);
        if (updatedStudent == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(updatedStudent);
    }

    @PutMapping("/uid={id}")
    @PreAuthorize("hasRole('admin')")
    public ResponseEntity<StudentDto> updateStudentUID(@PathVariable("id") String userID, @RequestBody StudentUpdateDto studentDto) {
        StudentDto updatedStudent = studentService.updateStudentUID(userID, studentDto);
        if (updatedStudent == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(updatedStudent);
    }
}
