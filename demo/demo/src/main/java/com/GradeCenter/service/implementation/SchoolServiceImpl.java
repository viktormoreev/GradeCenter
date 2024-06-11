package com.GradeCenter.service.implementation;

import com.GradeCenter.dtos.SchoolCreateRequest;
import com.GradeCenter.dtos.SchoolDto;
import com.GradeCenter.entity.Director;
import com.GradeCenter.entity.School;
import com.GradeCenter.entity.Teacher;
import com.GradeCenter.mapper.EntityMapper;
import com.GradeCenter.repository.DirectorRepository;
import com.GradeCenter.repository.SchoolRepository;
import com.GradeCenter.repository.TeacherRepository;
import com.GradeCenter.service.SchoolService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class SchoolServiceImpl implements SchoolService {

    @Autowired
    private SchoolRepository schoolRepository;

    @Autowired
    private DirectorRepository directorRepository;

    @Autowired
    private TeacherRepository teacherRepository;

    @Autowired
    private EntityMapper entityMapper;

    @Override
    public List<SchoolDto> getAllSchools() {
        return entityMapper.mapToSchoolListDto(schoolRepository.findAll());
    }

    @Override
    public SchoolDto addSchool(SchoolCreateRequest schoolCreateRequest) {
        School school = School.builder()
                .name(schoolCreateRequest.getName())
                .address(schoolCreateRequest.getAddress())
                .build();


        schoolRepository.save(school);
        return entityMapper.mapToSchoolDto(school);
    }

    @Override
    public SchoolDto getSchoolById(Long id) {
        return schoolRepository.findById(id)
                .map(entityMapper::mapToSchoolDto)
                .orElse(null);
    }

    @Override
    public boolean deleteSchool(Long id) {
        if (schoolRepository.existsById(id)) {
            schoolRepository.deleteById(id);
            return true;
        }
        return false;
    }

    @Override
    public SchoolDto updateSchool(Long id, SchoolDto schoolUpdateDto) {
        Optional<School> existingSchoolOpt = schoolRepository.findById(id);
        if (existingSchoolOpt.isPresent()) {
            School existingSchool = existingSchoolOpt.get();
            updateSchoolFromDto(existingSchool, schoolUpdateDto);
            schoolRepository.save(existingSchool);
            return entityMapper.mapToSchoolDto(existingSchool);
        }
        return null;
    }



    private void updateSchoolFromDto(School school, SchoolDto schoolUpdateDto) {
        school.setName(schoolUpdateDto.getName());
        school.setAddress(schoolUpdateDto.getAddress());

        if (schoolUpdateDto.getDirectorId() != null) {
            Director director = directorRepository.findById(schoolUpdateDto.getDirectorId())
                    .orElseThrow(() -> new IllegalArgumentException("Invalid director ID"));
            school.setDirector(director);
        } else {
            school.setDirector(null);
        }

        if (schoolUpdateDto.getTeachersId() != null) {
            List<Teacher> teachers = schoolUpdateDto.getTeachersId().stream()
                    .map(teacherId -> teacherRepository.findById(teacherId)
                            .orElseThrow(() -> new IllegalArgumentException("Invalid teacher ID")))
                    .collect(Collectors.toList());
            school.setTeachers(teachers);
        } else {
            school.setTeachers(null);
        }
    }
}
