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
@Table(name = "parents")
public class Parent extends IdGenerator{

    @Column(name = "user_id", nullable = false, unique = true)
    private String userID;

    @ManyToMany(fetch = FetchType.LAZY)
    private List<Student> students;

}
