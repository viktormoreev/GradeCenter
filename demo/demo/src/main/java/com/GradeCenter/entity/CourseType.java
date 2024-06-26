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
@Table(name = "course_types")
public class CourseType extends IdGenerator {
    private String name;

    @OneToMany(mappedBy = "courseType")
    private List<Course> courses;

    @ManyToMany(fetch = FetchType.LAZY)
    private List<Qualification> qualifications;

}
