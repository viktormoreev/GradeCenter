package com.GradeCenter.service;

import com.GradeCenter.dtos.DirectorDto;
import com.GradeCenter.entity.Director;

import java.util.List;

public interface DirectorService {

    List<DirectorDto> fetchDirectorList();

    Director saveDirector(Director director);

    void deleteDirectorById(Long directorId);
    DirectorDto fetchDirectorById(Long directorId);

    DirectorDto updateDirectorById(Long directorId);
}
