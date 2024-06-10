package com.GradeCenter.service.implementation;

import com.GradeCenter.dtos.StudentDto;
import com.GradeCenter.dtos.UserIDRequest;
import com.GradeCenter.repository.StudentRepository;
import com.GradeCenter.service.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class StudentServiceImpl implements StudentService {

    @Autowired
    StudentRepository studentRepository;

    @Override
    public List<StudentDto> getAllStudents() {

        // Implement the method to get all students and map them to StudentDto

        return null;
    }

    @Override
    public Optional<StudentDto> getStudentByUId(String uid) {

        // Implement the method to get a student by UserID and map it to StudentDto

        return Optional.empty();
    }

    @Override
    public ResponseEntity<String> deleteStudentUID(String userID) {

        // Implement the method to delete a student by UserID

        return ResponseEntity.ok("Successfully deleted student with UserID: " + userID + " from the database.");
    }

    @Override
    public ResponseEntity<String> deleteStudentID(Long ID) {

        // Implement the method to delete a student by ID

        return ResponseEntity.ok("Successfully deleted student with ID: " + ID + " from the database.");
    }

    @Override
    public Optional<StudentDto> updateStudentID(Long id, StudentDto studentDto) {

        // Implement the method to update a student by ID

        return null;
    }

    @Override
    public Optional<StudentDto> updateStudentUID(String userID, StudentDto studentDto) {

        // Implement the method to update a student by UserID

        return null;
    }

    @Override
    public StudentDto addStudent(UserIDRequest userIDRequest) {

        // Implement the method to add a student to the database

        return null;
    }

    @Override
    public Optional<StudentDto> getStudentById(Long id) {

        // Implement the method to get a student by ID and map it to StudentDto

        return Optional.empty();
    }


}
