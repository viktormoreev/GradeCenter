package com.GradeCenter.dtos;

import com.GradeCenter.entity.Course;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CourseDto {
    private String name;
    private List<AbsenceDto> absences;
    private List<GradeDto> grades;

}
