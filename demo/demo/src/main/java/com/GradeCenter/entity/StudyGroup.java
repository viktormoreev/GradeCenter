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
@Table(name = "study_groups")
public class StudyGroup extends IdGenerator {

    @Column
    private String name;

    @ManyToOne(fetch = FetchType.LAZY)
    private School school;

    @ManyToMany(mappedBy = "studyGroups")
    private List<Course> courses;

    @OneToMany(mappedBy = "classes")
    private List<Student> students;

}
