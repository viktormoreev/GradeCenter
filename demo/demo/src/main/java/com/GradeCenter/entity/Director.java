package com.GradeCenter.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "directors")
public class Director extends IdGenerator{

    @Column(name = "user_id", nullable = false, unique = true)
    private String userID;

    @OneToOne(mappedBy = "director")
    private School school;

}
