package com.GradeCenter.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED) // Make no-args constructor protected
@AllArgsConstructor(access = AccessLevel.PRIVATE) // Make all-args constructor private
@Builder
@Table(name = "directors")
public class Director extends IdGenerator{

    @OneToOne
    private Credentials credentials;

    @OneToOne(mappedBy = "director")
    private School school;

}
