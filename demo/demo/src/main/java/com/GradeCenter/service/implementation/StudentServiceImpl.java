package com.GradeCenter.service.implementation;

import com.GradeCenter.dtos.*;
import com.GradeCenter.entity.Parent;
import com.GradeCenter.entity.Student;
import com.GradeCenter.entity.StudyGroup;
import com.GradeCenter.mapper.EntityMapper;
import com.GradeCenter.repository.ParentRepository;
import com.GradeCenter.repository.StudentRepository;
import com.GradeCenter.repository.StudyGroupRepository;
import com.GradeCenter.service.CourseService;
import com.GradeCenter.service.StudentService;
import org.hibernate.validator.internal.util.stereotypes.Lazy;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class StudentServiceImpl implements StudentService {

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private ParentRepository parentRepository;

    @Autowired
    private StudyGroupRepository studyGroupRepository;

    @Autowired
    private EntityMapper entityMapper;

    @Autowired
    private KeycloakAdminClientService keycloakAdminClientService;

    @Autowired
    private CourseService courseService;

    @Override
    public List<StudentDto> getAllStudents() {
        return entityMapper.mapToStudentListDto(studentRepository.findAll());
    }

    @Override
    public StudentDto getStudentByUId(String uid) {
        return studentRepository.findByUserID(uid)
                .map(entityMapper::mapToStudentDto)
                .orElse(null);
    }

    @Override
    public boolean deleteStudentUID(String userID) {
        Optional<Student> student = studentRepository.findByUserID(userID);
        if (student.isPresent()) {
            studentRepository.delete(student.get());
            return true;
        }
        return false;
    }

    @Override
    public boolean deleteStudentID(Long ID) {
        if (studentRepository.existsById(ID)) {
            studentRepository.deleteById(ID);
            return true;
        }
        return false;
    }

    @Override
    public StudentDto updateStudentID(Long id, StudentUpdateDto studentUpdateDto) {
        Optional<Student> existingStudentOpt = studentRepository.findById(id);
        if (existingStudentOpt.isPresent()) {
            Student existingStudent = existingStudentOpt.get();
            updateStudentFromDto(existingStudent, studentUpdateDto);
            studentRepository.save(existingStudent);
            return entityMapper.mapToStudentDto(existingStudent);
        }
        return null;
    }

    @Override
    public StudentDto updateStudentUID(String userID, StudentUpdateDto studentUpdateDto) {
        Optional<Student> existingStudentOpt = studentRepository.findByUserID(userID);
        if (existingStudentOpt.isPresent()) {
            Student existingStudent = existingStudentOpt.get();
            updateStudentFromDto(existingStudent, studentUpdateDto);
            studentRepository.save(existingStudent);
            return entityMapper.mapToStudentDto(existingStudent);
        }
        return null;
    }

    @Override
    public List<StudentFullReturnDto> getAllStudentsFull() {

        List<Student> students = studentRepository.findAll();
        List<String> userIds = students.stream()
                .map(Student::getUserID)
                .collect(Collectors.toList());

        List<UserRepresentation> keycloakUsers = keycloakAdminClientService.getUsersFromIDs(userIds);

        return students.stream().map(student -> {
            StudentFullReturnDto studentFullDto = new StudentFullReturnDto();
            studentFullDto.setId(student.getId());

            // Fetch username from Keycloak user representation
            UserRepresentation keycloakUser = keycloakUsers.stream()
                    .filter(ku -> ku.getId().equals(student.getUserID()))
                    .findFirst()
                    .orElse(null);

            if (keycloakUser != null) {
                studentFullDto.setUsername(keycloakUser.getUsername());
            }

            studentFullDto.setGrade(""); // Set grade appropriately
            studentFullDto.setSchool(student.getClasses() != null ? student.getClasses().getName() : null);
            studentFullDto.setAbsences(0); // Set absences appropriately

            List<String> parentIDs = student.getParents().stream()
                    .map(Parent::getUserID)
                    .collect(Collectors.toList());

            List<UserRepresentation> parentUsers = keycloakAdminClientService.getUsersFromIDs(parentIDs);
            List<String> parentNames = parentUsers.stream()
                    .map(UserRepresentation::getUsername)
                    .collect(Collectors.toList());

            studentFullDto.setParent(parentNames);
            studentFullDto.setCourses(courseService.fetchCourseByStudentId(student.getId()));

            return studentFullDto;
        }).collect(Collectors.toList());
    }

    @Override
    public StudentDto addStudent(UserIDRequest userIDRequest) {
        Student student = Student.builder()
                .userID(userIDRequest.getUserID())
                .build();
        studentRepository.save(student);
        return entityMapper.mapToStudentDto(student);
    }

    @Override
    public StudentDto getStudentById(Long id) {
        return studentRepository.findById(id)
                .map(entityMapper::mapToStudentDto)
                .orElse(null);
    }

    private void updateStudentFromDto(Student student, StudentUpdateDto studentUpdateDto) {
        if (studentUpdateDto.getClassesID() != null) {
            StudyGroup studyGroup = studyGroupRepository.findById(studentUpdateDto.getClassesID())
                    .orElseThrow(() -> new IllegalArgumentException("Invalid class ID"));
            student.setClasses(studyGroup);
        } else {
            student.setClasses(null);
        }

        if (studentUpdateDto.getParentsID() != null) {
            List<Parent> parents = studentUpdateDto.getParentsID().stream()
                    .map(parentId -> parentRepository.findById(parentId)
                            .orElseThrow(() -> new IllegalArgumentException("Invalid parent ID")))
                    .collect(Collectors.toList());
            student.setParents(parents);
        } else {
            student.setParents(null);
        }
    }
}
