package com.GradeCenter.service;

import com.GradeCenter.dtos.SchoolCreateRequest;
import com.GradeCenter.dtos.SchoolDto;
import com.GradeCenter.dtos.SchoolNamesDto;
import com.GradeCenter.dtos.TeacherDto;

import java.util.List;

public interface SchoolService {
    List<SchoolNamesDto> getAllSchools();

    SchoolDto addSchool(SchoolCreateRequest schoolCreateRequest);

    SchoolDto getSchoolById(Long id);

    boolean deleteSchool(Long id);

    SchoolDto updateSchool(Long id, SchoolDto schoolDto);

    List<TeacherDto> getTeachersBySchoolId(Long schoolId);
}