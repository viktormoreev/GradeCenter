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
@Table(name = "courses")
public class Course extends IdGenerator{

    @Column
    private String name;

    @OneToMany(mappedBy = "course")
    private List<Absence> absences;

    @OneToMany(mappedBy = "course")
    private List<Grade> grades;

    @ManyToMany(fetch = FetchType.LAZY)
    private List<Teacher> teachers;

    @ManyToMany(cascade = CascadeType.ALL)
    private List<StudyGroup> studyGroups;

    @ManyToOne(fetch = FetchType.LAZY)
    private CourseType courseType;

}
