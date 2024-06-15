package com.GradeCenter.service;

import com.GradeCenter.dtos.CourseDto;
import com.GradeCenter.dtos.StudyGroupDto;
import com.GradeCenter.entity.StudyGroup;

import java.util.List;

public interface StudyGroupService {
    StudyGroupDto saveStudyGroup(StudyGroup studyGroup);

    List<StudyGroupDto> fetchStudyGroup();

    StudyGroupDto fetchStudyGroupById(Long courseId);

    StudyGroupDto updateStudyGroupById(Long courseId);

    void deleteStudyGroupById(Long studyGroupId);
}
