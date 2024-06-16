package com.GradeCenter.repository;

import com.GradeCenter.entity.Course;
import com.GradeCenter.entity.CourseType;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CourseTypeRepository extends JpaRepository<CourseType,Long> {
}
