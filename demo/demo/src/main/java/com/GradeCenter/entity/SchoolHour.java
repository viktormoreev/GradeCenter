package com.GradeCenter.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.*;

@Entity
@Getter
@Setter
@NoArgsConstructor // Make no-args constructor protected
@AllArgsConstructor // Make all-args constructor private
@Builder
@Table(name = "school_hours")
public class SchoolHour extends IdGenerator {

    @Column
    @PositiveOrZero
    @DecimalMax(value = "23")
    private int hour;

    @Column
    @PositiveOrZero
    @DecimalMax(value = "59")
    private int minute;
}
