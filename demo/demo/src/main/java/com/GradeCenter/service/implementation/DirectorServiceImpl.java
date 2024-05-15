package com.GradeCenter.service.implementation;

import com.GradeCenter.dtos.DirectorDto;
import com.GradeCenter.entity.Director;
import com.GradeCenter.entity.Teacher;
import com.GradeCenter.mapper.EntityMapper;
import com.GradeCenter.repository.DirectorRepository;
import com.GradeCenter.service.DirectorService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class DirectorServiceImpl implements DirectorService {

    @Autowired
    private EntityMapper entityMapper;
    @Autowired
    private DirectorRepository directorRepository;

    @Override
    public List<DirectorDto> fetchDirectorList() {
        return entityMapper.mapToDirectorListDto(directorRepository.findAll());
    }

    @Override
    public Director saveDirector(Director director) {
        return directorRepository.save(director);
    }

    @Override
    public void deleteDirectorById(Long directorId) {
        directorRepository.deleteById(directorId);
    }

    @Override
    public DirectorDto fetchDirectorById(Long directorId) {
        Optional<Director> director = directorRepository.findById(directorId);
        if (director.isPresent()){
            return entityMapper.mapToDirectorDto(director.get());
        }else {
            throw new EntityNotFoundException("Director is not found!");
        }
    }

    @Override
    public DirectorDto updateDirectorById(Long directorId) {
        Optional<Director> director = directorRepository.findById(directorId);
        if (director.isPresent()){
            return entityMapper.mapToDirectorDto(directorRepository.save(director.get()));
        }else {
            throw new EntityNotFoundException("Teacher is not found!");
        }
    }
}
