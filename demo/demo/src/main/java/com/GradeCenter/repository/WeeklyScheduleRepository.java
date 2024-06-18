package com.GradeCenter.repository;

import com.GradeCenter.entity.Teacher;
import com.GradeCenter.entity.WeeklySchedule;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface WeeklyScheduleRepository  extends JpaRepository<WeeklySchedule, Long> {
    List<WeeklySchedule> findByCourseId(Long courseId);
    List<WeeklySchedule> findBySchoolClassId(Long studyGroupId);
    List<WeeklySchedule> findBySchoolClass_Students_Id(Long studentId);
}
