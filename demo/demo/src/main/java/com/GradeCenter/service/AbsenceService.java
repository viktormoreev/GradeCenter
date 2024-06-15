package com.GradeCenter.service;

import com.GradeCenter.dtos.AbsenceDto;
import com.GradeCenter.dtos.AbsenceStudentViewDto;
import com.GradeCenter.dtos.AbsenceTeacherViewDto;
import com.GradeCenter.exceptions.AbsenceNotFoundException;
import com.GradeCenter.exceptions.CourseNotFoundException;
import com.GradeCenter.exceptions.StudentNotFoundException;

import java.util.List;

public interface AbsenceService {
    List<AbsenceDto> getAllAbsences();
    List<AbsenceDto> getAbsencesByStudentId(long studentId);
    AbsenceDto createAbsence(AbsenceDto absenceDto) throws StudentNotFoundException, CourseNotFoundException;
    List<AbsenceStudentViewDto> getPersonalStudentAbsences(String userId) throws StudentNotFoundException;
    List<AbsenceDto> getAllAbsencesByStudentIdForAdmin(long studentId) throws StudentNotFoundException;
    boolean deleteAbsenceById(Long id);
    AbsenceDto updateAbsence(Long id, AbsenceDto absenceDto) throws StudentNotFoundException, CourseNotFoundException, AbsenceNotFoundException;
    List<AbsenceTeacherViewDto> getTeacherViewAbsencesByStudentId(long studentId);
}
