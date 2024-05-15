package com.GradeCenter.controllers;

import com.GradeCenter.dtos.DirectorDto;
import com.GradeCenter.entity.Director;
import com.GradeCenter.service.DirectorService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/directors")
public class DirectorController {

    @Autowired
    DirectorService directorService;

    @PostMapping
    public ResponseEntity<Director> addDirector(@Valid @RequestBody Director director){
        return ResponseEntity.ok(directorService.saveDirector(director));
    }

    @GetMapping
    public ResponseEntity<List<DirectorDto>> fetchDirectorList(){
        List<DirectorDto> directorList = directorService.fetchDirectorList();
        return ResponseEntity.ok(directorList);
    }

    @GetMapping("/{id}")
    public ResponseEntity<DirectorDto> fetchCourseById(@PathVariable("id") Long directorId){
        DirectorDto directorDto = directorService.fetchDirectorById(directorId);
        return ResponseEntity.ok(directorDto);
    }


}
