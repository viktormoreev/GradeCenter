package com.GradeCenter.service;

import com.GradeCenter.dtos.SchoolCreateRequest;
import com.GradeCenter.dtos.SchoolDto;

import java.util.List;

public interface SchoolService {
    List<SchoolDto> getAllSchools();

    SchoolDto addSchool(SchoolCreateRequest schoolCreateRequest);

    SchoolDto getSchoolById(Long id);

    boolean deleteSchool(Long id);

    SchoolDto updateSchool(Long id, SchoolDto schoolDto);

}