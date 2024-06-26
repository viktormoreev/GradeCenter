package com.GradeCenter.demo.Tests;

import com.GradeCenter.dtos.DirectorDto;
import com.GradeCenter.dtos.DirectorUpdateDto;
import com.GradeCenter.dtos.SchoolDto;
import com.GradeCenter.dtos.UserIDRequest;
import com.GradeCenter.entity.Director;
import com.GradeCenter.entity.School;
import com.GradeCenter.mapper.EntityMapper;
import com.GradeCenter.repository.DirectorRepository;
import com.GradeCenter.repository.SchoolRepository;
import com.GradeCenter.service.implementation.DirectorServiceImpl;
import jakarta.persistence.EntityNotFoundException;
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

class DirectorServiceImplTest {

    @Mock
    private DirectorRepository directorRepository;

    @Mock
    private SchoolRepository schoolRepository;

    @Mock
    private EntityMapper entityMapper;

    @InjectMocks
    private DirectorServiceImpl directorService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getAllDirectors() {
        Director director1 = new Director();
        Director director2 = new Director();
        List<Director> directors = Arrays.asList(director1, director2);

        DirectorDto directorDto1 = new DirectorDto();
        DirectorDto directorDto2 = new DirectorDto();
        List<DirectorDto> directorDtos = Arrays.asList(directorDto1, directorDto2);

        when(directorRepository.findAll()).thenReturn(directors);
        when(entityMapper.mapToDirectorListDto(directors)).thenReturn(directorDtos);

        List<DirectorDto> result = directorService.getAllDirectors();

        assertEquals(directorDtos, result);
        verify(directorRepository).findAll();
        verify(entityMapper).mapToDirectorListDto(directors);
    }

    @Test
    void getDirectorByUId() {
        String uid = "testUid";
        Director director = new Director();
        DirectorDto directorDto = new DirectorDto();

        when(directorRepository.findByUserID(uid)).thenReturn(Optional.of(director));
        when(entityMapper.mapToDirectorDto(director)).thenReturn(directorDto);

        DirectorDto result = directorService.getDirectorByUId(uid);

        assertEquals(directorDto, result);
        verify(directorRepository).findByUserID(uid);
        verify(entityMapper).mapToDirectorDto(director);
    }

    @Test
    void deleteDirectorUID() {
        String userID = "testUid";
        Director director = new Director();

        when(directorRepository.findByUserID(userID)).thenReturn(Optional.of(director));

        boolean result = directorService.deleteDirectorUID(userID);

        assertTrue(result);
        verify(directorRepository).findByUserID(userID);
        verify(directorRepository).delete(director);
    }

    @Test
    void addDirectorToSchool() {
        Long directorId = 1L;
        Long schoolId = 1L;
        Director director = new Director();
        School school = new School();
        SchoolDto schoolDto = new SchoolDto();

        when(directorRepository.findById(directorId)).thenReturn(Optional.of(director));
        when(schoolRepository.findById(schoolId)).thenReturn(Optional.of(school));
        when(schoolRepository.save(school)).thenReturn(school);
        when(entityMapper.mapToSchoolDto(school)).thenReturn(schoolDto);

        SchoolDto result = directorService.addDirectorToSchool(directorId, schoolId);

        assertEquals(schoolDto, result);
        verify(directorRepository).findById(directorId);
        verify(schoolRepository).findById(schoolId);
        verify(schoolRepository, times(2)).save(school);
        verify(entityMapper).mapToSchoolDto(school);
    }

    @Test
    void addDirector() {
        UserIDRequest userIDRequest = new UserIDRequest("testUid");
        Director director = Director.builder().userID("testUid").build();
        DirectorDto directorDto = new DirectorDto();

        when(directorRepository.save(any(Director.class))).thenReturn(director);
        when(entityMapper.mapToDirectorDto(director)).thenReturn(directorDto);

        DirectorDto result = directorService.addDirector(userIDRequest);

        assertEquals(directorDto, result);
        verify(directorRepository).save(any(Director.class));
        verify(entityMapper).mapToDirectorDto(director);
    }

    @Test
    void getDirectorByUId_NotFound() {
        String uid = "nonExistentUid";
        when(directorRepository.findByUserID(uid)).thenReturn(Optional.empty());

        DirectorDto result = directorService.getDirectorByUId(uid);

        assertNull(result);
        verify(directorRepository).findByUserID(uid);
        verify(entityMapper, never()).mapToDirectorDto(any());
    }

