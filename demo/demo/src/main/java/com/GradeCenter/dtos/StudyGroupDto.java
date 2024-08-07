package com.GradeCenter.dtos;

import com.GradeCenter.entity.Course;
import com.GradeCenter.entity.School;
import com.GradeCenter.entity.Student;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class StudyGroupDto {
    private Long id;
    private String name;
    private Long schoolId;
    private String schoolName;
    private List<StudentFullReturnDto> students;
    private List<CourseDto> courses;
}
