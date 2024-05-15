package com.GradeCenter.controllers;

import com.GradeCenter.dtos.CourseDto;
import com.GradeCenter.dtos.StudyGroupDto;
import com.GradeCenter.entity.StudyGroup;
import com.GradeCenter.service.StudyGroupService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/studyGroup")
public class StudyGroupController {

    @Autowired
    StudyGroupService studyGroupService;

    @PostMapping
    public StudyGroupDto saveStudyGroup(@Valid @RequestBody StudyGroup studyGroup){
        return studyGroupService.saveStudyGroup(studyGroup);
    }

    @GetMapping
    public ResponseEntity<List<StudyGroupDto>> fetchStudyGroupList(){
        return ResponseEntity.ok(studyGroupService.fetchStudyGroup());
    }

    @GetMapping("/{id}")
    public ResponseEntity<StudyGroupDto> fetchStudyGroupById(@PathVariable("id") Long studyGroupId){
        return ResponseEntity.ok(studyGroupService.fetchStudyGroupById(studyGroupId));
    }

    @PutMapping("/{id}")
    public ResponseEntity<StudyGroupDto> updateStudyGroupById(@PathVariable("id") Long studyGroupId){
        return ResponseEntity.ok(studyGroupService.updateStudyGroupById(studyGroupId));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteStudyGroupById(@PathVariable("id")Long studyGroupId){
        studyGroupService.deleteStudyGroupById(studyGroupId);
        return ResponseEntity.ok("Course was successfully deleted!");
    }

}
