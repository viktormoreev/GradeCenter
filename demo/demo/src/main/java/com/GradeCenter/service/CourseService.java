package com.GradeCenter.service;

import com.GradeCenter.dtos.SubjectDto;
import com.GradeCenter.entity.Course;

import java.util.List;

public interface CourseService {
    public List<SubjectDto> fetchSubjectsList();

    Course saveSubject(Course course);
}
