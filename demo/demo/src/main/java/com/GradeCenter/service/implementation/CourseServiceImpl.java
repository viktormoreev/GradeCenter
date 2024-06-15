package com.GradeCenter.service.implementation;

import com.GradeCenter.dtos.CourseDto;
import com.GradeCenter.dtos.StudentCourseDto;
import com.GradeCenter.dtos.StudentDto;
import com.GradeCenter.dtos.StudyGroupDto;
import com.GradeCenter.entity.Course;
import com.GradeCenter.entity.Student;
import com.GradeCenter.entity.StudyGroup;
import com.GradeCenter.mapper.EntityMapper;
import com.GradeCenter.repository.CourseRepository;
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
    private StudyGroupRepository studyGroupRepository;
    @Autowired
    private StudentRepository studentRepository;

    @Override
    public List<CourseDto> fetchCourseList() {
        return entityMapper.mapToCourseListDto(courseRepository.findAll());
    }

    @Override
    public CourseDto saveCourse(Course course) {
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

    @Override
    public List<StudentCourseDto> fetchCourseByStudentId(Long studentId) {
        Optional<Student> student = studentRepository.findById(studentId);
        if(student.isPresent()){
            Optional<StudyGroup> studyGroup = studyGroupRepository.findById(student.get().getClasses().getId());
            if(studyGroup.isPresent()){
                List<Course> courses = studyGroup.get().getCourses();
                return entityMapper.mapToStudentCourseListDto(courses,studentId);
            }
            else throw new EntityNotFoundException("Study group is not found");
        }
        else throw new EntityNotFoundException("Student is not found");
    }

    @Override
    public List<CourseDto> fetchCourseByStudyGroupId(Long studyGroupId) {
        Optional<StudyGroup> studyGroup = studyGroupRepository.findById(studyGroupId);
        if(studyGroup.isPresent()){
            List<Course> courses = studyGroup.get().getCourses();
            return entityMapper.mapToCourseListDto(courses);
        }
        else throw new EntityNotFoundException("Study group is not found");
    }


}
