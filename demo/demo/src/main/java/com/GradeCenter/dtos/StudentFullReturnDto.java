package com.GradeCenter.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class StudentFullReturnDto {

    private Long id;
    private String username;
    private String grade;
    private List<String> parent;
    private String school;
    private List<StudentCourseDto> courses;
    private Integer absences;

}
