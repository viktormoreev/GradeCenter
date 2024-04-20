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
public class School extends IdGenerator{

    @Column
    private String name;

    @Column
    private String address;

    @OneToOne(mappedBy = "school")
    private Director director;

    @OneToMany(fetch = FetchType.LAZY)
    private List<Teacher> teachers;
}
