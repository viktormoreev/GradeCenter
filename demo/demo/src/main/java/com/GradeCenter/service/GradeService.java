package com.GradeCenter.service;

import com.GradeCenter.dtos.GradeDto;
import com.GradeCenter.dtos.GradeStudentViewDto;
import com.GradeCenter.exceptions.CourseNotFoundException;
import com.GradeCenter.exceptions.GradeNotFoundException;
import com.GradeCenter.exceptions.StudentNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface GradeService {
    List<GradeDto> getAllGrades();
    List<GradeDto> getGradesByStudentId(long studentId) throws StudentNotFoundException;
    GradeDto createGrade(GradeDto gradeDto) throws StudentNotFoundException, CourseNotFoundException;
    GradeDto updateGrade(Long id, GradeDto gradeDto) throws GradeNotFoundException, StudentNotFoundException, CourseNotFoundException;
    boolean deleteGradeById(Long id);
    List<GradeStudentViewDto> getPersonalStudentGrades(String userId) throws StudentNotFoundException;
}