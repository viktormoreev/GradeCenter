package com.GradeCenter.mapper;

import com.GradeCenter.dtos.SubjectDto;
import com.GradeCenter.entity.Course;
import org.springframework.stereotype.Service;
import org.modelmapper.ModelMapper;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class EntityMapper {
    private final ModelMapper modelMapper = new ModelMapper();

    public SubjectDto mapToSubjectDto (Course course){
        SubjectDto subjectDto=new SubjectDto();
        modelMapper.map(course,subjectDto);
        return subjectDto;
    }

    public List<SubjectDto> mapToSubjectListDto(List<Course> courseList){
        return courseList.stream().map(this::mapToSubjectDto).collect(Collectors.toList());
    }
}
