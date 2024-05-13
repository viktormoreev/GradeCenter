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
@Table(name = "qualifications")
public class Qualification extends IdGenerator{

    @Column(name = "area")
    private String area;

    @ManyToMany(fetch = FetchType.LAZY)
    private List<Teacher> teachers;
}
