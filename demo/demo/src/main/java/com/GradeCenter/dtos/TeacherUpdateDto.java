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
public class TeacherUpdateDto {

    private List<Long> courseIds;
    private Long schoolId;
    private List<Long> qualificationsIds;
}
