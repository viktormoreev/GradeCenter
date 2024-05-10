package com.GradeCenter.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SubjectDto {
    private String name;

    //private List<AbsenceDto> absences;//Waiting for AbsenceDto functionality
    //private List<GradeDto> grades;//Waiting for GradeDto functionality

}
