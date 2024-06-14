package com.GradeCenter.service.implementation;

import com.GradeCenter.dtos.AbsenceDto;
import com.GradeCenter.dtos.AbsenceStudentViewDto;
import com.GradeCenter.entity.Absence;
import com.GradeCenter.entity.Course;
import com.GradeCenter.entity.Student;
import com.GradeCenter.exceptions.CourseNotFoundException;
import com.GradeCenter.exceptions.StudentNotFoundException;
import com.GradeCenter.mapper.EntityMapper;
import com.GradeCenter.repository.AbsenceRepository;
import com.GradeCenter.repository.CourseRepository;
import com.GradeCenter.repository.StudentRepository;
import com.GradeCenter.service.AbsenceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class AbsenceServiceImpl implements AbsenceService {

    @Autowired
    EntityMapper entityMapper;

    @Autowired
    AbsenceRepository absenceRepository;

    @Autowired
    StudentRepository studentRepository;

    @Autowired
    CourseRepository courseRepository;

    @Override
    public List<AbsenceDto> getAllAbsences() {
        return entityMapper.mapToAbsenceListDto(absenceRepository.findAll());
    }

    @Override
    public List<AbsenceDto> getAbsencesByStudentId(long studentId) {
        return null;
    }

    @Override
    public AbsenceDto createAbsence(AbsenceDto absenceDto) throws StudentNotFoundException, CourseNotFoundException {
        Student student = studentRepository.findById(absenceDto.getStudentId())
                .orElseThrow(() -> new StudentNotFoundException("Student not found"));

        Course course = courseRepository.findById(absenceDto.getCourseId())
                .orElseThrow(() -> new CourseNotFoundException("Course not found"));

        Absence absence = entityMapper.mapToAbsenceEntity(absenceDto, student, course);

        Absence savedAbsence = absenceRepository.save(absence);

        return entityMapper.mapToAbsenceDto(savedAbsence);
    }

    @Override
    public List<AbsenceStudentViewDto> getPersonalStudentAbsences(String userId) throws StudentNotFoundException {
        Student student = studentRepository.findByUserID(userId)
                .orElseThrow(() -> new StudentNotFoundException("Student not found in database."));

        Optional<List<Absence>> optionalAbsences = absenceRepository.findByStudentId(student.getId());
        if (optionalAbsences.isPresent() && !optionalAbsences.get().isEmpty()) {
            return optionalAbsences.get()
                    .stream()
                    .map(entityMapper::mapToAbsenceStudentViewDto)
                    .collect(Collectors.toList());
        } else {
            return Collections.emptyList();
        }
    }

    @Override
    public List<AbsenceDto> getAllAbsencesByStudentIdForAdmin(long studentId) throws StudentNotFoundException {

        if(!studentRepository.existsById(studentId)){
            throw new StudentNotFoundException("Student not found in database.");
        }

        Optional<List<Absence>> optionalAbsences = absenceRepository.findByStudentId(studentId);
        if (optionalAbsences.isPresent() && !optionalAbsences.get().isEmpty()) {
            return optionalAbsences.get()
                    .stream()
                    .map(entityMapper::mapToAbsenceDto)
                    .collect(Collectors.toList());
        } else {
            return Collections.emptyList();
        }
    }

    @Override
    public boolean deleteAbsenceById(Long id) {
        if (absenceRepository.existsById(id)) {
            absenceRepository.deleteById(id);
            return true;
        } else {
            return false;
        }
    }
}
