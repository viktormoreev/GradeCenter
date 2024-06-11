package com.GradeCenter.service.implementation;

import com.GradeCenter.dtos.ParentDto;
import com.GradeCenter.dtos.ParentUpdateDto;
import com.GradeCenter.dtos.UserIDRequest;
import com.GradeCenter.entity.Parent;
import com.GradeCenter.entity.Student;
import com.GradeCenter.mapper.EntityMapper;
import com.GradeCenter.repository.ParentRepository;
import com.GradeCenter.repository.StudentRepository;
import com.GradeCenter.service.ParentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ParentServiceImpl implements ParentService {

    @Autowired
    private ParentRepository parentRepository;

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private EntityMapper entityMapper;

    @Override
    public List<ParentDto> getAllParents() {
        return entityMapper.mapToParentListDto(parentRepository.findAll());
    }

    @Override
    public ParentDto getParentByUId(String uid) {
        return parentRepository.findByUserID(uid)
                .map(entityMapper::mapToParentDto)
                .orElse(null);
    }

    @Override
    public boolean deleteParentUID(String userID) {
        Optional<Parent> parent = parentRepository.findByUserID(userID);
        if (parent.isPresent()) {
            parentRepository.delete(parent.get());
            return true;
        }
        return false;
    }

    @Override
    public boolean deleteParentID(Long ID) {
        if (parentRepository.existsById(ID)) {
            parentRepository.deleteById(ID);
            return true;
        }
        return false;
    }

    @Override
    public ParentDto updateParentID(Long id, ParentUpdateDto parentUpdateDto) {
        Optional<Parent> existingParentOpt = parentRepository.findById(id);
        if (existingParentOpt.isPresent()) {
            Parent existingParent = existingParentOpt.get();
            updateParentFromDto(existingParent, parentUpdateDto);
            parentRepository.save(existingParent);
            return entityMapper.mapToParentDto(existingParent);
        }
        return null;
    }

    @Override
    public ParentDto updateParentUID(String userID, ParentUpdateDto parentUpdateDto) {
        Optional<Parent> existingParentOpt = parentRepository.findByUserID(userID);
        if (existingParentOpt.isPresent()) {
            Parent existingParent = existingParentOpt.get();
            updateParentFromDto(existingParent, parentUpdateDto);
            parentRepository.save(existingParent);
            return entityMapper.mapToParentDto(existingParent);
        }
        return null;
    }

    @Override
    public ParentDto addParent(UserIDRequest userIDRequest) {
        Parent parent = Parent.builder()
                .userID(userIDRequest.getUserID())
                .build();
        parentRepository.save(parent);
        return entityMapper.mapToParentDto(parent);
    }

    @Override
    public ParentDto getParentById(Long id) {
        return parentRepository.findById(id)
                .map(entityMapper::mapToParentDto)
                .orElse(null);
    }

    private void updateParentFromDto(Parent parent, ParentUpdateDto parentUpdateDto) {
        if (parentUpdateDto.getStudentsID() != null) {
            List<Student> students = parentUpdateDto.getStudentsID().stream()
                    .map(studentId -> studentRepository.findById(studentId)
                            .orElseThrow(() -> new IllegalArgumentException("Invalid student ID")))
                    .collect(Collectors.toList());
            parent.setStudents(students);
        } else {
            parent.setStudents(null);
        }
    }
}
