package com.GradeCenter.repository;

import com.GradeCenter.entity.Parent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ParentRepository extends JpaRepository<Parent, Long>{
    Optional<Parent> findByUserID(String userID);
}
