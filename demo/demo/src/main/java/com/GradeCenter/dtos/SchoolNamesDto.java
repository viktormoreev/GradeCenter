package com.GradeCenter.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SchoolNamesDto {

    private Long id;
    private String name;
    private String address;
    private String directorName;
    private List<String> teachersNames;
}
