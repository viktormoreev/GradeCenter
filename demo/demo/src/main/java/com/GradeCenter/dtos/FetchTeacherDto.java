package com.GradeCenter.dtos;

import com.GradeCenter.entity.Qualification;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class FetchTeacherDto {
    private String userID;
    private List<CourseDto> courses;
    private String schoolName;
    private List<QualificationDto> qualifications;
}
