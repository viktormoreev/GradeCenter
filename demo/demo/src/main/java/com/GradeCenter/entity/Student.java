package com.GradeCenter.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "students")
public class Student extends IdGenerator{

    @Column(name = "user_id", nullable = false, unique = true)
    private String userID;

    @ManyToMany(mappedBy = "students")
    private List<Parent> parents;

    @ManyToOne(fetch = FetchType.LAZY)
    private StudyGroup classes;

}
