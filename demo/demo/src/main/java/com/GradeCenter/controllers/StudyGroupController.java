package com.GradeCenter.controllers;

import com.GradeCenter.dtos.StudyGroupCreateRequest;
import com.GradeCenter.dtos.StudyGroupDto;
import com.GradeCenter.service.StudyGroupService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/studyGroup")
public class StudyGroupController {

    @Autowired
    StudyGroupService studyGroupService;

    @PostMapping
    public StudyGroupDto saveStudyGroup(@Valid @RequestBody StudyGroupCreateRequest studyGroup){
        return studyGroupService.saveStudyGroupInSchool(studyGroup);
    }

    @PreAuthorize("hasRole('admin')")
    @GetMapping
    public ResponseEntity<List<StudyGroupDto>> fetchStudyGroupList(){
        return ResponseEntity.ok(studyGroupService.fetchStudyGroups());
    }

    @PreAuthorize("hasRole('admin')")
    @GetMapping("/{id}")
    public ResponseEntity<StudyGroupDto> fetchStudyGroupById(@PathVariable("id") Long studyGroupId){
        return ResponseEntity.ok(studyGroupService.fetchStudyGroupById(studyGroupId));
    }

    @PreAuthorize("hasRole('admin')")
    @GetMapping("/schoolId={id}")
    public ResponseEntity<List<StudyGroupDto>> fetchStudyGroupBySchoolId(@PathVariable("id") Long schoolId){
        return ResponseEntity.ok(studyGroupService.fetchStudyGroupsBySchoolId(schoolId));
    }

    @PreAuthorize("hasRole('admin')")
    @PutMapping("/{id}")
    public ResponseEntity<StudyGroupDto> updateStudyGroupById(@PathVariable("id") Long studyGroupId){
        return ResponseEntity.ok(studyGroupService.updateStudyGroupById(studyGroupId));
    }

    @PreAuthorize("hasRole('admin')")
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteStudyGroupById(@PathVariable("id")Long studyGroupId){
        studyGroupService.deleteStudyGroupById(studyGroupId);
        return ResponseEntity.ok("Course was successfully deleted!");
    }

}
