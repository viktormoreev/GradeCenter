package com.GradeCenter.controllers;

import com.GradeCenter.dtos.ParentDto;
import com.GradeCenter.dtos.UserIDRequest;
import com.GradeCenter.service.ParentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

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

        Optional<ParentDto> parent = parentService.getParentByUId(userId);
        if (parent.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return parent.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/id={id}")
    @PreAuthorize("hasRole('admin')")
    public ResponseEntity<ParentDto> getParentById(@PathVariable("id") Long id) {
        Optional<ParentDto> parent = parentService.getParentById(id);
        if (parent.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return parent.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/uid={id}")
    @PreAuthorize("hasRole('admin')")
    public ResponseEntity<ParentDto> getParentByUId(@PathVariable("id") String userID) {
        Optional<ParentDto> parent = parentService.getParentByUId(userID);
        if (parent.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return parent.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
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
        ResponseEntity<String> response = parentService.deleteParentID(id);
        if (response.getStatusCode().is2xxSuccessful()) {
            return ResponseEntity.ok("Parent deleted successfully");
        } else {
            return ResponseEntity.status(response.getStatusCode()).body("Failed to delete parent");
        }
    }

    @DeleteMapping("/uid={id}")
    @PreAuthorize("hasRole('admin')")
    public ResponseEntity<String> deleteParentUID(@PathVariable("id") String userID) {
        ResponseEntity<String> response = parentService.deleteParentUID(userID);
        if (response.getStatusCode().is2xxSuccessful()) {
            return ResponseEntity.ok("Parent deleted successfully");
        } else {
            return ResponseEntity.status(response.getStatusCode()).body("Failed to delete parent");
        }
    }

    @PutMapping("/id={id}")
    @PreAuthorize("hasRole('admin')")
    public ResponseEntity<ParentDto> updateParentID(@PathVariable("id") Long id, @RequestBody ParentDto parentDto) {
        Optional<ParentDto> response = parentService.updateParentID(id, parentDto);
        if (response.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(parentDto);
    }

    @PutMapping("/uid={id}")
    @PreAuthorize("hasRole('admin')")
    public ResponseEntity<ParentDto> updateParentUID(@PathVariable("id") String userID, @RequestBody ParentDto parentDto) {
        Optional<ParentDto> response = parentService.updateParentUID(userID, parentDto);
        if (response.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(parentDto);
    }
}