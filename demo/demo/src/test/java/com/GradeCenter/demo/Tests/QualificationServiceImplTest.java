package com.GradeCenter.demo.Tests;

import com.GradeCenter.dtos.QualificationDto;
import com.GradeCenter.entity.Qualification;
import com.GradeCenter.exceptions.QualificationNotFoundException;
import com.GradeCenter.mapper.EntityMapper;
import com.GradeCenter.repository.QualificationRepository;
import com.GradeCenter.service.implementation.QualificationServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class QualificationServiceImplTest {

    @Mock
    private EntityMapper entityMapper;

    @Mock
    private QualificationRepository qualificationRepository;

    @InjectMocks
    private QualificationServiceImpl qualificationService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getAllQualifications_ShouldReturnListOfQualificationDtos() {

        List<Qualification> qualifications = Arrays.asList(new Qualification(), new Qualification());
        List<QualificationDto> expectedDtos = Arrays.asList(new QualificationDto(), new QualificationDto());

        when(qualificationRepository.findAll()).thenReturn(qualifications);
        when(entityMapper.mapToQualificationListDto(qualifications)).thenReturn(expectedDtos);

        List<QualificationDto> result = qualificationService.getAllQualifications();

        assertEquals(expectedDtos, result);
        verify(qualificationRepository).findAll();
        verify(entityMapper).mapToQualificationListDto(qualifications);
    }

    @Test
    void getQualificationById_ExistingId_ShouldReturnQualificationDto() throws QualificationNotFoundException {

        Long id = 1L;
        Qualification qualification = new Qualification();
        QualificationDto expectedDto = new QualificationDto();

        when(qualificationRepository.findById(id)).thenReturn(Optional.of(qualification));
        when(entityMapper.mapToQualificationDto(qualification)).thenReturn(expectedDto);

        QualificationDto result = qualificationService.getQualificationById(id);

        assertEquals(expectedDto, result);
        verify(qualificationRepository).findById(id);
        verify(entityMapper).mapToQualificationDto(qualification);
    }

    @Test
    void getQualificationById_NonExistingId_ShouldThrowException() {

        Long id = 1L;
        when(qualificationRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(QualificationNotFoundException.class, () -> qualificationService.getQualificationById(id));
        verify(qualificationRepository).findById(id);
    }

    @Test
    void createQualification_ShouldReturnCreatedQualificationDto() {

        QualificationDto inputDto = new QualificationDto();
        Qualification qualification = new Qualification();
        Qualification savedQualification = new Qualification();
        QualificationDto expectedDto = new QualificationDto();

        when(entityMapper.mapToQualificationEntity(inputDto)).thenReturn(qualification);
        when(qualificationRepository.save(qualification)).thenReturn(savedQualification);
        when(entityMapper.mapToQualificationDto(savedQualification)).thenReturn(expectedDto);

        QualificationDto result = qualificationService.createQualification(inputDto);

        assertEquals(expectedDto, result);
        verify(entityMapper).mapToQualificationEntity(inputDto);
        verify(qualificationRepository).save(qualification);
        verify(entityMapper).mapToQualificationDto(savedQualification);
    }

    @Test
    void updateQualification_ExistingId_ShouldReturnUpdatedQualificationDto() throws QualificationNotFoundException {

        Long id = 1L;
        QualificationDto inputDto = new QualificationDto();
        inputDto.setArea("Updated Area");
        Qualification existingQualification = new Qualification();
        Qualification updatedQualification = new Qualification();
        QualificationDto expectedDto = new QualificationDto();

        when(qualificationRepository.findById(id)).thenReturn(Optional.of(existingQualification));
        when(qualificationRepository.save(existingQualification)).thenReturn(updatedQualification);
        when(entityMapper.mapToQualificationDto(updatedQualification)).thenReturn(expectedDto);

        QualificationDto result = qualificationService.updateQualification(id, inputDto);

        assertEquals(expectedDto, result);
        assertEquals("Updated Area", existingQualification.getArea());
        verify(qualificationRepository).findById(id);
        verify(qualificationRepository).save(existingQualification);
        verify(entityMapper).mapToQualificationDto(updatedQualification);
    }

    @Test
    void updateQualification_NonExistingId_ShouldThrowException() {

        Long id = 1L;
        QualificationDto inputDto = new QualificationDto();
        when(qualificationRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(QualificationNotFoundException.class, () -> qualificationService.updateQualification(id, inputDto));
        verify(qualificationRepository).findById(id);
    }

    @Test
    void deleteQualification_ShouldCallRepositoryDeleteById() {

        Long id = 1L;

        qualificationService.deleteQualification(id);

        verify(qualificationRepository).deleteById(id);
    }
}