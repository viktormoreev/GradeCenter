package com.GradeCenter.demo.Tests;

import com.GradeCenter.dtos.ParentDto;
import com.GradeCenter.dtos.ParentUpdateDto;
import com.GradeCenter.dtos.UserIDRequest;
import com.GradeCenter.entity.Parent;
import com.GradeCenter.entity.Student;
import com.GradeCenter.mapper.EntityMapper;
import com.GradeCenter.repository.ParentRepository;
import com.GradeCenter.repository.StudentRepository;
import com.GradeCenter.service.implementation.ParentServiceImpl;
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

class ParentServiceImplTest {

    @Mock
    private ParentRepository parentRepository;

    @Mock
    private StudentRepository studentRepository;

    @Mock
    private EntityMapper entityMapper;

    @InjectMocks
    private ParentServiceImpl parentService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getAllParents() {
        List<Parent> parents = Arrays.asList(new Parent(), new Parent());
        List<ParentDto> parentDtos = Arrays.asList(new ParentDto(), new ParentDto());

        when(parentRepository.findAll()).thenReturn(parents);
        when(entityMapper.mapToParentListDto(parents)).thenReturn(parentDtos);

        List<ParentDto> result = parentService.getAllParents();

        assertEquals(parentDtos, result);
        verify(parentRepository).findAll();
        verify(entityMapper).mapToParentListDto(parents);
    }

    @Test
    void getParentByUId() {
        String uid = "testUid";
        Parent parent = new Parent();
        ParentDto parentDto = new ParentDto();

        when(parentRepository.findByUserID(uid)).thenReturn(Optional.of(parent));
        when(entityMapper.mapToParentDto(parent)).thenReturn(parentDto);

        ParentDto result = parentService.getParentByUId(uid);

        assertEquals(parentDto, result);
        verify(parentRepository).findByUserID(uid);
        verify(entityMapper).mapToParentDto(parent);
    }

    @Test
    void deleteParentUID() {
        String userID = "testUid";
        Parent parent = new Parent();

        when(parentRepository.findByUserID(userID)).thenReturn(Optional.of(parent));

        boolean result = parentService.deleteParentUID(userID);

        assertTrue(result);
        verify(parentRepository).findByUserID(userID);
        verify(parentRepository).delete(parent);
    }

    @Test
    void deleteParentID() {
        Long id = 1L;

        when(parentRepository.existsById(id)).thenReturn(true);

        boolean result = parentService.deleteParentID(id);

        assertTrue(result);
        verify(parentRepository).existsById(id);
        verify(parentRepository).deleteById(id);
    }

    @Test
    void updateParentID() {
        Long id = 1L;
        ParentUpdateDto updateDto = new ParentUpdateDto();
        Parent parent = new Parent();
        ParentDto parentDto = new ParentDto();

        when(parentRepository.findById(id)).thenReturn(Optional.of(parent));
        when(entityMapper.mapToParentDto(parent)).thenReturn(parentDto);

        ParentDto result = parentService.updateParentID(id, updateDto);

        assertEquals(parentDto, result);
        verify(parentRepository).findById(id);
        verify(parentRepository).save(parent);
        verify(entityMapper).mapToParentDto(parent);
    }

    @Test
    void addParent() {
        UserIDRequest request = new UserIDRequest("testUid");
        Parent parent = new Parent();
        ParentDto parentDto = new ParentDto();

        when(parentRepository.save(any(Parent.class))).thenReturn(parent);
        when(entityMapper.mapToParentDto(parent)).thenReturn(parentDto);

        ParentDto result = parentService.addParent(request);

        assertEquals(parentDto, result);
        verify(parentRepository).save(any(Parent.class));
        verify(entityMapper).mapToParentDto(parent);
    }

    @Test
    void getParentById() {
        Long id = 1L;
        Parent parent = new Parent();
        ParentDto parentDto = new ParentDto();

        when(parentRepository.findById(id)).thenReturn(Optional.of(parent));
        when(entityMapper.mapToParentDto(parent)).thenReturn(parentDto);

        ParentDto result = parentService.getParentById(id);

        assertEquals(parentDto, result);
        verify(parentRepository).findById(id);
        verify(entityMapper).mapToParentDto(parent);
    }
}