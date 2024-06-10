package com.GradeCenter.service.implementation;

import com.GradeCenter.dtos.ParentDto;
import com.GradeCenter.dtos.UserIDRequest;
import com.GradeCenter.repository.ParentRepository;
import com.GradeCenter.service.ParentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ParentServiceImpl implements ParentService {

    @Autowired
    ParentRepository parentRepository;

    @Override
    public List<ParentDto> getAllParents() {

        // Implement the method to get all parents and map them to ParentDto

        return null;
    }

    @Override
    public Optional<ParentDto> getParentByUId(String uid) {

        // Implement the method to get a parent by UserID and map it to ParentDto

        return Optional.empty();
    }

    @Override
    public ResponseEntity<String> deleteParentUID(String userID) {

        // Implement the method to delete a parent by UserID

        return ResponseEntity.ok("Successfully deleted parent with UserID: " + userID + " from the database.");
    }

    @Override
    public ResponseEntity<String> deleteParentID(Long ID) {

        // Implement the method to delete a parent by ID

        return ResponseEntity.ok("Successfully deleted parent with ID: " + ID + " from the database.");
    }

    @Override
    public Optional<ParentDto> updateParentID(Long id, ParentDto parentDto) {

        // Implement the method to update a parent by ID

        return null;
    }

    @Override
    public Optional<ParentDto> updateParentUID(String userID, ParentDto parentDto) {

        // Implement the method to update a parent by UserID

        return null;
    }

    @Override
    public ParentDto addParent(UserIDRequest userIDRequest) {

        // Implement the method to add a parent to the database

        return null;
    }

    @Override
    public Optional<ParentDto> getParentById(Long id) {

        // Implement the method to get a parent by ID and map it to ParentDto

        return Optional.empty();
    }
}