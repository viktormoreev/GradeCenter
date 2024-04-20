package com.GradeCenter.entity;

import com.GradeCenter.enums.Weekday;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED) // Make no-args constructor protected
@AllArgsConstructor(access = AccessLevel.PRIVATE) // Make all-args constructor private
@Builder
public class SchoolProgram extends IdGenerator{

    @ManyToOne(fetch = FetchType.LAZY)
    private SchoolClass schoolClass;

    @ManyToOne(fetch = FetchType.LAZY)
    private Subject subject;

    @Enumerated
    private Weekday day;

    @ManyToOne(fetch = FetchType.LAZY)
    private SchoolHour startHour;

}
