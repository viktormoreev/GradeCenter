package com.GradeCenter.controllers;

import com.GradeCenter.dtos.DirectorDto;
import com.GradeCenter.dtos.UserIDRequest;
import com.GradeCenter.service.DirectorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

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

        Optional<DirectorDto> director = directorService.getDirectorByUId(userId);
        if (director.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return director.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/id={id}")
    @PreAuthorize("hasRole('admin')")
    public ResponseEntity<DirectorDto> getDirectorById(@PathVariable("id") Long id) {
        Optional<DirectorDto> director = directorService.getDirectorById(id);
        if (director.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return director.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/uid={id}")
    @PreAuthorize("hasRole('admin')")
    public ResponseEntity<DirectorDto> getDirectorByUId(@PathVariable("id") String userID) {
        Optional<DirectorDto> director = directorService.getDirectorByUId(userID);
        if (director.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return director.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    @PreAuthorize("hasRole('admin')")
    public ResponseEntity<DirectorDto> addDirector(@RequestBody UserIDRequest userIDRequest) {
        DirectorDto director = directorService.addDirector(userIDRequest);
        return ResponseEntity.ok(director);
    }

    @DeleteMapping("/id={id}")
    @PreAuthorize("hasRole('admin')")
    public ResponseEntity<String> deleteDirectorId(@PathVariable("id") Long id) {
        ResponseEntity<String> response = directorService.deleteDirectorID(id);
        if (response.getStatusCode().is2xxSuccessful()) {
            return ResponseEntity.ok("Director deleted successfully");
        } else {
            return ResponseEntity.status(response.getStatusCode()).body("Failed to delete director");
        }
    }

    @DeleteMapping("/uid={id}")
    @PreAuthorize("hasRole('admin')")
    public ResponseEntity<String> deleteDirectorUID(@PathVariable("id") String userID) {
        ResponseEntity<String> response = directorService.deleteDirectorUID(userID);
        if (response.getStatusCode().is2xxSuccessful()) {
            return ResponseEntity.ok("Director deleted successfully");
        } else {
            return ResponseEntity.status(response.getStatusCode()).body("Failed to delete director");
        }
    }

    @PutMapping("/id={id}")
    @PreAuthorize("hasRole('admin')")
    public ResponseEntity<DirectorDto> updateDirectorID(@PathVariable("id") Long id, @RequestBody DirectorDto directorDto) {
        Optional<DirectorDto> response = directorService.updateDirectorID(id, directorDto);
        if (response.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(directorDto);
    }

    @PutMapping("/uid={id}")
    @PreAuthorize("hasRole('admin')")
    public ResponseEntity<DirectorDto> updateDirectorUID(@PathVariable("id") String userID, @RequestBody DirectorDto directorDto) {
        Optional<DirectorDto> response = directorService.updateDirectorUID(userID, directorDto);
        if (response.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(directorDto);
    }
}