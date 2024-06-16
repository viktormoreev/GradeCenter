package com.GradeCenter.service;

import com.GradeCenter.dtos.*;

import java.util.List;

public interface StudentService {
    List<StudentDto> getAllStudents();

    StudentDto addStudent(UserIDRequest userIDRequest);

    StudentDto getStudentById(Long id);

    StudentDto getStudentByUId(String uid);

    boolean deleteStudentUID(String userID);

    boolean deleteStudentID(Long ID);

    StudentDto updateStudentID(Long id, StudentUpdateDto studentDto);

    StudentDto updateStudentUID(String userID, StudentUpdateDto studentDto);

    List<StudentFullReturnDto> getAllStudentsFull();

    StudentFullReturnDto getFullStudentById(Long id);

    StudentDto addStudentToStudyGroup(CreateStudentDto studentDto, String userID);
}
