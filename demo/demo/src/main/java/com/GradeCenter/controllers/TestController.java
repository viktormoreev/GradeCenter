package com.GradeCenter.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/test")
public class TestController {

    @GetMapping
    public ResponseEntity<String> defaultResponce() {
        return ResponseEntity.ok("This works");
    }

    @GetMapping("/student")
    @PreAuthorize("hasRole('student')")
    public ResponseEntity<String> studentResponce() {
        return ResponseEntity.ok("Hello Student");
    }

}