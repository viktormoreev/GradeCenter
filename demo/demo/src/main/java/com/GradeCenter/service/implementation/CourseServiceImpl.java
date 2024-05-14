package com.GradeCenter.service.implementation;

import com.GradeCenter.dtos.CourseDto;
import com.GradeCenter.entity.Course;
import com.GradeCenter.mapper.EntityMapper;
import com.GradeCenter.repository.CourseRepository;
import com.GradeCenter.service.CourseService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CourseServiceImpl implements CourseService {

    @Autowired
    private EntityMapper entityMapper;
    @Autowired
    private CourseRepository courseRepository;

    @Override
    public List<CourseDto> fetchCourseList() {
        return entityMapper.mapToCourseListDto(courseRepository.findAll());
    }

    @Override
    public CourseDto saveSubject(Course course) {
        return entityMapper.mapToCourseDto(courseRepository.save(course));
    }

    @Override
    public CourseDto fetchCourseById(Long courseId) {
        Optional<Course> course = courseRepository.findById(courseId);
        if(course.isPresent()){
            return entityMapper.mapToCourseDto(course.get());
        }
        else throw new EntityNotFoundException("Course is not found");

    }

    @Override
    public CourseDto updateCourseById(Long courseId) {
        Optional<Course> course = courseRepository.findById(courseId);
        if(course.isPresent()){
            return entityMapper.mapToCourseDto(courseRepository.save(course.get()));
        }
        else throw new EntityNotFoundException("Course is not found");
    }

    @Override
    public boolean deleteCourseById(Long courseId) {
        courseRepository.deleteById(courseId);
        return true;
    }


}
