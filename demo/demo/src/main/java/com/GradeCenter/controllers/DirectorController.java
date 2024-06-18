package com.GradeCenter.controllers;

import com.GradeCenter.dtos.DirectorDto;
import com.GradeCenter.dtos.DirectorUpdateDto;
import com.GradeCenter.dtos.SchoolDto;
import com.GradeCenter.dtos.UserIDRequest;
import com.GradeCenter.service.DirectorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/directors")
public class DirectorController {

    @Autowired
    private DirectorService directorService;

    @GetMapping
    @PreAuthorize("hasRole('admin')")
    public List<DirectorDto> getAllDirectors() {
        return directorService.getAllDirectors();
    }

    @GetMapping("/me")
    @PreAuthorize("hasRole('director')")
    public ResponseEntity<DirectorDto> getPersonalDirector() {
        Jwt jwt = (Jwt) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String userId = jwt.getClaimAsString("sub");

        DirectorDto director = directorService.getDirectorByUId(userId);
        if (director == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(director);
    }

    @GetMapping("/id={id}")
    @PreAuthorize("hasRole('admin')")
    public ResponseEntity<DirectorDto> getDirectorById(@PathVariable("id") Long id) {
        DirectorDto director = directorService.getDirectorById(id);
        if (director == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(director);
    }

    @GetMapping("/uid={id}")
    @PreAuthorize("hasRole('admin')")
    public ResponseEntity<DirectorDto> getDirectorByUId(@PathVariable("id") String userID) {
        DirectorDto director = directorService.getDirectorByUId(userID);
        if (director == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(director);
    }

    @PostMapping
    @PreAuthorize("hasRole('admin')")
    public ResponseEntity<DirectorDto> addDirector(@RequestBody UserIDRequest userIDRequest) {
        DirectorDto director = directorService.addDirector(userIDRequest);
        return ResponseEntity.ok(director);
    }

    @PostMapping("/directorId={directorId}/schoolId={schoolId}")
    @PreAuthorize("hasRole('admin')")
    public ResponseEntity<SchoolDto> addDirectorToSchool(@PathVariable("directorId") Long directorId, @PathVariable("schoolId") Long schoolId) {
        SchoolDto schoolDto = directorService.addDirectorToSchool(directorId,schoolId);
        return ResponseEntity.ok(schoolDto);
    }

    // Fire a director from a school
    @PatchMapping("/schoolId={schoolId}")
    @PreAuthorize("hasRole('admin')")
    public ResponseEntity<SchoolDto> fireDirectorFromSchool(@PathVariable("schoolId") Long schoolId) {
        SchoolDto schoolDto = directorService.removeDirectorFromSchool(schoolId);
        return ResponseEntity.ok(schoolDto);
    }


    @DeleteMapping("/id={id}")
    @PreAuthorize("hasRole('admin')")
    public ResponseEntity<String> deleteDirectorId(@PathVariable("id") Long id) {
        boolean isDeleted = directorService.deleteDirectorID(id);
        if (isDeleted) {
            return ResponseEntity.ok("Director deleted successfully");
        } else {
            return ResponseEntity.status(404).body("Failed to delete director");
        }
    }

    @DeleteMapping("/uid={id}")
    @PreAuthorize("hasRole('admin')")
    public ResponseEntity<String> deleteDirectorUID(@PathVariable("id") String userID) {
        boolean isDeleted = directorService.deleteDirectorUID(userID);
        if (isDeleted) {
            return ResponseEntity.ok("Director deleted successfully");
        } else {
            return ResponseEntity.status(404).body("Failed to delete director");
        }
    }

    @PutMapping("/id={id}")
    @PreAuthorize("hasRole('admin')")
    public ResponseEntity<DirectorDto> updateDirectorID(@PathVariable("id") Long id, @RequestBody DirectorUpdateDto directorUpdateDto) {
        DirectorDto updatedDirector = directorService.updateDirectorID(id, directorUpdateDto);
        if (updatedDirector == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(updatedDirector);
    }

    @PutMapping("/uid={id}")
    @PreAuthorize("hasRole('admin')")
    public ResponseEntity<DirectorDto> updateDirectorUID(@PathVariable("id") String userID, @RequestBody DirectorUpdateDto directorUpdateDto) {
        DirectorDto updatedDirector = directorService.updateDirectorUID(userID, directorUpdateDto);
        if (updatedDirector == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(updatedDirector);
    }
}
