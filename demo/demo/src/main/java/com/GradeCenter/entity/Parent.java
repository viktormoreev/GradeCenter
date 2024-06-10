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
@Table(name = "parents")
public class Parent extends IdGenerator{

    @Column
    private String userID;

    @ManyToMany(fetch = FetchType.LAZY)
    private List<Student> students;

}
