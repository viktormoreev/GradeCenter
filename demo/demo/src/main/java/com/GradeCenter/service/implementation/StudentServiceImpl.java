package com.GradeCenter.service.implementation;

import com.GradeCenter.dtos.StudentDto;
import com.GradeCenter.dtos.UserIDRequest;
import com.GradeCenter.repository.StudentRepository;
import com.GradeCenter.service.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
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
    public StudentDto addStudent(UserIDRequest userIDRequest) {
        return null;
    }


}
