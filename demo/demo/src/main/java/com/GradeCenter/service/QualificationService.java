package com.GradeCenter.service;

import com.GradeCenter.dtos.QualificationDto;
import com.GradeCenter.exceptions.QualificationNotFoundException;

import java.util.List;

public interface QualificationService {
    List<QualificationDto> getAllQualifications();
    QualificationDto getQualificationById(Long id) throws QualificationNotFoundException;
    QualificationDto createQualification(QualificationDto qualificationDto);
    QualificationDto updateQualification(Long id, QualificationDto qualificationDto) throws QualificationNotFoundException;
    void deleteQualification(Long id);
}
