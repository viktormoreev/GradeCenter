package com.GradeCenter.service.implementation;

import com.GradeCenter.dtos.TeacherDto;
import com.GradeCenter.dtos.TeacherUpdateDto;
import com.GradeCenter.dtos.UserIDRequest;
import com.GradeCenter.entity.Course;
import com.GradeCenter.entity.Qualification;
import com.GradeCenter.entity.School;
import com.GradeCenter.entity.Teacher;
import com.GradeCenter.mapper.EntityMapper;
import com.GradeCenter.repository.CourseRepository;
import com.GradeCenter.repository.QualificationRepository;
import com.GradeCenter.repository.SchoolRepository;
import com.GradeCenter.repository.TeacherRepository;
import com.GradeCenter.service.TeacherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class TeacherServiceImpl implements TeacherService {

    @Autowired
    private TeacherRepository teacherRepository;

    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    private QualificationRepository qualificationRepository;

    @Autowired
    private SchoolRepository schoolRepository;

    @Autowired
    private EntityMapper entityMapper;

    @Override
    public List<TeacherDto> getAllTeachers() {
        return entityMapper.mapToTeacherListDto(teacherRepository.findAll());
    }

    @Override
    public TeacherDto getTeacherByUId(String uid) {
        return teacherRepository.findByUserID(uid)
                .map(entityMapper::mapToTeacherDto)
                .orElse(null);
    }

    @Override
    public boolean deleteTeacherUID(String userID) {
        Optional<Teacher> teacher = teacherRepository.findByUserID(userID);
        if (teacher.isPresent()) {
            teacherRepository.delete(teacher.get());
            return true;
        }
        return false;
    }

    @Override
    public boolean deleteTeacherID(Long ID) {
        if (teacherRepository.existsById(ID)) {
            teacherRepository.deleteById(ID);
            return true;
        }
        return false;
    }

    @Override
    public TeacherDto updateTeacherID(Long id, TeacherUpdateDto teacherUpdateDto) {
        Optional<Teacher> existingTeacherOpt = teacherRepository.findById(id);
        if (existingTeacherOpt.isPresent()) {
            Teacher existingTeacher = existingTeacherOpt.get();
            updateTeacherFromDto(existingTeacher, teacherUpdateDto);
            teacherRepository.save(existingTeacher);
            return entityMapper.mapToTeacherDto(existingTeacher);
        }
        return null;
    }

    @Override
    public TeacherDto updateTeacherUID(String userID, TeacherUpdateDto teacherUpdateDto) {
        Optional<Teacher> existingTeacherOpt = teacherRepository.findByUserID(userID);
        if (existingTeacherOpt.isPresent()) {
            Teacher existingTeacher = existingTeacherOpt.get();
            updateTeacherFromDto(existingTeacher, teacherUpdateDto);
            teacherRepository.save(existingTeacher);
            return entityMapper.mapToTeacherDto(existingTeacher);
        }
        return null;
    }

    @Override
    public TeacherDto addTeacher(UserIDRequest userIDRequest) {
        Teacher teacher = Teacher.builder()
                .userID(userIDRequest.getUserID())
                .build();
        teacherRepository.save(teacher);
        return entityMapper.mapToTeacherDto(teacher);
    }

    @Override
    public TeacherDto getTeacherById(Long id) {
        return teacherRepository.findById(id)
                .map(entityMapper::mapToTeacherDto)
                .orElse(null);
    }

    private void updateTeacherFromDto(Teacher teacher, TeacherUpdateDto teacherUpdateDto) {
        if (teacherUpdateDto.getSchoolId() != null) {
            School school = schoolRepository.findById(teacherUpdateDto.getSchoolId())
                    .orElseThrow(() -> new IllegalArgumentException("Invalid school ID"));
            teacher.setSchool(school);
        } else {
            teacher.setSchool(null);
        }

        if (teacherUpdateDto.getCourseIds() != null) {
            List<Course> courses = teacherUpdateDto.getCourseIds().stream()
                    .map(courseId -> courseRepository.findById(courseId)
                            .orElseThrow(() -> new IllegalArgumentException("Invalid course ID")))
                    .collect(Collectors.toList());
            teacher.setCourses(courses);
        } else {
            teacher.setCourses(null);
        }

        if (teacherUpdateDto.getQualificationsIds() != null) {
            List<Qualification> qualifications = teacherUpdateDto.getQualificationsIds().stream()
                    .map(qualificationId -> qualificationRepository.findById(qualificationId)
                            .orElseThrow(() -> new IllegalArgumentException("Invalid qualification ID")))
                    .collect(Collectors.toList());
            teacher.setQualifications(qualifications);
        } else {
            teacher.setQualifications(null);
        }
    }
}
