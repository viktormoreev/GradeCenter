package com.GradeCenter.service.implementation;

import com.GradeCenter.dtos.DirectorDto;
import com.GradeCenter.dtos.DirectorUpdateDto;
import com.GradeCenter.dtos.SchoolDto;
import com.GradeCenter.dtos.UserIDRequest;
import com.GradeCenter.entity.Director;
import com.GradeCenter.entity.School;
import com.GradeCenter.mapper.EntityMapper;
import com.GradeCenter.repository.DirectorRepository;
import com.GradeCenter.repository.SchoolRepository;
import com.GradeCenter.service.DirectorService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class DirectorServiceImpl implements DirectorService {

    @Autowired
    private DirectorRepository directorRepository;

    @Autowired
    private SchoolRepository schoolRepository;

    @Autowired
    private EntityMapper entityMapper;

    @Override
    public List<DirectorDto> getAllDirectors() {
        return entityMapper.mapToDirectorListDto(directorRepository.findAll());
    }

    @Override
    public DirectorDto getDirectorByUId(String uid) {
        return directorRepository.findByUserID(uid)
                .map(entityMapper::mapToDirectorDto)
                .orElse(null);
    }

    @Override
    public boolean deleteDirectorUID(String userID) {
        Optional<Director> director = directorRepository.findByUserID(userID);
        if (director.isPresent()) {
            directorRepository.delete(director.get());
            return true;
        }
        return false;
    }

    @Override
    public boolean deleteDirectorID(Long ID) {
        if (directorRepository.existsById(ID)) {
            directorRepository.deleteById(ID);
            return true;
        }
        return false;
    }

    @Override
    public DirectorDto updateDirectorID(Long id, DirectorUpdateDto directorUpdateDto) {
        Optional<Director> existingDirectorOpt = directorRepository.findById(id);
        if (existingDirectorOpt.isPresent()) {
            Director existingDirector = existingDirectorOpt.get();
            updateDirectorFromDto(existingDirector, directorUpdateDto);
            directorRepository.save(existingDirector);
            return entityMapper.mapToDirectorDto(existingDirector);
        }
        return null;
    }

    @Override
    public DirectorDto updateDirectorUID(String userID, DirectorUpdateDto directorUpdateDto) {
        Optional<Director> existingDirectorOpt = directorRepository.findByUserID(userID);
        if (existingDirectorOpt.isPresent()) {
            Director existingDirector = existingDirectorOpt.get();
            updateDirectorFromDto(existingDirector, directorUpdateDto);
            directorRepository.save(existingDirector);
            return entityMapper.mapToDirectorDto(existingDirector);
        }
        return null;
    }

    @Override
    public SchoolDto removeDirectorFromSchool(Long schoolId) {
        Optional<School> optionalSchool = schoolRepository.findById(schoolId);
        if(optionalSchool.isPresent()){
            School school = optionalSchool.get();
            school.setDirector(null);
            schoolRepository.save(school);
            return entityMapper.mapToSchoolDto(schoolRepository.save(school));
        }
        else throw new EntityNotFoundException("School is not found exception");
    }

    @Override
    public SchoolDto addDirectorToSchool(Long directorId, Long schoolId) {
        Optional<Director> optionalDirector = directorRepository.findById(directorId);
        if(optionalDirector.isPresent()){
            Optional<School> optionalSchool = schoolRepository.findById(schoolId);
            if(optionalSchool.isPresent()){
                School school = optionalSchool.get();
                school.setDirector(optionalDirector.get());
                schoolRepository.save(school);
                return entityMapper.mapToSchoolDto(schoolRepository.save(school));
            }
            else throw new EntityNotFoundException("School is not found exception");
        }
        else throw new EntityNotFoundException("Director is not found exception");
    }

    @Override
    public DirectorDto addDirector(UserIDRequest userIDRequest) {
        Director director = Director.builder()
                .userID(userIDRequest.getUserID())
                .build();
        directorRepository.save(director);
        return entityMapper.mapToDirectorDto(director);
    }

    @Override
    public DirectorDto getDirectorById(Long id) {
        return directorRepository.findById(id)
                .map(entityMapper::mapToDirectorDto)
                .orElse(null);
    }

    private void updateDirectorFromDto(Director director, DirectorUpdateDto directorUpdateDto) {
        if (directorUpdateDto.getSchoolID() != null) {
            School school = schoolRepository.findById(directorUpdateDto.getSchoolID())
                    .orElseThrow(() -> new IllegalArgumentException("Invalid school ID"));
            director.setSchool(school);
        } else {
            director.setSchool(null);
        }
    }
}
