package com.GradeCenter.entity;

import jakarta.persistence.Column;
import jakarta.persistence.FetchType;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MappedSuperclass;
import lombok.*;


@MappedSuperclass
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED) // Make no-args constructor protected
@AllArgsConstructor(access = AccessLevel.PRIVATE) // Make all-args constructor private
@Builder
public class User extends IdGenerator{

    @Column
    private String name;

    @Column(unique = true)
    private String username;

    @Column
    private String password;

    @ManyToOne(fetch = FetchType.LAZY)
    private Role role;

}
