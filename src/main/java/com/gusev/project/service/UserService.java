package com.gusev.project.service;

import com.gusev.project.domain.User;
import com.gusev.project.domain.dto.UserDto;
import com.gusev.project.exception.UsernameExistsException;

import java.util.Optional;
import java.util.Set;

public interface UserService {
    UserDto findUserByUsername(String username);
    Optional<User> findById(Long userId);
    UserDto findUserById(Long userId);
    boolean userExistsByUserName(String username);
    Set<UserDto> findAllUsers();
    Set<UserDto> findUsersByTrainerId(Long trainerId);
    Set<UserDto> findAllUsersWithoutTrainer();
    Set<UserDto> findAllUsersByGroupClassId(Long groupClassId);

    void saveUser(UserDto dto) throws UsernameExistsException;
    void addTrainerToUser(Long userId, Long trainerId);

    void updateUser(Long userId, UserDto dto) throws UsernameExistsException;

    void deleteUserById(Long userId);
    void deleteUsersTrainer(Long userId, Long trainerId);
}
