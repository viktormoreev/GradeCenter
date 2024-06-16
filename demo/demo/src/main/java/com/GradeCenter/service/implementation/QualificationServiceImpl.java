package com.GradeCenter.service.implementation;

import com.GradeCenter.dtos.QualificationDto;
import com.GradeCenter.entity.Qualification;
import com.GradeCenter.exceptions.QualificationNotFoundException;
import com.GradeCenter.mapper.EntityMapper;
import com.GradeCenter.repository.QualificationRepository;
import com.GradeCenter.service.QualificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class QualificationServiceImpl implements QualificationService {

    @Autowired
    private EntityMapper entityMapper;

    @Autowired
    private QualificationRepository qualificationRepository;

    @Override
    public List<QualificationDto> getAllQualifications() {
        return entityMapper.mapToQualificationListDto(qualificationRepository.findAll());
    }

    @Override
    public QualificationDto getQualificationById(Long id) throws QualificationNotFoundException {
        return qualificationRepository.findById(id)
                .map(entityMapper::mapToQualificationDto)
                .orElseThrow(() -> new QualificationNotFoundException("Qualification not found"));
    }

    @Override
    public QualificationDto createQualification(QualificationDto qualificationDto) {
        Qualification qualification = entityMapper.mapToQualificationEntity(qualificationDto);
        Qualification savedQualification = qualificationRepository.save(qualification);
        return entityMapper.mapToQualificationDto(savedQualification);
    }

    @Override
    public QualificationDto updateQualification(Long id, QualificationDto qualificationDto) throws QualificationNotFoundException {
        Qualification existingQualification = qualificationRepository.findById(id)
                .orElseThrow(() -> new QualificationNotFoundException("Qualification not found"));

        existingQualification.setArea(qualificationDto.getArea());

        Qualification updatedQualification = qualificationRepository.save(existingQualification);
        return entityMapper.mapToQualificationDto(updatedQualification);
    }

    @Override
    public void deleteQualification(Long id) {
        qualificationRepository.deleteById(id);
    }
}
