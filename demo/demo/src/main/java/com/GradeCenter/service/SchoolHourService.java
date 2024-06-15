package com.GradeCenter.service;

import com.GradeCenter.entity.SchoolHour;
import com.GradeCenter.dtos.SchoolHourCreateRequest;

import java.util.List;

public interface SchoolHourService {
    List<SchoolHour> findAll();
    SchoolHour findById(Long id);
    SchoolHour save(SchoolHourCreateRequest schoolHour);
    SchoolHour update(Long id, SchoolHourCreateRequest schoolHourDetails);
    boolean deleteById(Long id);
}