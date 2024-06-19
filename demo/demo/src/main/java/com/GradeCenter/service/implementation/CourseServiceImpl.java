package com.GradeCenter.service.implementation;

import com.GradeCenter.dtos.*;
import com.GradeCenter.entity.Course;
import com.GradeCenter.entity.CourseType;
import com.GradeCenter.entity.Student;
import com.GradeCenter.entity.StudyGroup;
import com.GradeCenter.mapper.EntityMapper;
import com.GradeCenter.repository.CourseRepository;
import com.GradeCenter.repository.CourseTypeRepository;
import com.GradeCenter.repository.StudentRepository;
import com.GradeCenter.repository.StudyGroupRepository;
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
    @Autowired
    private CourseTypeRepository courseTypeRepository;
    @Autowired
    private StudyGroupRepository studyGroupRepository;
    @Autowired
    private StudentRepository studentRepository;

    @Override
    public List<CourseDto> fetchCourseList() {
        return entityMapper.mapToCourseListDto(courseRepository.findAll());
    }

    @Override
    public CourseDto saveCourse(CreateCourseDto course) {
        Optional<CourseType> courseType = courseTypeRepository.findById(course.getCourseTypeId());
        if (courseType.isPresent()) {
            Course newCourse = Course.builder().courseType(courseType.get()).build();
            newCourse.setName(course.getName());
            return entityMapper.mapToCourseDto(courseRepository.save(newCourse));
        } else throw new EntityNotFoundException("Course Type is not found");
    }

    @Override
    public CourseDto fetchCourseById(Long courseId) {
        Optional<Course> course = courseRepository.findById(courseId);
        if (course.isPresent()) {
            return entityMapper.mapToCourseDto(course.get());
        } else throw new EntityNotFoundException("Course is not found");
    }

    @Override
    public CourseDto updateCourseById(Long courseId, CreateCourseDto courseDto) {
        Optional<Course> course = courseRepository.findById(courseId);
        if (course.isPresent()) {
            Optional<CourseType> courseType = courseTypeRepository.findById(courseDto.getCourseTypeId());
            if (courseType.isPresent()) {
                course.get().setCourseType(courseType.get());
                course.get().setName(courseDto.getName());
                return entityMapper.mapToCourseDto(courseRepository.save(course.get()));
            } else throw new EntityNotFoundException("Course Type is not found");
        } else throw new EntityNotFoundException("Course is not found");
    }

    @Override
    public boolean deleteCourseById(Long courseId) {
        courseRepository.deleteById(courseId);
        return true;
    }

    @Override
    public List<StudentCourseDto> fetchCourseByStudentId(Long studentId) {
        Optional<Student> student = studentRepository.findById(studentId);
        if (student.isPresent()) {
            if (student.get().getClasses() == null) {
                return null;
            }
            Optional<StudyGroup> studyGroup = studyGroupRepository.findById(student.get().getClasses().getId());
            if (studyGroup.isPresent()) {
                List<Course> courses = studyGroup.get().getCourses();
                return entityMapper.mapToStudentCourseListDto(courses, studentId);
            } else throw new EntityNotFoundException("Study group is not found");
        } else throw new EntityNotFoundException("Student is not found");
    }

    @Override
    public List<CourseDto> fetchCourseByStudyGroupId(Long studyGroupId) {
        Optional<StudyGroup> studyGroup = studyGroupRepository.findById(studyGroupId);
        if (studyGroup.isPresent()) {
            List<Course> courses = studyGroup.get().getCourses();
            return entityMapper.mapToCourseListDto(courses);
        } else throw new EntityNotFoundException("Study group is not found");
    }

    @Override
    public CourseTypeDto addCourseType(CourseTypeDto courseTypeDto) {
        CourseType courseType = CourseType.builder().name(courseTypeDto.getName()).build();
        courseTypeRepository.save(courseType);
        return courseTypeDto;
    }

    @Override
    public void deleteCourseTypeById(Long courseTypeId) {
        courseTypeRepository.deleteById(courseTypeId);
    }

    @Override
    public CourseTypeDto updateCourseTypeById(Long courseTypeId, CourseTypeDto courseTypeDto) {
        Optional<CourseType> optionalCourseType = courseTypeRepository.findById(courseTypeId);
        if (optionalCourseType.isPresent()) {
            CourseType courseType = optionalCourseType.get();
            courseType.setName(courseTypeDto.getName());
            return entityMapper.mapToCourseTypeDto(courseTypeRepository.save(courseType));
        } else throw new EntityNotFoundException("Course Type is not found");
    }

    @Override
    public List<CourseTypeDto> fetchCourseTypeList() {
        return entityMapper.mapToCourseTypeListDto(courseTypeRepository.findAll());
    }


}
