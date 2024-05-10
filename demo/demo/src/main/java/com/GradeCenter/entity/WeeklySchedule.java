package com.GradeCenter.entity;

import com.GradeCenter.enums.Weekday;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED) // Make no-args constructor protected
@AllArgsConstructor(access = AccessLevel.PRIVATE) // Make all-args constructor private
@Builder
@Table(name = "weekly_schedule")
public class WeeklySchedule extends IdGenerator{

    @ManyToOne(fetch = FetchType.LAZY)
    private StudyGroup schoolClass;

    @ManyToOne(fetch = FetchType.LAZY)
    private Course course;

    @Enumerated
    private Weekday day;

    @ManyToOne(fetch = FetchType.LAZY)
    private SchoolHour startHour;

}
