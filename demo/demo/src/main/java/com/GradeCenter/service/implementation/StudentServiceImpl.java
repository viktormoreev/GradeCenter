package com.GradeCenter.service.implementation;

import com.GradeCenter.dtos.*;
import com.GradeCenter.entity.Parent;
import com.GradeCenter.entity.School;
import com.GradeCenter.entity.Student;
import com.GradeCenter.entity.StudyGroup;
import com.GradeCenter.mapper.EntityMapper;
import com.GradeCenter.repository.ParentRepository;
import com.GradeCenter.repository.SchoolRepository;
import com.GradeCenter.repository.StudentRepository;
import com.GradeCenter.repository.StudyGroupRepository;
import com.GradeCenter.service.CourseService;
import com.GradeCenter.service.StudentService;
import com.GradeCenter.service.StudyGroupService;
import jakarta.persistence.EntityNotFoundException;
import org.hibernate.validator.internal.util.stereotypes.Lazy;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Map;
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
    public List<StudentFullReturnDto> getAllStudentsFullSchool(Long id) {
        List<Student> students = studentRepository.findByClasses_School_Id(id);

        List<String> userIds = students.stream()
                .map(Student::getUserID)
                .collect(Collectors.toList());

        Map<String, UserRepresentation> keycloakUserMap = keycloakAdminClientService.getUsersFromIDs(userIds).stream()
                .collect(Collectors.toMap(UserRepresentation::getId, user -> user));

        Map<Long, List<StudentCourseDto>> studentCoursesMap = students.stream()
                .collect(Collectors.toMap(
                        Student::getId,
                        student -> Optional.ofNullable(courseService.fetchCourseByStudentId(student.getId()))
                                .orElse(Collections.emptyList())
                ));

        return entityMapper.mapToStudentFullReturnDtoList(students, keycloakUserMap, studentCoursesMap);
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

        Map<String, UserRepresentation> keycloakUserMap = keycloakAdminClientService.getUsersFromIDs(userIds).stream()
                .collect(Collectors.toMap(UserRepresentation::getId, user -> user));

        Map<Long, List<StudentCourseDto>> studentCoursesMap = students.stream()
                .collect(Collectors.toMap(
                        Student::getId,
                        student -> Optional.ofNullable(courseService.fetchCourseByStudentId(student.getId()))
                                .orElse(Collections.emptyList())
                ));
        return entityMapper.mapToStudentFullReturnDtoList(students, keycloakUserMap, studentCoursesMap);
    }

    @Override
    public StudentFullReturnDto getFullStudentById(Long id) {
        Student student = studentRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Student with that id is not found"));

        List<String> userIds = List.of(student.getUserID());
        Map<String, UserRepresentation> keycloakUserMap = keycloakAdminClientService.getUsersFromIDs(userIds).stream()
                .collect(Collectors.toMap(UserRepresentation::getId, user -> user));

        List<StudentCourseDto> courses = courseService.fetchCourseByStudentId(student.getId());

        return entityMapper.mapToStudentFullReturnDto(student, keycloakUserMap, courses);
    }

    @Override
    public StudentDto addStudentToStudyGroup(Long studyGroupId, Long studentId) {
        Optional<StudyGroup> studyGroup = studyGroupRepository.findById(studyGroupId);
        if(studyGroup.isPresent()){
//            Optional<Student> optionalStudent = studentRepository.findByUserID(userID);
            Optional<Student> optionalStudent = studentRepository.findById(studentId);
            if(optionalStudent.isPresent()){
                Student student = optionalStudent.get();
                student.setClasses(studyGroup.get());
                return entityMapper.mapToStudentDto(studentRepository.save(student));
            }
            else throw new EntityNotFoundException("Student is not found");
        }
        else throw new EntityNotFoundException("Study Group is not found");
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
            // Add the student to the class
            Optional<StudyGroup> studyGroup = studyGroupRepository.findById(studentUpdateDto.getClassesID());
            if (studyGroup.isPresent()) {
                student.setClasses(studyGroup.get());
                studyGroup.get().getStudents().add(student);
                studyGroupRepository.save(studyGroup.get());

            } else {
                throw new IllegalArgumentException("Invalid class ID");
            }
        } else {
            student.setClasses(null);
        }

        if (studentUpdateDto.getParentsID() != null) {
            // Add the student to the parents' list of students
            List<Parent> parents = studentUpdateDto.getParentsID().stream()
                    .map(parentId -> parentRepository.findById(parentId)
                            .map(parent -> {
                                parent.getStudents().add(student);
                                parentRepository.save(parent);
                                return parent;
                            })
                            .orElseThrow(() -> new IllegalArgumentException("Invalid parent ID")))
                    .collect(Collectors.toList());
            student.setParents(parents);
        } else {
            student.setParents(null);
        }
    }
}
