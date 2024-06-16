package com.GradeCenter.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Service;

@Data
@AllArgsConstructor
public class KeycloakUser {

    private String username;

    private String password;
}
