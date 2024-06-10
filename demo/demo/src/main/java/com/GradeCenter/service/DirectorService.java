package com.GradeCenter.service;

import com.GradeCenter.dtos.DirectorDto;
import com.GradeCenter.dtos.UserIDRequest;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Optional;

public interface DirectorService {
    List<DirectorDto> getAllDirectors();

    Optional<DirectorDto> getDirectorByUId(String userId);

    Optional<DirectorDto> getDirectorById(Long id);

    DirectorDto addDirector(UserIDRequest userIDRequest);

    ResponseEntity<String> deleteDirectorID(Long id);

    ResponseEntity<String> deleteDirectorUID(String userID);

    Optional<DirectorDto> updateDirectorID(Long id, DirectorDto directorDto);

    Optional<DirectorDto> updateDirectorUID(String userID, DirectorDto directorDto);
}
