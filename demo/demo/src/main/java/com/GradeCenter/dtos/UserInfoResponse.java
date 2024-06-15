package com.GradeCenter.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class UserInfoResponse {

    private String username;
    private String userid;
    private String role;
}
