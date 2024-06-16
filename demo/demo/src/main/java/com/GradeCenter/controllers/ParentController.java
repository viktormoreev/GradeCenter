package com.GradeCenter.controllers;

import com.GradeCenter.dtos.ParentDto;
import com.GradeCenter.dtos.ParentUpdateDto;
import com.GradeCenter.dtos.UserIDRequest;
import com.GradeCenter.service.ParentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/parents")
public class ParentController {

    @Autowired
    private ParentService parentService;

    @GetMapping
    @PreAuthorize("hasRole('admin')")
    public List<ParentDto> getAllParents() {
        return parentService.getAllParents();
    }

    @GetMapping("/me")
    @PreAuthorize("hasRole('parent')")
    public ResponseEntity<ParentDto> getPersonalParent() {
        Jwt jwt = (Jwt) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String userId = jwt.getClaimAsString("sub");

        ParentDto parent = parentService.getParentByUId(userId);
        if (parent == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(parent);
    }

    @GetMapping("/id={id}")
    @PreAuthorize("hasRole('admin')")
    public ResponseEntity<ParentDto> getParentById(@PathVariable("id") Long id) {
        ParentDto parent = parentService.getParentById(id);
        if (parent == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(parent);
    }

    @GetMapping("/uid={id}")
    @PreAuthorize("hasRole('admin')")
    public ResponseEntity<ParentDto> getParentByUId(@PathVariable("id") String userID) {
        ParentDto parent = parentService.getParentByUId(userID);
        if (parent == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(parent);
    }

    @PostMapping
    @PreAuthorize("hasRole('admin')")
    public ResponseEntity<ParentDto> addParent(@RequestBody UserIDRequest userIDRequest) {
        ParentDto parent = parentService.addParent(userIDRequest);
        return ResponseEntity.ok(parent);
    }

    @DeleteMapping("/id={id}")
    @PreAuthorize("hasRole('admin')")
    public ResponseEntity<String> deleteParentId(@PathVariable("id") Long id) {
        boolean isDeleted = parentService.deleteParentID(id);
        if (isDeleted) {
            return ResponseEntity.ok("Parent deleted successfully");
        } else {
            return ResponseEntity.status(404).body("Failed to delete parent");
        }
    }

    @DeleteMapping("/uid={id}")
    @PreAuthorize("hasRole('admin')")
    public ResponseEntity<String> deleteParentUID(@PathVariable("id") String userID) {
        boolean isDeleted = parentService.deleteParentUID(userID);
        if (isDeleted) {
            return ResponseEntity.ok("Parent deleted successfully");
        } else {
            return ResponseEntity.status(404).body("Failed to delete parent");
        }
    }

    @PutMapping("/id={id}")
    @PreAuthorize("hasRole('admin')")
    public ResponseEntity<ParentDto> updateParentID(@PathVariable("id") Long id, @RequestBody ParentUpdateDto parentUpdateDto) {
        ParentDto updatedParent = parentService.updateParentID(id, parentUpdateDto);
        if (updatedParent == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(updatedParent);
    }

    @PutMapping("/uid={id}")
    @PreAuthorize("hasRole('admin')")
    public ResponseEntity<ParentDto> updateParentUID(@PathVariable("id") String userID, @RequestBody ParentUpdateDto parentUpdateDto) {
        ParentDto updatedParent = parentService.updateParentUID(userID, parentUpdateDto);
        if (updatedParent == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(updatedParent);
    }
}
