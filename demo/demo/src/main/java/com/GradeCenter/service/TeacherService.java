package com.GradeCenter.service;

import com.GradeCenter.dtos.FetchTeacherDto;
import com.GradeCenter.dtos.TeacherDto;
import com.GradeCenter.dtos.TeacherUpdateDto;
import com.GradeCenter.dtos.UserIDRequest;

import java.util.List;

public interface TeacherService {
    List<FetchTeacherDto> getAllTeachers();

    TeacherDto addTeacher(UserIDRequest userIDRequest);

    FetchTeacherDto getTeacherById(Long id);

    FetchTeacherDto getTeacherByUId(String uid);

    boolean deleteTeacherUID(String userID);

    boolean deleteTeacherID(Long ID);

    TeacherDto updateTeacherID(Long id, TeacherUpdateDto teacherUpdateDto);

    TeacherDto updateTeacherUID(String userID, TeacherUpdateDto teacherUpdateDto);


}
