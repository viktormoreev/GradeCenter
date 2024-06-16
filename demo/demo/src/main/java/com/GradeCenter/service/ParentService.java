package com.GradeCenter.service;

import com.GradeCenter.dtos.ParentDto;
import com.GradeCenter.dtos.ParentUpdateDto;
import com.GradeCenter.dtos.UserIDRequest;

import java.util.List;

public interface ParentService {
    List<ParentDto> getAllParents();

    ParentDto addParent(UserIDRequest userIDRequest);

    ParentDto getParentById(Long id);

    ParentDto getParentByUId(String uid);

    boolean deleteParentUID(String userID);

    boolean deleteParentID(Long ID);

    ParentDto updateParentID(Long id, ParentUpdateDto parentUpdateDto);

    ParentDto updateParentUID(String userID, ParentUpdateDto parentUpdateDto);
}
