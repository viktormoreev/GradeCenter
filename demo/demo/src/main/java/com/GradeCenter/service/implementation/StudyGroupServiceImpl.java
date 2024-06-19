package com.GradeCenter.service.implementation;

import com.GradeCenter.dtos.StudentCourseDto;
import com.GradeCenter.dtos.StudentFullReturnDto;
import com.GradeCenter.dtos.StudyGroupCreateRequest;
import com.GradeCenter.dtos.StudyGroupDto;
import com.GradeCenter.entity.School;
import com.GradeCenter.entity.Student;
import com.GradeCenter.entity.StudyGroup;
import com.GradeCenter.mapper.EntityMapper;
import com.GradeCenter.repository.SchoolRepository;
import com.GradeCenter.repository.StudyGroupRepository;
import com.GradeCenter.service.CourseService;
import com.GradeCenter.service.StudyGroupService;
import jakarta.persistence.EntityNotFoundException;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class StudyGroupServiceImpl implements StudyGroupService {
    @Autowired
    private EntityMapper entityMapper;
    @Autowired
    private StudyGroupRepository studyGroupRepository;

    @Autowired
    private SchoolRepository schoolRepository;

    @Autowired
    private CourseService courseService;

    @Autowired
    private KeycloakAdminClientService keycloakAdminClientService;

    @Override
    public StudyGroupDto saveStudyGroupInSchool(StudyGroupCreateRequest studyGroup) {
        Optional<School> optionalSchool = schoolRepository.findById(studyGroup.getSchoolId());
        if(optionalSchool.isPresent()){
            School school = optionalSchool.get();
            StudyGroup newStudyGroup = new StudyGroup();
            newStudyGroup.setName(studyGroup.getName());
            school.getStudyGroups().add(newStudyGroup);
            newStudyGroup.setSchool(school);
            newStudyGroup.setStudents(new ArrayList<>());
            schoolRepository.save(school);
            return ConvertToDto(newStudyGroup);
        }
        throw new EntityNotFoundException("School is not found");
    }

    @Override
    public List<StudyGroupDto> fetchStudyGroups() {
        return ConvertToDtoList(studyGroupRepository.findAll());
    }

    @Override
    public StudyGroupDto fetchStudyGroupById(Long studyGroupId) {
        Optional<StudyGroup> studyGroup = studyGroupRepository.findById(studyGroupId);
        if(studyGroup.isPresent()){
            return ConvertToDto(studyGroup.get());
        }
        else throw new EntityNotFoundException("Study group is not found");
    }

    @Override
    public StudyGroupDto updateStudyGroupById(Long studyGroupId) {
        Optional<StudyGroup> studyGroup = studyGroupRepository.findById(studyGroupId);
        if(studyGroup.isPresent()){
            return ConvertToDto(studyGroup.get());
        }
        else throw new EntityNotFoundException("Study group is not found");
    }

    @Override
    public void deleteStudyGroupById(Long studyGroupId) {
        studyGroupRepository.deleteById(studyGroupId);
    }

    @Override
    public List<StudyGroupDto> fetchStudyGroupsBySchoolId(Long schoolId) {
        Optional<School> school = schoolRepository.findById(schoolId);
        if(school.isPresent()){
            return ConvertToDtoList(school.get().getStudyGroups());
        }
        return null;
    }

    private StudyGroupDto ConvertToDto(StudyGroup studyGroup){
        List<Student> students = studyGroup.getStudents();

        List<String> userIds = studyGroup.getStudents().stream()
                .map(Student::getUserID)
                .collect(Collectors.toList());

        Map<String, UserRepresentation> keycloakUserMap = keycloakAdminClientService.getUsersFromIDs(userIds).stream()
                .collect(Collectors.toMap(UserRepresentation::getId, user -> user));

        Map<Long, List<StudentCourseDto>> studentCoursesMap = students.stream()
                .collect(Collectors.toMap(Student::getId, student -> courseService.fetchCourseByStudentId(student.getId())));

        List<StudentFullReturnDto> fullStudents = entityMapper.mapToStudentFullReturnDtoList(students, keycloakUserMap, studentCoursesMap);
        return entityMapper.mapToStudyGroupDto(studyGroup, fullStudents);
    }

    private List<StudyGroupDto> ConvertToDtoList(List<StudyGroup> studyGroups){
        return studyGroups.stream()
                .map(this::ConvertToDto)
                .collect(Collectors.toList());
    }
}
