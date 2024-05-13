package com.GradeCenter.entity;

import jakarta.persistence.*;
import lombok.*;


@Entity
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED) // Make no-args constructor protected
@AllArgsConstructor(access = AccessLevel.PRIVATE) // Make all-args constructor private
@Builder
@Table(name = "credentials")
public class Credentials extends IdGenerator{
    @Column
    private String name;
    @Column(unique = true)
    private String username;
    @Column
    private String password;
}
