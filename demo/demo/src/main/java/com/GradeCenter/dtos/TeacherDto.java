package com.GradeCenter.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class TeacherDto {
    private String userID;
    private Long id;
    private List<Long> courseIds;
    private String username;
    private Long schoolId;
    private List<Long> qualificationsIds;
}
