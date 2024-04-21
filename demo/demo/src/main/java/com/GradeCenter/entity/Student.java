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
@Table(name = "students")
public class Student extends IdGenerator {

    @ManyToMany(mappedBy = "students")
    private List<Parent> parents;

    @ManyToOne(fetch = FetchType.LAZY)
    private StudyGroup classes;

}