    @Test
    void deleteDirectorUID_NotFound() {
        String userID = "nonExistentUid";
        when(directorRepository.findByUserID(userID)).thenReturn(Optional.empty());

        boolean result = directorService.deleteDirectorUID(userID);

        assertFalse(result);
        verify(directorRepository).findByUserID(userID);
        verify(directorRepository, never()).delete(any());
    }

    @Test
    void deleteDirectorID() {
        Long id = 1L;
        when(directorRepository.existsById(id)).thenReturn(true);

        boolean result = directorService.deleteDirectorID(id);

        assertTrue(result);
        verify(directorRepository).existsById(id);
        verify(directorRepository).deleteById(id);
    }

    @Test
    void deleteDirectorID_NotFound() {
        Long id = 1L;
        when(directorRepository.existsById(id)).thenReturn(false);

        boolean result = directorService.deleteDirectorID(id);

        assertFalse(result);
        verify(directorRepository).existsById(id);
        verify(directorRepository, never()).deleteById(any());
    }

    @Test
    void updateDirectorID() {
        Long id = 1L;
        DirectorUpdateDto updateDto = new DirectorUpdateDto(2L);
        Director existingDirector = new Director();
        School school = new School();
        DirectorDto updatedDirectorDto = new DirectorDto();

        when(directorRepository.findById(id)).thenReturn(Optional.of(existingDirector));
        when(schoolRepository.findById(updateDto.getSchoolID())).thenReturn(Optional.of(school));
        when(directorRepository.save(existingDirector)).thenReturn(existingDirector);
        when(entityMapper.mapToDirectorDto(existingDirector)).thenReturn(updatedDirectorDto);

        DirectorDto result = directorService.updateDirectorID(id, updateDto);

        assertEquals(updatedDirectorDto, result);
        verify(directorRepository).findById(id);
        verify(schoolRepository).findById(updateDto.getSchoolID());
        verify(directorRepository).save(existingDirector);
        verify(entityMapper).mapToDirectorDto(existingDirector);
    }

    @Test
    void updateDirectorID_NotFound() {
        Long id = 1L;
        DirectorUpdateDto updateDto = new DirectorUpdateDto(2L);

        when(directorRepository.findById(id)).thenReturn(Optional.empty());

        DirectorDto result = directorService.updateDirectorID(id, updateDto);

        assertNull(result);
        verify(directorRepository).findById(id);
        verify(schoolRepository, never()).findById(any());
        verify(directorRepository, never()).save(any());
    }

    @Test
    void removeDirectorFromSchool() {
        Long schoolId = 1L;
        School school = new School();
        SchoolDto schoolDto = new SchoolDto();

        when(schoolRepository.findById(schoolId)).thenReturn(Optional.of(school));
        when(schoolRepository.save(school)).thenReturn(school);
        when(entityMapper.mapToSchoolDto(school)).thenReturn(schoolDto);

        SchoolDto result = directorService.removeDirectorFromSchool(schoolId);

        assertEquals(schoolDto, result);
        assertNull(school.getDirector());
        verify(schoolRepository).findById(schoolId);
        verify(schoolRepository).save(school);
        verify(entityMapper).mapToSchoolDto(school);
    }

    @Test
    void removeDirectorFromSchool_SchoolNotFound() {
        Long schoolId = 1L;

        when(schoolRepository.findById(schoolId)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> directorService.removeDirectorFromSchool(schoolId));
        verify(schoolRepository).findById(schoolId);
        verify(schoolRepository, never()).save(any());
    }

    @Test
    void addDirectorToSchool_DirectorNotFound() {
        Long directorId = 1L;
        Long schoolId = 1L;

        when(directorRepository.findById(directorId)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> directorService.addDirectorToSchool(directorId, schoolId));
        verify(directorRepository).findById(directorId);
        verify(schoolRepository, never()).findById(any());
    }

    @Test
    void addDirectorToSchool_SchoolNotFound() {
        Long directorId = 1L;
        Long schoolId = 1L;
        Director director = new Director();

        when(directorRepository.findById(directorId)).thenReturn(Optional.of(director));
        when(schoolRepository.findById(schoolId)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> directorService.addDirectorToSchool(directorId, schoolId));
        verify(directorRepository).findById(directorId);
        verify(schoolRepository).findById(schoolId);
    }
}