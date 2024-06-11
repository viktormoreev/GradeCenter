package com.GradeCenter.service;

import com.GradeCenter.dtos.TeacherDto;
import com.GradeCenter.dtos.TeacherUpdateDto;
import com.GradeCenter.dtos.UserIDRequest;

import java.util.List;

public interface TeacherService {
    List<TeacherDto> getAllTeachers();

    TeacherDto addTeacher(UserIDRequest userIDRequest);

    TeacherDto getTeacherById(Long id);

    TeacherDto getTeacherByUId(String uid);

    boolean deleteTeacherUID(String userID);

    boolean deleteTeacherID(Long ID);

    TeacherDto updateTeacherID(Long id, TeacherUpdateDto teacherUpdateDto);

    TeacherDto updateTeacherUID(String userID, TeacherUpdateDto teacherUpdateDto);

    List<TeacherDto> getTeachersBySchoolId(Long schoolId);
}
