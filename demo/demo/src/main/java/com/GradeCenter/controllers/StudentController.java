package com.GradeCenter.controllers;

import com.GradeCenter.dtos.StudentDto;
import com.GradeCenter.dtos.UserIDRequest;
import com.GradeCenter.service.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

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

    @GetMapping("/me")
    @PreAuthorize("hasRole('student')")
    public ResponseEntity<StudentDto> getStudentByUserId() {
        Jwt jwt = (Jwt) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String userId = jwt.getClaimAsString("sub");

        Optional<StudentDto> student = studentService.getStudentByUId(userId);
        return student.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('admin')")
    public ResponseEntity<StudentDto> getStudentById(@PathVariable("id") Long id) {
        Optional<StudentDto> student = studentService.getStudentById(id);
        return student.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('admin')")
    public ResponseEntity<StudentDto> getStudentByUId(@PathVariable("id") String userID) {
        Optional<StudentDto> student = studentService.getStudentByUId(userID);
        return student.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    @PreAuthorize("hasRole('admin')")
    public ResponseEntity<StudentDto> addStudent(@RequestBody UserIDRequest userIDRequest) {
        StudentDto student = studentService.addStudent(userIDRequest);
        return ResponseEntity.ok(student);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('admin')")
    public ResponseEntity<String> deleteStudentUid(@PathVariable("id") String userID) {
        ResponseEntity<String> response = studentService.deleteStudentUID(userID);
        if (response.getStatusCode().is2xxSuccessful()) {
            return ResponseEntity.ok("Student deleted successfully");
        } else {
            return ResponseEntity.status(response.getStatusCode()).body("Failed to delete student");
        }
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('admin')")
    public ResponseEntity<String> deleteStudentId(@PathVariable("id") Long id) {
        ResponseEntity<String> response = studentService.deleteStudentID(id);
        if (response.getStatusCode().is2xxSuccessful()) {
            return ResponseEntity.ok("Student deleted successfully");
        } else {
            return ResponseEntity.status(response.getStatusCode()).body("Failed to delete student");
        }
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('admin')")
    public ResponseEntity<StudentDto> updateStudent(@PathVariable("id") Long id, @RequestBody StudentDto studentDto) {
        Optional<StudentDto> response = studentService.updateStudentID(id, studentDto);
        if (response.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(studentDto);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('admin')")
    public ResponseEntity<StudentDto> updateStudent(@PathVariable("id") String userID, @RequestBody StudentDto studentDto) {
        Optional<StudentDto> response = studentService.updateStudentUID(userID, studentDto);
        if (response.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(studentDto);
    }


}
