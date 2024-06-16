package com.GradeCenter.dtos;

import com.GradeCenter.enums.Weekday;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class WeeklyScheduleDto {
    private Weekday weekday;
    private String courseName;
    private String studyGroupName;
    private SchoolHourDto schoolHour;
}
