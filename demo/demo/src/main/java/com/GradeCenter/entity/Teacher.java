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
@Table(name = "teachers")
public class Teacher extends IdGenerator {

    @Column(name = "user_id", nullable = false, unique = true)
    private String userID;

    @ManyToMany(mappedBy = "teachers")
    private List<Course> courses;

    @ManyToOne(fetch = FetchType.LAZY)
    private School school;

    @ManyToMany(mappedBy = "teachers")
    private List<Qualification> qualifications;
}
