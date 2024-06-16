package com.GradeCenter.repository;

import com.GradeCenter.entity.Grade;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface GradeRepository extends JpaRepository<Grade, Long> {
    Optional<List<Grade>> findByStudentId(Long studentId);
}