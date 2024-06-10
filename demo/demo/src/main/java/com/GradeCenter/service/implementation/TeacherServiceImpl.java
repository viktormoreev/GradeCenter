package com.GradeCenter.service.implementation;

import com.GradeCenter.dtos.TeacherDto;
import com.GradeCenter.dtos.UserIDRequest;
import com.GradeCenter.repository.TeacherRepository;
import com.GradeCenter.service.TeacherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TeacherServiceImpl implements TeacherService {

    @Autowired
    TeacherRepository teacherRepository;

    @Override
    public List<TeacherDto> getAllTeachers() {

        // Implement the method to get all teachers and map them to TeacherDto

        return null;
    }

    @Override
    public Optional<TeacherDto> getTeacherByUId(String uid) {

        // Implement the method to get a teacher by UserID and map it to TeacherDto

        return Optional.empty();
    }

    @Override
    public ResponseEntity<String> deleteTeacherUID(String userID) {

        // Implement the method to delete a teacher by UserID

        return ResponseEntity.ok("Successfully deleted teacher with UserID: " + userID + " from the database.");
    }

    @Override
    public ResponseEntity<String> deleteTeacherID(Long ID) {

        // Implement the method to delete a teacher by ID

        return ResponseEntity.ok("Successfully deleted teacher with ID: " + ID + " from the database.");
    }

    @Override
    public Optional<TeacherDto> updateTeacherID(Long id, TeacherDto teacherDto) {

        // Implement the method to update a teacher by ID

        return null;
    }

    @Override
    public Optional<TeacherDto> updateTeacherUID(String userID, TeacherDto teacherDto) {

        // Implement the method to update a teacher by UserID

        return null;
    }

    @Override
    public TeacherDto addTeacher(UserIDRequest userIDRequest) {

        // Implement the method to add a teacher to the database

        return null;
    }

    @Override
    public Optional<TeacherDto> getTeacherById(Long id) {

        // Implement the method to get a teacher by ID and map it to TeacherDto

        return Optional.empty();
    }
}
