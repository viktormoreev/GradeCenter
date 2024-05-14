package com.GradeCenter.dtos;

import com.GradeCenter.entity.Course;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CourseDto {
    private String name;

    //private List<AbsenceDto> absences;//Waiting for AbsenceDto functionality
    //private List<GradeDto> grades;//Waiting for GradeDto functionality

}
