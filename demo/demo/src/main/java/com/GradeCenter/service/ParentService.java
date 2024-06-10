package com.GradeCenter.service;

import com.GradeCenter.dtos.ParentDto;
import com.GradeCenter.dtos.UserIDRequest;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Optional;

public interface ParentService {
    List<ParentDto> getAllParents();

    Optional<ParentDto> getParentByUId(String userId);

    Optional<ParentDto> getParentById(Long id);

    ParentDto addParent(UserIDRequest userIDRequest);

    ResponseEntity<String> deleteParentID(Long id);

    ResponseEntity<String> deleteParentUID(String userID);

    Optional<ParentDto> updateParentID(Long id, ParentDto parentDto);

    Optional<ParentDto> updateParentUID(String userID, ParentDto parentDto);
}
