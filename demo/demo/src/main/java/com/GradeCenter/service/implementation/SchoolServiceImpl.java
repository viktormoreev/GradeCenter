package com.GradeCenter.service.implementation;

import com.GradeCenter.dtos.SchoolDto;
import com.GradeCenter.dtos.TeacherDto;
import com.GradeCenter.entity.School;
import com.GradeCenter.mapper.EntityMapper;
import com.GradeCenter.repository.SchoolRepository;
import com.GradeCenter.repository.TeacherRepository;
import com.GradeCenter.service.SchoolService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class SchoolServiceImpl implements SchoolService {

    @Autowired
    private EntityMapper entityMapper;
    @Autowired
    private SchoolRepository schoolRepository;

    @Override
    public List<SchoolDto> fetchSchoolList() {
        return entityMapper.mapToSchoolListDto(schoolRepository.findAll());
    }

    @Override
    public SchoolDto saveSchool(School school) {
        return entityMapper.mapToSchoolDto(schoolRepository.save(school));
    }

    @Override
    public void deleteSchoolById(Long schoolId) {
        schoolRepository.deleteById(schoolId);
    }

    @Override
    public SchoolDto fetchSchoolById(Long schoolId) {
        Optional<School> school = schoolRepository.findById(schoolId);
        if (school.isPresent()){
            return entityMapper.mapToSchoolDto(school.get());
        }else {
            throw new EntityNotFoundException("School is not found!");
        }
    }

    @Override
    public SchoolDto updateSchoolById(Long schoolId) {
        Optional<School> school = schoolRepository.findById(schoolId);
        if (school.isPresent()){
            return entityMapper.mapToSchoolDto(schoolRepository.save(school.get()));
        }else {
            throw new EntityNotFoundException("School is not found!");
        }
    }
}
