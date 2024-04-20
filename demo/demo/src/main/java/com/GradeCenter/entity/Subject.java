package com.GradeCenter.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import lombok.*;

import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED) // Make no-args constructor protected
@AllArgsConstructor(access = AccessLevel.PRIVATE) // Make all-args constructor private
@Builder
public class Subject extends IdGenerator{

    @Column
    private String name;

    @OneToMany(mappedBy = "course")
    private List<Absence> absences;

    @OneToMany(mappedBy = "course")
    private List<Grade> grades;
}
