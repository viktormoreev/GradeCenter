package com.GradeCenter.service.implementation;

import com.GradeCenter.dtos.DirectorDto;
import com.GradeCenter.dtos.UserIDRequest;
import com.GradeCenter.repository.DirectorRepository;
import com.GradeCenter.service.DirectorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class DirectorServiceImpl implements DirectorService {

    @Autowired
    DirectorRepository directorRepository;

    @Override
    public List<DirectorDto> getAllDirectors() {

        // Implement the method to get all directors and map them to DirectorDto

        return null;
    }

    @Override
    public Optional<DirectorDto> getDirectorByUId(String uid) {

        // Implement the method to get a director by UserID and map it to DirectorDto

        return Optional.empty();
    }

    @Override
    public ResponseEntity<String> deleteDirectorUID(String userID) {

        // Implement the method to delete a director by UserID

        return ResponseEntity.ok("Successfully deleted director with UserID: " + userID + " from the database.");
    }

    @Override
    public ResponseEntity<String> deleteDirectorID(Long ID) {

        // Implement the method to delete a director by ID

        return ResponseEntity.ok("Successfully deleted director with ID: " + ID + " from the database.");
    }

    @Override
    public Optional<DirectorDto> updateDirectorID(Long id, DirectorDto directorDto) {

        // Implement the method to update a director by ID

        return null;
    }

    @Override
    public Optional<DirectorDto> updateDirectorUID(String userID, DirectorDto directorDto) {

        // Implement the method to update a director by UserID

        return null;
    }

    @Override
    public DirectorDto addDirector(UserIDRequest userIDRequest) {

        // Implement the method to add a director to the database

        return null;
    }

    @Override
    public Optional<DirectorDto> getDirectorById(Long id) {

        // Implement the method to get a director by ID and map it to DirectorDto

        return Optional.empty();
    }
}