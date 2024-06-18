package com.GradeCenter.service;

import com.GradeCenter.dtos.DirectorDto;
import com.GradeCenter.dtos.DirectorUpdateDto;
import com.GradeCenter.dtos.SchoolDto;
import com.GradeCenter.dtos.UserIDRequest;

import java.util.List;

public interface DirectorService {
    List<DirectorDto> getAllDirectors();

    DirectorDto addDirector(UserIDRequest userIDRequest);

    DirectorDto getDirectorById(Long id);

    DirectorDto getDirectorByUId(String uid);

    boolean deleteDirectorUID(String userID);

    boolean deleteDirectorID(Long ID);

    DirectorDto updateDirectorID(Long id, DirectorUpdateDto directorUpdateDto);

    DirectorDto updateDirectorUID(String userID, DirectorUpdateDto directorUpdateDto);

    SchoolDto addDirectorToSchool(Long directorId, Long schoolId);

    SchoolDto removeDirectorFromSchool(Long schoolId);
}
