package com.GradeCenter.repository;

import com.GradeCenter.entity.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface StudentRepository extends JpaRepository<Student, Long> {
    Optional<Student> findByUserID(String userID);

    List<Student> findAllBySchool(Long id);
}
