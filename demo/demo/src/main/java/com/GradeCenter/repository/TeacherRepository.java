package com.GradeCenter.repository;

import com.GradeCenter.entity.Teacher;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TeacherRepository extends JpaRepository<Teacher, Long> {
    Optional<Teacher> findByUserID(String uid);

    List<Teacher> findBySchoolId(Long id);
}