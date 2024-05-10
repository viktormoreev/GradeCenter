package com.GradeCenter.service.implementation;

import com.GradeCenter.entity.StudyGroup;
import com.GradeCenter.mapper.EntityMapper;
import com.GradeCenter.repository.StudyGroupRepository;
import com.GradeCenter.service.StudyGroupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class StudyGroupServiceImpl implements StudyGroupService {
    @Autowired
    private EntityMapper entityMapper;
    @Autowired
    private StudyGroupRepository studyGroupRepository;

    @Override
    public StudyGroup saveStudyGroup(StudyGroup studyGroup) {
        return studyGroupRepository.save(studyGroup);
    }
}
