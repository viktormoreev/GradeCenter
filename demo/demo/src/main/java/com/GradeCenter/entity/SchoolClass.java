package com.GradeCenter.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED) // Make no-args constructor protected
@AllArgsConstructor(access = AccessLevel.PRIVATE) // Make all-args constructor private
@Builder
public class SchoolClass extends IdGenerator {

    @Column
    private String name;

    @ManyToOne(fetch = FetchType.LAZY)
    private School school;

    @OneToMany(mappedBy = "schoolClass")
    private List<Subject> subjects;

}
