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
@Table(name = "schools")
public class School extends IdGenerator{

    @Column
    private String name;

    @Column
    private String address;

    @OneToOne(fetch = FetchType.LAZY)
    private Director director;

    @OneToMany(mappedBy = "school")
    private List<Teacher> teachers;
}
