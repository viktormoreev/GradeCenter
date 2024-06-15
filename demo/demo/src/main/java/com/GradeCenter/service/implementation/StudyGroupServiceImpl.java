package com.GradeCenter.service.implementation;

import com.GradeCenter.dtos.StudyGroupCreateRequest;
import com.GradeCenter.dtos.StudyGroupDto;
import com.GradeCenter.entity.School;
import com.GradeCenter.entity.StudyGroup;
import com.GradeCenter.mapper.EntityMapper;
import com.GradeCenter.repository.SchoolRepository;
import com.GradeCenter.repository.StudyGroupRepository;
import com.GradeCenter.service.StudyGroupService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class StudyGroupServiceImpl implements StudyGroupService {
    @Autowired
    private EntityMapper entityMapper;
    @Autowired
    private StudyGroupRepository studyGroupRepository;

    @Autowired
    private SchoolRepository schoolRepository;

    @Override
    public StudyGroupDto saveStudyGroupInSchool(StudyGroupCreateRequest studyGroup) {
        Optional<School> optionalSchool = schoolRepository.findById(studyGroup.getSchoolId());
        if(optionalSchool.isPresent()){
            School school = optionalSchool.get();
            StudyGroup newStudyGroup= new StudyGroup();
            newStudyGroup.setName(studyGroup.getName());
            school.getStudyGroups().add(newStudyGroup);
            schoolRepository.save(school);
            return entityMapper.mapToStudyGroupDto(studyGroupRepository.save(newStudyGroup));

        }
        throw new EntityNotFoundException("School is not found");
    }

    @Override
    public List<StudyGroupDto> fetchStudyGroups() {
        return entityMapper.mapToStudyGroupDtoList(studyGroupRepository.findAll());
    }

    @Override
    public StudyGroupDto fetchStudyGroupById(Long studyGroupId) {
        Optional<StudyGroup> studyGroup = studyGroupRepository.findById(studyGroupId);
        if(studyGroup.isPresent()){
            return entityMapper.mapToStudyGroupDto(studyGroup.get());
        }
        else throw new EntityNotFoundException("Study group is not found");
    }

    @Override
    public StudyGroupDto updateStudyGroupById(Long studyGroupId) {
        Optional<StudyGroup> studyGroup = studyGroupRepository.findById(studyGroupId);
        if(studyGroup.isPresent()){
            return entityMapper.mapToStudyGroupDto(studyGroup.get());
        }
        else throw new EntityNotFoundException("Study group is not found");
    }

    @Override
    public void deleteStudyGroupById(Long studyGroupId) {
        studyGroupRepository.deleteById(studyGroupId);
    }
}
