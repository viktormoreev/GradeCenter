package com.GradeCenter.service.implementation;

import com.GradeCenter.dtos.CreateWeeklyScheduleDto;
import com.GradeCenter.dtos.WeeklyScheduleDto;
import com.GradeCenter.entity.Course;
import com.GradeCenter.entity.SchoolHour;
import com.GradeCenter.entity.StudyGroup;
import com.GradeCenter.entity.WeeklySchedule;
import com.GradeCenter.mapper.EntityMapper;
import com.GradeCenter.repository.CourseRepository;
import com.GradeCenter.repository.SchoolHourRepository;
import com.GradeCenter.repository.StudyGroupRepository;
import com.GradeCenter.repository.WeeklyScheduleRepository;
import com.GradeCenter.service.WeeklyScheduleService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class WeeklyScheduleServiceImpl implements WeeklyScheduleService {

    @Autowired
    private EntityMapper entityMapper;
    @Autowired
    private CourseRepository courseRepository;
    @Autowired
    private StudyGroupRepository studyGroupRepository;
    @Autowired
    private SchoolHourRepository schoolHourRepository;
    @Autowired
    private WeeklyScheduleRepository weeklyScheduleRepository;


    @Override
    public WeeklyScheduleDto saveWeeklySchedule(CreateWeeklyScheduleDto createWeeklyScheduleDto) {
        Optional<Course> optionalCourse = courseRepository.findById(createWeeklyScheduleDto.getCourseId());
        if (optionalCourse.isPresent()) {
            Optional<StudyGroup> optionalStudyGroup = studyGroupRepository.findById(createWeeklyScheduleDto.getStudyGroupId());
            if (optionalStudyGroup.isPresent()) {
                Optional<SchoolHour> optionalSchoolHour = schoolHourRepository.findById(createWeeklyScheduleDto.getStartHourId());
                if (optionalSchoolHour.isPresent()) {
                    WeeklySchedule weeklySchedule = WeeklySchedule.builder()
                            .day(createWeeklyScheduleDto.getWeekday())
                            .course(optionalCourse.get())
                            .schoolClass(optionalStudyGroup.get())
                            .startHour(optionalSchoolHour.get())
                            .build();
                    return entityMapper.mapToWeeklyScheduleDto(weeklyScheduleRepository.save(weeklySchedule));
                } else throw new EntityNotFoundException("School hour is not found");
            } else throw new EntityNotFoundException("Study group is not found");
        } else throw new EntityNotFoundException("Course is not found");
    }

    @Override
    public List<WeeklyScheduleDto> fetchAllWeeklySchedule() {
        return entityMapper.mapToWeeklyScheduleListDto(weeklyScheduleRepository.findAll());
    }

    @Override
    public List<WeeklyScheduleDto> fetchWeeklyScheduleByCourseId(Long id) {
        return entityMapper.mapToWeeklyScheduleListDto(weeklyScheduleRepository.findByCourseId(id));
    }

    @Override
    public List<WeeklyScheduleDto> fetchWeeklyScheduleByStudyGroupId(Long id) {
        return entityMapper.mapToWeeklyScheduleListDto(weeklyScheduleRepository.findBySchoolClassId(id));
    }

    @Override
    public WeeklyScheduleDto updateWeeklyScheduleById(Long id, CreateWeeklyScheduleDto createWeeklyScheduleDto) {
        Optional<WeeklySchedule> optionalWeeklySchedule = weeklyScheduleRepository.findById(id);
        if (optionalWeeklySchedule.isPresent()) {
            WeeklySchedule weeklySchedule = optionalWeeklySchedule.get();
            Optional<Course> optionalCourse = courseRepository.findById(createWeeklyScheduleDto.getCourseId());
            if (optionalCourse.isPresent()) {
                weeklySchedule.setCourse(optionalCourse.get());
            }
            Optional<StudyGroup> optionalStudyGroup = studyGroupRepository.findById(createWeeklyScheduleDto.getStudyGroupId());
            if (optionalStudyGroup.isPresent()) {
                weeklySchedule.setSchoolClass(optionalStudyGroup.get());
            }
            Optional<SchoolHour> optionalSchoolHour = schoolHourRepository.findById(createWeeklyScheduleDto.getStartHourId());
            if (optionalSchoolHour.isPresent()) {
                weeklySchedule.setStartHour(optionalSchoolHour.get());
            }
            if (!createWeeklyScheduleDto.getWeekday().toString().isEmpty()) {
                weeklySchedule.setDay(createWeeklyScheduleDto.getWeekday());
            }
            return entityMapper.mapToWeeklyScheduleDto(weeklyScheduleRepository.save(weeklySchedule));
        } else throw new EntityNotFoundException("Weekly schedule is not found");
    }

    @Override
    public void deleteWeeklyScheduleById(Long id) {
        weeklyScheduleRepository.deleteById(id);
    }
}
