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
@Table(name = "roles")
public class Role{

    @Id
    @Column(name = "Credentials ID")
    @ManyToMany(fetch = FetchType.LAZY)
    private List<Credentials> credentialsList;

    @Id
    @Column(name = "Role")
    private String role;
}
