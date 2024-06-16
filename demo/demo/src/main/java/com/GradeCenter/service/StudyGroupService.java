package com.GradeCenter.service;

import com.GradeCenter.dtos.StudyGroupCreateRequest;
import com.GradeCenter.dtos.StudyGroupDto;

import java.util.List;

public interface StudyGroupService {
    StudyGroupDto saveStudyGroupInSchool(StudyGroupCreateRequest studyGroup);

    List<StudyGroupDto> fetchStudyGroups();

    StudyGroupDto fetchStudyGroupById(Long courseId);

    StudyGroupDto updateStudyGroupById(Long courseId);

    void deleteStudyGroupById(Long studyGroupId);

    List<StudyGroupDto> fetchStudyGroupsBySchoolId(Long schoolId);
}
