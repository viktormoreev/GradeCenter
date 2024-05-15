package com.GradeCenter.mapper;

import com.GradeCenter.dtos.CourseDto;
import com.GradeCenter.dtos.DirectorDto;
import com.GradeCenter.dtos.SchoolDto;
import com.GradeCenter.dtos.TeacherDto;
import com.GradeCenter.entity.Course;
import com.GradeCenter.entity.Director;
import com.GradeCenter.entity.School;
import com.GradeCenter.entity.Teacher;
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

    public TeacherDto mapToTeacherDto(Teacher teacher){
        TeacherDto teacherDto = new TeacherDto();
        modelMapper.map(teacher, teacherDto);
        return teacherDto;
    }

    public List<TeacherDto> mapToTeacherListDto(List<Teacher> teacherList){
        return teacherList.stream().map(this::mapToTeacherDto).collect(Collectors.toList());
    }


    public DirectorDto mapToDirectorDto(Director director){
        DirectorDto directorDto = new DirectorDto();
        modelMapper.map(director, directorDto);
        return directorDto;
    }

    public List<DirectorDto> mapToDirectorListDto(List<Director> directorList){
        return directorList.stream().map(this::mapToDirectorDto).collect(Collectors.toList());
    }

    public SchoolDto mapToSchoolDto(School school){
        SchoolDto schoolDto = new SchoolDto();
        modelMapper.map(school, schoolDto);
        return schoolDto;
    }

    public List<SchoolDto> mapToSchoolListDto(List<School> schoolList){
        return schoolList.stream().map(this::mapToSchoolDto).collect(Collectors.toList());
    }

}
