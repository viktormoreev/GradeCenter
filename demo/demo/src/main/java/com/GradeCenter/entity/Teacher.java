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
@Table(name = "teachers")
public class Teacher extends IdGenerator {

    @OneToOne
    private Credentials credentials;

    @ManyToMany(mappedBy = "teachers")
    private List<Course> courses;

    @ManyToOne(fetch = FetchType.LAZY)
    private School school;

    @ManyToMany(mappedBy = "teachers")
    private List<Qualification> qualifications;
}
