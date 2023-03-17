package com.gusev.project.service.impl;

import com.gusev.project.domain.GroupClass;
import com.gusev.project.domain.Trainer;
import com.gusev.project.domain.User;
import com.gusev.project.domain.dto.GroupClassDto;
import com.gusev.project.domain.dto.GroupClassDtoBuilder;
import com.gusev.project.domain.dto.UserDto;
import com.gusev.project.domain.dto.UserDtoBuilder;
import com.gusev.project.repository.GroupClassRepository;
import com.gusev.project.service.GroupClassService;
import com.gusev.project.service.TrainerService;
import com.gusev.project.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.LinkedHashSet;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class DefaultGroupClassService implements GroupClassService {

    private final GroupClassRepository groupClassRepository;
    private final TrainerService trainerService;
    private final UserService userService;
    private final GroupClassDtoBuilder groupClassDtoBuilder;
    private final UserDtoBuilder userDtoBuilder;

    @Autowired
    public DefaultGroupClassService(GroupClassRepository groupClassRepository, TrainerService trainerService, UserService userService, GroupClassDtoBuilder groupClassDtoBuilder, UserDtoBuilder userDtoBuilder) {
        this.groupClassRepository = groupClassRepository;
        this.trainerService = trainerService;
        this.userService = userService;
        this.groupClassDtoBuilder = groupClassDtoBuilder;
        this.userDtoBuilder = userDtoBuilder;
    }

    @Override
    public GroupClassDto findGroupClassById(Long groupClassId) {
        return groupClassRepository.findById(groupClassId).map(groupClassDtoBuilder::fromGroupClassToDto).orElseGet(GroupClassDto::new);
    }

    @Override
    public Set<GroupClassDto> findGroupClasses() {
        return groupClassDtoBuilder.fromGroupClassSetToDtoSet(groupClassRepository.findAllByOrderByDateAscUsersAsc());
    }

    @Override
    public Set<GroupClassDto> findAllTrainersGroupClasses(Long trainerId) {
        return groupClassDtoBuilder.fromGroupClassSetToDtoSet(groupClassRepository.findAllByTrainerIdOrderByDate(trainerId));
    }

    @Override
    public Set<UserDto> findAllUsersNotInGroupClass(Long groupClassId) {
        Set<UserDto> users = userService.findAllUsers();
        return users.stream()
                .filter(
                    u -> u.getGroupClasses().stream()
                        .noneMatch(group -> group.getId().equals(groupClassId)))
                .collect(Collectors.toCollection(LinkedHashSet::new));
    }

    @Override
    @Transactional
    public void saveGroupClass(GroupClassDto dto) {
        Optional<Trainer> trainerOpt = trainerService.findTrainerById(dto.getTrainer().getId());
        if (trainerOpt.isPresent()) {
            GroupClass groupClass = groupClassDtoBuilder.fromDtoToGroupClass(dto);
            groupClass.setTrainer(trainerOpt.get());
            groupClassRepository.save(groupClass);
        }
    }

    @Override
    @Transactional
    public void registerUser(Long groupClassId, String username) {
        Optional<GroupClass> groupClassOpt = groupClassRepository.findById(groupClassId);
        if (groupClassOpt.isPresent() && userService.userExistsByUserName(username)) {
            GroupClass groupClass = groupClassOpt.get();
            UserDto userDto = userService.findUserByUsername(username);
            User user = userDtoBuilder.fromDtoToUser(userDto);
            groupClass.addUser(user);
            user.setGroupClasses(Set.of(groupClass));
        }
    }

    @Override
    @Transactional
    public void addUserToGroupClass(Long userId, Long classId) {
        Optional<User> userOpt = userService.findById(userId);
        Optional<GroupClass> groupClassOpt = groupClassRepository.findById(classId);
        if (userOpt.isPresent() && groupClassOpt.isPresent()) {
            groupClassOpt.get().addUser(userOpt.get());
        }
    }

    @Override
    @Transactional
    public void deleteGroupClassById(Long groupClassId) {
        Optional<GroupClass> groupClassOpt = groupClassRepository.findById(groupClassId);
        if (groupClassOpt.isPresent()) {
            GroupClass groupClass = groupClassOpt.get();
            groupClass.removeTrainer();
            groupClass.removeUsers();
            groupClassRepository.delete(groupClass);
        }
    }

    @Override
    @Transactional
    public void cancelUserRegistration(Long groupClassId, Long userId) {
        Optional<GroupClass> groupClassOpt = groupClassRepository.findById(groupClassId);
        UserDto userDto = userService.findUserById(userId);
        if (groupClassOpt.isPresent()) {
            GroupClass groupClass = groupClassOpt.get();
            groupClass.removeUser(userDto.getId());
        }
    }
}
