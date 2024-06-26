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
@Table(name = "qualifications")
public class Qualification extends IdGenerator{

    @Column(name = "area")
    private String area;

    @ManyToMany(fetch = FetchType.LAZY)
    private List<Teacher> teachers;
}
