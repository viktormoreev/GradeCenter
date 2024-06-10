package com.GradeCenter.service;

import com.GradeCenter.dtos.StudentDto;
import com.GradeCenter.dtos.UserIDRequest;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Optional;

public interface StudentService {
    List<StudentDto> getAllStudents();

    StudentDto addStudent(UserIDRequest userIDRequest);

    Optional<StudentDto> getStudentById(Long id);

    Optional<StudentDto> getStudentByUId(String l);

    ResponseEntity<String> deleteStudentUID(String userID);

    ResponseEntity<String> deleteStudentID(Long ID);

    Optional<StudentDto> updateStudentID(Long id, StudentDto studentDto);

    Optional<StudentDto> updateStudentUID(String userID, StudentDto studentDto);
}
