package com.GradeCenter.service;

import com.GradeCenter.dtos.SchoolDto;
import com.GradeCenter.entity.School;

import java.util.List;

public interface SchoolService {

    List<SchoolDto> fetchSchoolList();

    SchoolDto saveSchool(School school);

    void deleteSchoolById(Long schoolId);

    SchoolDto fetchSchoolById(Long schoolId);

    SchoolDto updateSchoolById(Long schoolId);
}
