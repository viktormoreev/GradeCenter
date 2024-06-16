package com.GradeCenter.dtos;

import com.GradeCenter.enums.Weekday;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class CreateWeeklyScheduleDto {
    private Weekday weekday;
    private Long courseId;
    private Long studyGroupId;
    private Long startHourId;
}
