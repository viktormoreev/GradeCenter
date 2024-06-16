package com.GradeCenter.service;

import com.GradeCenter.dtos.WeeklyScheduleDto;

import java.util.List;

public interface WeeklyScheduleService {
    WeeklyScheduleDto saveWeeklySchedule(WeeklyScheduleDto weeklyScheduleDto);

    List<WeeklyScheduleDto> fetchAllWeeklySchedule();

    List<WeeklyScheduleDto> fetchWeeklyScheduleByCourseId(Long id);

    List<WeeklyScheduleDto> fetchWeeklyScheduleByStudyGroupId(Long id);

    WeeklyScheduleDto updateWeeklyScheduleById(Long id, WeeklyScheduleDto weeklyScheduleDto);

    void deleteWeeklyScheduleById(Long id);
}
