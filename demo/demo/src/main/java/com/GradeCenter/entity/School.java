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
@Table(name = "schools")
public class School extends IdGenerator{

    @Column(unique = true)
    private String name;

    @Column
    private String address;

    @OneToOne(fetch = FetchType.LAZY)
    private Director director;

    @OneToMany(mappedBy = "school")
    private List<Teacher> teachers;

    @OneToMany(mappedBy = "school")
    private List<StudyGroup> studyGroups;

}
