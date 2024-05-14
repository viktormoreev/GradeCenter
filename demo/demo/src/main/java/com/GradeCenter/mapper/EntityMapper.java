package com.GradeCenter.mapper;

import com.GradeCenter.dtos.CourseDto;
import com.GradeCenter.entity.Course;
import org.springframework.stereotype.Service;
import org.modelmapper.ModelMapper;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class EntityMapper {
    private final ModelMapper modelMapper = new ModelMapper();

    public CourseDto mapToCourseDto(Course course){
        CourseDto courseDto =new CourseDto();
        modelMapper.map(course, courseDto);
        return courseDto;
    }

    public List<CourseDto> mapToCourseListDto(List<Course> courseList){
        return courseList.stream().map(this::mapToCourseDto).collect(Collectors.toList());
    }


}
