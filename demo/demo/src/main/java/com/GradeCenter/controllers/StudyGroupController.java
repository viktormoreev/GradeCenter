package com.GradeCenter.controllers;

import com.GradeCenter.entity.StudyGroup;
import com.GradeCenter.service.StudyGroupService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/studyGroup")
public class StudyGroupController {

    @Autowired
    StudyGroupService studyGroupService;

    @PostMapping
    public StudyGroup addStudyGroup(@Valid @RequestBody StudyGroup studyGroup){ // needs to be added to teachers
        return studyGroupService.saveStudyGroup(studyGroup);
    }

}
