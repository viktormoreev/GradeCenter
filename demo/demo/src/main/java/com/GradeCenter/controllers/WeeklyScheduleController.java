package com.GradeCenter.controllers;

import com.GradeCenter.dtos.CreateWeeklyScheduleDto;
import com.GradeCenter.dtos.WeeklyScheduleDto;
import com.GradeCenter.service.WeeklyScheduleService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("schedule")
public class WeeklyScheduleController {
    @Autowired
    private WeeklyScheduleService weeklyScheduleService;

    @PostMapping
    public ResponseEntity<WeeklyScheduleDto> addWeeklyScheduleHour(@Valid @RequestBody CreateWeeklyScheduleDto createWeeklyScheduleDto) {
        return ResponseEntity.ok(weeklyScheduleService.saveWeeklySchedule(createWeeklyScheduleDto));
    }

    @GetMapping
    public ResponseEntity<List<WeeklyScheduleDto>> fetchAllWeeklySchedule() {
        return ResponseEntity.ok(weeklyScheduleService.fetchAllWeeklySchedule());
    }

    @GetMapping("/courseId={id}")
    public ResponseEntity<List<WeeklyScheduleDto>> fetchWeeklyScheduleByCourseId(@PathVariable Long id) {
        return ResponseEntity.ok(weeklyScheduleService.fetchWeeklyScheduleByCourseId(id));
    }

    @GetMapping("/studyGroup={id}")
    public ResponseEntity<List<WeeklyScheduleDto>> fetchWeeklyScheduleByStudyGroupId(@PathVariable Long id) {
        return ResponseEntity.ok(weeklyScheduleService.fetchWeeklyScheduleByStudyGroupId(id));
    }

    @PutMapping("/updateId={id}")
    public ResponseEntity<WeeklyScheduleDto> updateWeeklyScheduleById(@PathVariable Long id, @RequestBody CreateWeeklyScheduleDto createWeeklyScheduleDto) {
        return ResponseEntity.ok(weeklyScheduleService.updateWeeklyScheduleById(id, createWeeklyScheduleDto));
    }

    @DeleteMapping("/deleteId={id}")
    public ResponseEntity<String> deleteWeeklyScheduleById(@PathVariable Long id) {
        weeklyScheduleService.deleteWeeklyScheduleById(id);
        return ResponseEntity.ok("Successfully deleted");
    }

}
