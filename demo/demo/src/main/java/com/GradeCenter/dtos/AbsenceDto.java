package com.GradeCenter.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class AbsenceDto {
    private Long studentId;
    private Long absenceId;
    private Long courseId;
    private LocalDate date;
}
