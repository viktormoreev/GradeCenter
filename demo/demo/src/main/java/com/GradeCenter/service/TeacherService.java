package com.GradeCenter.service;

import com.GradeCenter.dtos.TeacherDto;
import com.GradeCenter.entity.Teacher;

import java.util.List;

public interface TeacherService {
    List<TeacherDto> fetchTeacherList();
    TeacherDto saveTeacher(Teacher teacher);
    void deleteTeacherById(Long teacherId);
    TeacherDto fetchTeacherById(Long teacherId);

    TeacherDto updateTeacherById(Long teacherId);
}
