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
    private Long id;
    private String name;
    private List<CourseDto> courses;
    private String schoolName;
    private Long schoolId;
    private List<QualificationDto> qualifications;
}
