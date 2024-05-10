package com.GradeCenter.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED) // Make no-args constructor protected
@AllArgsConstructor(access = AccessLevel.PRIVATE) // Make all-args constructor private
@Builder
@Table(name = "roles")
public class Role{

    @Id
    @Column(name = "Credentials ID")
    private Long credentialsId;

    @Id
    @Column(name = "Role")
    private String role;
}
