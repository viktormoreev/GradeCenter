package com.GradeCenter.service.implementation;

import com.GradeCenter.dtos.StudyGroupDto;
import com.GradeCenter.entity.StudyGroup;
import com.GradeCenter.mapper.EntityMapper;
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

    @Override
    public StudyGroupDto saveStudyGroup(StudyGroup studyGroup) {
        return entityMapper.mapToStudyGroupDto(studyGroupRepository.save(studyGroup));
    }

    @Override
    public List<StudyGroupDto> fetchStudyGroup() {
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
