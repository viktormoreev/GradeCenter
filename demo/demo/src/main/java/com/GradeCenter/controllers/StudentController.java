package com.GradeCenter.controllers;

import com.GradeCenter.service.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/users")
public class StudentController {

    @Autowired
    private StudentService studentService;



}
