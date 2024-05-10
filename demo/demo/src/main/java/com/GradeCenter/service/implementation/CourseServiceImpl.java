package com.GradeCenter.service.implementation;

import com.GradeCenter.dtos.SubjectDto;
import com.GradeCenter.entity.Course;
import com.GradeCenter.mapper.EntityMapper;
import com.GradeCenter.repository.CourseRepository;
import com.GradeCenter.service.CourseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CourseServiceImpl implements CourseService {

    @Autowired
    private EntityMapper entityMapper;
    @Autowired
    private CourseRepository subjectReposetory;

    @Override
    public List<SubjectDto> fetchSubjectsList() {
        return entityMapper.mapToSubjectListDto(subjectReposetory.findAll());
    }

    @Override
    public Course saveSubject(Course course) {
        return subjectReposetory.save(course);
    }


}
