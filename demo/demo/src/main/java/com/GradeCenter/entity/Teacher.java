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
public class Teacher {
    @Id
    private Long credentialsId;

    @ManyToMany(mappedBy = "teachers")
    private List<Course> courses;

    @ManyToOne(fetch = FetchType.LAZY)
    private School school;
}
