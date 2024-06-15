package com.GradeCenter.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class StudentCourseDto {
    private String name;
    private List<AbsenceDto> absences;
    private List<SmallGradeDto> grades;

}
