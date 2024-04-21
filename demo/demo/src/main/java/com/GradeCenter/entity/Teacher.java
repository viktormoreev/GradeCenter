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
public class Teacher extends IdGenerator {

    @ManyToMany(mappedBy = "teachers")
    private List<Subject> subjects;

    @ManyToOne(fetch = FetchType.LAZY)
    private School school;
}
