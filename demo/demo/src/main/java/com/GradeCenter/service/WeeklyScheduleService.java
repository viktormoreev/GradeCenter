package com.GradeCenter.service;

import com.GradeCenter.dtos.CreateWeeklyScheduleDto;
import com.GradeCenter.dtos.WeeklyScheduleDto;

import java.util.List;

public interface WeeklyScheduleService {
    WeeklyScheduleDto saveWeeklySchedule(CreateWeeklyScheduleDto createWeeklyScheduleDto);

    List<WeeklyScheduleDto> fetchAllWeeklySchedule();

    List<WeeklyScheduleDto> fetchWeeklyScheduleByCourseId(Long id);

    List<WeeklyScheduleDto> fetchWeeklyScheduleByStudyGroupId(Long id);

    WeeklyScheduleDto updateWeeklyScheduleById(Long id, CreateWeeklyScheduleDto createWeeklyScheduleDto);

    void deleteWeeklyScheduleById(Long id);
}
