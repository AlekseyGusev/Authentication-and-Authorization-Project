package com.gusev.project.domain.dto;

import com.gusev.project.domain.User;
import com.gusev.project.domain.security.Role;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.LinkedHashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class UserDtoBuilder {

    private final TrainerDtoBuilder trainerDtoBuilder;
    private final GroupClassDtoBuilder groupClassDtoBuilder;

    @Autowired
    public UserDtoBuilder(TrainerDtoBuilder trainerDtoBuilder, GroupClassDtoBuilder groupClassDtoBuilder) {
        this.trainerDtoBuilder = trainerDtoBuilder;
        this.groupClassDtoBuilder = groupClassDtoBuilder;
    }

    public UserDto fromUserToDto(User user) {
        UserDto dto = new UserDto();
        dto.setId(user.getId());
        dto.setFirstName(user.getFirstName());
        dto.setLastName(user.getLastName());
        dto.setUsername(user.getUsername());
        dto.setPassword("STRONG_SECRET");
        if (user.getTrainer() != null) {
            dto.setTrainer(trainerDtoBuilder.fromTrainerToDto(user.getTrainer()));
        }
        dto.setGroupClasses(groupClassDtoBuilder.fromGroupClassSetToDtoSet(user.getGroupClasses()));
        return dto;
    }

    public User fromDtoToUser(UserDto dto) {
        User user = new User();
        user.setId(dto.getId());
        user.setFirstName(dto.getFirstName());
        user.setLastName(dto.getLastName());
        user.setUsername(dto.getUsername());
        user.setPassword(dto.getPassword());
        user.setRole(Role.defaultUserRole());
        user.setEnabled(true);
        return user;
    }

    public User updateUser(User user, UserDto dto) {
        user.setUsername(dto.getUsername());
        user.setFirstName(dto.getFirstName());
        user.setLastName(dto.getLastName());
        return user;
    }

    public Set<UserDto> fromUserSetToDtoSet(Set<User> users) {
        return users.stream()
                .map(this::fromUserToDto)
                .collect(Collectors.toCollection(LinkedHashSet::new));
    }
}
