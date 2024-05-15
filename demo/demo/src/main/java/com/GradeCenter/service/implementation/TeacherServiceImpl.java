package com.GradeCenter.service.implementation;

import com.GradeCenter.dtos.TeacherDto;
import com.GradeCenter.entity.Teacher;
import com.GradeCenter.mapper.EntityMapper;
import com.GradeCenter.repository.TeacherRepository;
import com.GradeCenter.service.TeacherService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TeacherServiceImpl implements TeacherService {

    @Autowired
    private EntityMapper entityMapper;

    @Autowired
    private TeacherRepository teacherRepository;

    @Override
    public List<TeacherDto> fetchTeacherList() {
        return entityMapper.mapToTeacherListDto(teacherRepository.findAll());
    }

    @Override
    public TeacherDto saveTeacher(Teacher teacher) {
        return entityMapper.mapToTeacherDto(teacherRepository.save(teacher));
    }

    @Override
    public void deleteTeacherById(Long teacherId) {
        teacherRepository.deleteById(teacherId);
    }

    @Override
    public TeacherDto fetchTeacherById(Long teacherId) {
        Optional<Teacher> teacher = teacherRepository.findById(teacherId);
        if (teacher.isPresent()){
            return entityMapper.mapToTeacherDto(teacher.get());
        }else {
            throw new EntityNotFoundException("Teacher is not found!");
        }
    }

    @Override
    public TeacherDto updateTeacherById(Long teacherId) {
        Optional<Teacher> teacher = teacherRepository.findById(teacherId);
        if (teacher.isPresent()){
            return entityMapper.mapToTeacherDto(teacherRepository.save(teacher.get()));
        }else {
            throw new EntityNotFoundException("Teacher is not found!");
        }
    }
}
