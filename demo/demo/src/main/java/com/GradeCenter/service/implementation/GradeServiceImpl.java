package com.GradeCenter.service.implementation;

import com.GradeCenter.dtos.GradeDto;
import com.GradeCenter.dtos.GradeStudentViewDto;
import com.GradeCenter.entity.Course;
import com.GradeCenter.entity.Grade;
import com.GradeCenter.entity.Student;
import com.GradeCenter.exceptions.CourseNotFoundException;
import com.GradeCenter.exceptions.GradeNotFoundException;
import com.GradeCenter.exceptions.StudentNotFoundException;
import com.GradeCenter.mapper.EntityMapper;
import com.GradeCenter.repository.CourseRepository;
import com.GradeCenter.repository.GradeRepository;
import com.GradeCenter.repository.StudentRepository;
import com.GradeCenter.service.GradeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class GradeServiceImpl implements GradeService {

    @Autowired
    private GradeRepository gradeRepository;

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    private EntityMapper entityMapper;

    @Override
    public List<GradeDto> getAllGrades() {
        return gradeRepository.findAll()
                .stream()
                .map(entityMapper::mapToGradeDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<GradeDto> getGradesByStudentId(long studentId) throws StudentNotFoundException {
        return gradeRepository.findByStudentId(studentId)
                .orElseThrow(() -> new StudentNotFoundException("Student not found"))
                .stream()
                .map(entityMapper::mapToGradeDto)
                .collect(Collectors.toList());
    }

    @Override
    public GradeDto createGrade(GradeDto gradeDto) throws StudentNotFoundException, CourseNotFoundException {
        Student student = studentRepository.findById(gradeDto.getStudentId())
                .orElseThrow(() -> new StudentNotFoundException("Student not found"));

        Course course = courseRepository.findById(gradeDto.getCourseId())
                .orElseThrow(() -> new CourseNotFoundException("Course not found"));

        Grade grade = entityMapper.mapToGradeEntity(gradeDto, student, course);

        Grade savedGrade = gradeRepository.save(grade);

        return entityMapper.mapToGradeDto(savedGrade);
    }

    @Override
    public GradeDto updateGrade(Long id, GradeDto gradeDto) throws GradeNotFoundException, StudentNotFoundException, CourseNotFoundException {
        Grade existingGrade = gradeRepository.findById(id)
                .orElseThrow(() -> new GradeNotFoundException("Grade not found"));

        Student student = studentRepository.findById(gradeDto.getStudentId())
                .orElseThrow(() -> new StudentNotFoundException("Student not found"));

        Course course = courseRepository.findById(gradeDto.getCourseId())
                . orElseThrow(() -> new CourseNotFoundException("Course not found"));

        existingGrade.setStudent(student);
        existingGrade.setCourse(course);
        existingGrade.setGrade(gradeDto.getGrade());

        Grade updatedGrade = gradeRepository.save(existingGrade);

        return entityMapper.mapToGradeDto(updatedGrade);
    }

    @Override
    public boolean deleteGradeById(Long id) {
        if (gradeRepository.existsById(id)) {
            gradeRepository.deleteById(id);
            return true;
        } else {
            return false;
        }
    }

    @Override
    public List<GradeStudentViewDto> getPersonalStudentGrades(String userId) throws StudentNotFoundException {
        Student student = studentRepository.findByUserID(userId)
                .orElseThrow(() -> new StudentNotFoundException("Student not found"));

        List<Grade> grades = gradeRepository.findByStudentId(student.getId())
                .orElseThrow(() -> new StudentNotFoundException("No grades found for student"));

        return grades.stream()
                .map(grade -> new GradeStudentViewDto(
                        grade.getCourse().getName(),
                        grade.getGrade()))
                .collect(Collectors.toList());
    }
}