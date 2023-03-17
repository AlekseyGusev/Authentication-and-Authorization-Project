package com.gusev.project.service;


import com.gusev.project.domain.dto.GroupClassDto;
import com.gusev.project.domain.dto.UserDto;

import java.util.Set;

public interface GroupClassService {
    GroupClassDto findGroupClassById(Long groupClassId);
    Set<GroupClassDto> findGroupClasses();
    Set<GroupClassDto> findAllTrainersGroupClasses(Long trainerId);
    Set<UserDto> findAllUsersNotInGroupClass(Long groupClassId);

    void saveGroupClass(GroupClassDto dto);
    void registerUser(Long groupClassId, String username);
    void addUserToGroupClass(Long userId, Long classId);

    void deleteGroupClassById(Long groupClassId);
    void cancelUserRegistration(Long groupClassId, Long userId);
}
