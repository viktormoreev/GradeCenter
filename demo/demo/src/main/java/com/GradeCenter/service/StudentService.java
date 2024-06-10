package com.GradeCenter.service;

import com.GradeCenter.dtos.StudentDto;
import com.GradeCenter.dtos.UserIDRequest;

import java.util.List;
import java.util.Optional;

public interface StudentService {
    List<StudentDto> getAllStudents();

    Optional<StudentDto> getStudentByUId(String l);

    StudentDto addStudent(UserIDRequest userIDRequest);
}
