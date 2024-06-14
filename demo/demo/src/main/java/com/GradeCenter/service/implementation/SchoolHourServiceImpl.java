package com.GradeCenter.service.implementation;

import com.GradeCenter.entity.SchoolHour;
import com.GradeCenter.dtos.SchoolHourCreateRequest;
import com.GradeCenter.repository.SchoolHourRepository;
import com.GradeCenter.service.SchoolHourService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class SchoolHourServiceImpl implements SchoolHourService {

    @Autowired
    private SchoolHourRepository schoolHourRepository;

    @Override
    public List<SchoolHour> findAll() {
        return schoolHourRepository.findAll();
    }

    @Override
    public SchoolHour findById(Long id) {
        Optional<SchoolHour> schoolHour = schoolHourRepository.findById(id);
        if (!schoolHour.isPresent()) {
            return null;
        }
        return schoolHour.get();
    }

    @Override
    public SchoolHour save(SchoolHourCreateRequest schoolHourCreateRequest) {
        SchoolHour schoolHour = SchoolHour.builder()
                .hour(schoolHourCreateRequest.getHour())
                .minute(schoolHourCreateRequest.getMinute())
                .build();
        return schoolHourRepository.save(schoolHour);
    }

    @Override
    public SchoolHour update(Long id, SchoolHourCreateRequest schoolHourDetails) {
        SchoolHour schoolHour = findById(id);
        schoolHour.setHour(schoolHourDetails.getHour());
        schoolHour.setMinute(schoolHourDetails.getMinute());
        return schoolHourRepository.save(schoolHour);
    }

    @Override
    public boolean deleteById(Long id) {
        return schoolHourRepository.findById(id).map(schoolHour -> {
            schoolHourRepository.delete(schoolHour);
            return true;
        }).orElse(false);
    }
}