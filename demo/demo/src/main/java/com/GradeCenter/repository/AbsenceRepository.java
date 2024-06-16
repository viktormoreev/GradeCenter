package com.GradeCenter.repository;

import com.GradeCenter.entity.Absence;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AbsenceRepository extends JpaRepository<Absence, Long> {
    Optional<List<Absence>> findByStudentId(long studentId);

}
