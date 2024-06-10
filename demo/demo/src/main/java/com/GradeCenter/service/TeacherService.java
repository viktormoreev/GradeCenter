package com.GradeCenter.service;

import com.GradeCenter.dtos.TeacherDto;
import com.GradeCenter.dtos.UserIDRequest;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Optional;

public interface TeacherService {
    List<TeacherDto> getAllTeachers();

    Optional<TeacherDto> getTeacherByUId(String userId);

    Optional<TeacherDto> getTeacherById(Long id);

    TeacherDto addTeacher(UserIDRequest userIDRequest);

    ResponseEntity<String> deleteTeacherID(Long id);

    ResponseEntity<String> deleteTeacherUID(String userID);

    Optional<TeacherDto> updateTeacherID(Long id, TeacherDto teacherDto);

    Optional<TeacherDto> updateTeacherUID(String userID, TeacherDto teacherDto);
}
