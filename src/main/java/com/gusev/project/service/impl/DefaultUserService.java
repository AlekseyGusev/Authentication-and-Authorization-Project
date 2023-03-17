package com.gusev.project.service.impl;

import com.gusev.project.domain.Trainer;
import com.gusev.project.domain.User;
import com.gusev.project.domain.dto.UserDto;
import com.gusev.project.domain.dto.UserDtoBuilder;
import com.gusev.project.exception.UsernameExistsException;
import com.gusev.project.repository.UserRepository;
import com.gusev.project.service.TrainerService;
import com.gusev.project.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
@Transactional(readOnly = true)
public class DefaultUserService implements UserService {

    private final UserRepository userRepository;
    private final TrainerService trainerService;
    private final UserDtoBuilder userDtoBuilder;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public DefaultUserService(UserRepository userRepository, TrainerService trainerService, UserDtoBuilder userDtoBuilder, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.trainerService = trainerService;
        this.userDtoBuilder = userDtoBuilder;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public UserDto findUserByUsername(String username) {
        return userRepository.findByUsername(username).map(userDtoBuilder::fromUserToDto).orElseGet(UserDto::new);
    }

    @Override
    public UserDto findUserById(Long userId) {
        return userRepository.findById(userId).map(userDtoBuilder::fromUserToDto).orElseGet(UserDto::new);
    }

    @Override
    public Set<UserDto> findAllUsers() {
        List<User> all = userRepository.findAll();
        return userDtoBuilder.fromUserSetToDtoSet(new HashSet<>(all));
    }

    @Override
    public Set<UserDto> findUsersByTrainerId(Long trainerId) {
        return userDtoBuilder.fromUserSetToDtoSet(userRepository.findAllByTrainerId(trainerId));
    }

    @Override
    public Set<UserDto> findAllUsersWithoutTrainer() {
        return userDtoBuilder.fromUserSetToDtoSet(userRepository.findAllByTrainerIsNull());
    }

    @Override
    public Set<UserDto> findAllUsersByGroupClassId(Long groupClassId) {
        return userDtoBuilder.fromUserSetToDtoSet(userRepository.findAllByGroupClassesId(groupClassId));
    }

    @Override
    public void saveUser(UserDto dto) throws UsernameExistsException {
        if (userRepository.existsByUsername(dto.getUsername())) {
            throw new UsernameExistsException("There is an account with username: " + dto.getUsername());
        }
        dto.setPassword(passwordEncoder.encode(dto.getPassword()));
        User user = userDtoBuilder.fromDtoToUser(dto);
        userRepository.save(user);
    }

    @Override
    @Transactional
    public void addTrainerToUser(Long userId, Long trainerId) {
        Optional<User> userOpt = userRepository.findById(userId);
        Optional<Trainer> trainerOpt = trainerService.findTrainerById(trainerId);
        if (userOpt.isPresent() && trainerOpt.isPresent()) {
            userOpt.get().setTrainer(trainerOpt.get());
        }
    }

    @Override
    @Transactional
    public void deleteUserById(Long userId) {
        Optional<User> userOpt = userRepository.findById(userId);
        if (userOpt.isPresent()) {
            User user = userOpt.get();
            user.removeTrainer();
            user.removeFromGroupClasses();
            userRepository.delete(user);
        }
    }

    @Override
    @Transactional
    public void updateUser(Long userId, UserDto dto) throws UsernameExistsException {
        Optional<User> userOpt = userRepository.findById(userId);
        if (userOpt.isEmpty()) {
            throw new UsernameExistsException("There is an account with username: " + dto.getUsername());
        }
        userOpt.ifPresent(user -> userDtoBuilder.updateUser(user, dto));
    }

    @Override
    @Transactional
    public void deleteUsersTrainer(Long userId, Long trainerId) {
        Optional<User> userOpt = userRepository.findById(userId);
        if (userOpt.isPresent() && trainerService.trainerExists(trainerId)) {
            userOpt.get().removeTrainer();
        }
    }

    @Override
    public Optional<User> findById(Long userId) {
        return userRepository.findById(userId);
    }

    @Override
    public boolean userExistsByUserName(String username) {
        return userRepository.existsByUsername(username);
    }
}
