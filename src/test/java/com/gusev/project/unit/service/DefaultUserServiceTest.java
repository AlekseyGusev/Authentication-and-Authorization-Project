package com.gusev.project.unit.service;

import com.gusev.project.domain.Trainer;
import com.gusev.project.domain.User;
import com.gusev.project.domain.dto.GroupClassDto;
import com.gusev.project.domain.dto.UserDto;
import com.gusev.project.domain.dto.UserDtoBuilder;
import com.gusev.project.exception.UsernameExistsException;
import com.gusev.project.repository.UserRepository;
import com.gusev.project.service.TrainerService;
import com.gusev.project.service.impl.DefaultUserService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static com.gusev.project.util.Utils.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DefaultUserServiceTest {

    @Mock
    private UserRepository userRepository;
    @Mock
    private TrainerService trainerService;
    @Mock
    private UserDtoBuilder userDtoBuilder;
    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private DefaultUserService service;

    @Test
    void findUserByUsername_shouldFindUser_whenExists() {
        User user = userTest();
        UserDto expected = userDto();
        when(userRepository.findByUsername(USERNAME)).thenReturn(Optional.of(user));
        when(userDtoBuilder.fromUserToDto(user)).thenReturn(expected);

        UserDto actual = service.findUserByUsername(USERNAME);

        assertNotNull(actual);
        assertEquals(expected, actual);
        verify(userRepository).findByUsername(USERNAME);
        verify(userDtoBuilder).fromUserToDto(user);
    }

    @Test
    void findUserByUsername_shouldNotFindUser_whenDoestExist() {
        UserDto unExpected = userDto();
        when(userRepository.findByUsername(USERNAME)).thenReturn(Optional.empty());

        UserDto actual = service.findUserByUsername(USERNAME);

        assertNotNull(actual);
        assertNotEquals(unExpected, actual);
        verify(userRepository).findByUsername(USERNAME);
        verifyNoInteractions(userDtoBuilder);
    }

    @Test
    void findUserById_shouldFindUser_whenExists() {
        User user = userTest();
        UserDto expected = userDto();
        when(userRepository.findById(ID)).thenReturn(Optional.of(user));
        when(userDtoBuilder.fromUserToDto(user)).thenReturn(expected);

        UserDto actual = service.findUserById(ID);

        assertNotNull(actual);
        assertEquals(expected, actual);
        verify(userRepository).findById(ID);
        verify(userDtoBuilder).fromUserToDto(user);
    }

    @Test
    void findUserById_shouldNotFindUser_whenDoesntExist() {
        UserDto expected = userDto();
        when(userRepository.findById(ID)).thenReturn(Optional.empty());

        UserDto actual = service.findUserById(ID);

        assertNotNull(actual);
        assertNotEquals(expected, actual);
        verify(userRepository).findById(ID);
        verifyNoInteractions(userDtoBuilder);
    }

    @Test
    void findAllUsers_shouldFindAllUsers_whenExists() {
        Set<UserDto> unExpected = userDtoSet();
        Set<User> userSet = userSet();
        when(userRepository.findAll()).thenReturn(List.copyOf(userSet));
        when(userDtoBuilder.fromUserSetToDtoSet(userSet)).thenReturn(new HashSet<>(unExpected));

        Set<UserDto> actual = service.findAllUsers();

        assertNotNull(actual);
        assertFalse(actual.isEmpty());
        assertNotSame(unExpected, actual);
        assertTrue(actual.containsAll(unExpected));
        verify(userRepository).findAll();
        verify(userDtoBuilder).fromUserSetToDtoSet(userSet);
    }

    @Test
    void findUsersByTrainerId_shouldFindUsersByTrainer_whenExists() {
        Set<UserDto> unExpected = userDtoSetWithSameTrainer();
        Set<User> userSet = userSetWithSameTrainer();
        when(userRepository.findAllByTrainerId(ID)).thenReturn(userSet);
        when(userDtoBuilder.fromUserSetToDtoSet(userSet)).thenReturn(new HashSet<>(unExpected));

        Set<UserDto> actual = service.findUsersByTrainerId(ID);

        assertNotNull(actual);
        assertFalse(actual.isEmpty());
        assertNotSame(unExpected, actual);
        actual.forEach(u -> assertEquals(ID, (long) u.getTrainer().getId()));
        verify(userRepository).findAllByTrainerId(ID);
        verify(userDtoBuilder).fromUserSetToDtoSet(userSet);
    }

    @Test
    void findAllUsersWithoutTrainer_shouldFindAllUsersWithoutTrainer_whenCalled() {
        Set<UserDto> unExpected = userDtoSet();
        Set<User> userSetWithoutTrainer = userSet();
        when(userRepository.findAllByTrainerIsNull()).thenReturn(userSetWithoutTrainer);
        when(userDtoBuilder.fromUserSetToDtoSet(userSetWithoutTrainer)).thenReturn(new HashSet<>(unExpected));

        Set<UserDto> actual = service.findAllUsersWithoutTrainer();

        assertNotNull(actual);
        assertFalse(actual.isEmpty());
        assertNotSame(unExpected, actual);
        actual.forEach(u -> assertNull(u.getTrainer()));
        verify(userRepository).findAllByTrainerIsNull();
        verify(userDtoBuilder).fromUserSetToDtoSet(userSetWithoutTrainer);
    }

    @Test
    void findAllUsersByGroupClassId_shouldFindAllUsersByGroupClass_whenCalled() {
        Set<UserDto> unExpected = userDtoSetWithSameGroupClass();
        Set<User> userSetWithSameGroupClass = userSetWithSameGroupClass();
        when(userRepository.findAllByGroupClassesId(ID)).thenReturn(userSetWithSameGroupClass);
        when(userDtoBuilder.fromUserSetToDtoSet(userSetWithSameGroupClass)).thenReturn(new HashSet<>(unExpected));

        Set<UserDto> actual = service.findAllUsersByGroupClassId(ID);

        assertNotNull(actual);
        assertFalse(actual.isEmpty());
        assertNotSame(unExpected, actual);
        actual.forEach(u -> {
            List<GroupClassDto> list = List.copyOf(u.getGroupClasses());
            assertEquals(ID, list.size());
            assertEquals(ID, list.get(0).getId());
        });
        verify(userRepository).findAllByGroupClassesId(ID);
        verify(userDtoBuilder).fromUserSetToDtoSet(userSetWithSameGroupClass);
    }

    @Test
    void saveUser_shouldSaveUser_whenCalled() throws UsernameExistsException {
        UserDto dto = userDtoWithUsernameAndPassword();
        User user = userWithUsernameAndPassword();
        when(userRepository.existsByUsername(dto.getUsername())).thenReturn(false);
        when(passwordEncoder.encode(PASSWORD)).thenReturn(ENCODED_PASSWORD);
        when(userDtoBuilder.fromDtoToUser(dto)).thenReturn(user);

        service.saveUser(dto);

        assertEquals(USERNAME, user.getUsername());
        assertEquals(ENCODED_PASSWORD, user.getPassword());
        verify(userRepository).existsByUsername(USERNAME);
        verify(passwordEncoder).encode(PASSWORD);
        verify(userDtoBuilder).fromDtoToUser(dto);
        verify(userRepository).save(user);
    }

    @Test
    void saveUser_shouldNotSaveUser_whenCalled() {
        UserDto dto = userDtoWithUsernameAndPassword();
        when(userRepository.existsByUsername(dto.getUsername())).thenReturn(true);

        Exception exception = assertThrows(UsernameExistsException.class, () -> service.saveUser(dto));
        assertEquals("There is an account with username: " + USERNAME, exception.getMessage());
        verifyNoMoreInteractions(userRepository);
        verifyNoInteractions(passwordEncoder, userDtoBuilder);
    }

    @Test
    void addTrainerToUser_shouldAddTrainerToUser_whenExists() {
        User user = userTest();
        Trainer trainer = trainer();
        when(userRepository.findById(ID)).thenReturn(Optional.of(user));
        when(trainerService.findTrainerById(ID)).thenReturn(Optional.of(trainer));

        service.addTrainerToUser(ID, ID);

        assertNotNull(user.getTrainer());
        assertEquals(ID, user.getTrainer().getId());
        verify(userRepository).findById(ID);
        verify(trainerService).findTrainerById(ID);
    }

    @Test
    void addTrainerToUser_shouldNotAddTrainerToUser_whenDoesntExists() {
        User user = userTest();
        when(userRepository.findById(ID)).thenReturn(Optional.of(user));
        when(trainerService.findTrainerById(ID)).thenReturn(Optional.empty());

        service.addTrainerToUser(ID, ID);

        assertNull(user.getTrainer());
        verify(userRepository).findById(ID);
        verify(trainerService).findTrainerById(ID);
    }

    @Test
    void deleteUserById_shouldDeleteUserById_whenExists() {
        User user = userWithTrainerAndGroupClass();
        when(userRepository.findById(ID)).thenReturn(Optional.of(user));
        doNothing().when(userRepository).delete(user);

        service.deleteUserById(ID);

        assertNull(user.getTrainer());
        assertTrue(user.getGroupClasses().isEmpty());
        verify(userRepository).delete(user);
    }

    @Test
    void deleteUserById_shouldNotDeleteUserById_whenDoesntExist() {
        when(userRepository.findById(ID)).thenReturn(Optional.empty());

        service.deleteUserById(ID);

        verify(userRepository).findById(ID);
        verifyNoMoreInteractions(userRepository);
    }

    @Test
    void updateUser_shouldUpdateUser_whenExists() throws UsernameExistsException {
        User user = userTest();
        UserDto dto = changedUserDto();
        User result = changedUser();
        when(userRepository.findById(ID)).thenReturn(Optional.of(user));
        when(userDtoBuilder.updateUser(user, dto)).thenReturn(result);

        service.updateUser(ID, dto);

        assertEquals(DIFFERENT_USERNAME, result.getUsername());
        assertEquals(DIFFERENT_FIRST_NAME, result.getFirstName());
        assertEquals(DIFFERENT_LASTNAME, result.getLastName());

        verify(userRepository).findById(ID);
        verify(userDtoBuilder).updateUser(user, dto);
    }

    @Test
    void updateUser_shouldNotUpdateUser_whenDoesntExist() {
        UserDto dto = changedUserDto();
        when(userRepository.findById(ID)).thenReturn(Optional.empty());

        Exception exception = assertThrows(UsernameExistsException.class, () -> service.updateUser(ID, dto));
        assertEquals("There is an account with username: " + DIFFERENT_USERNAME, exception.getMessage());
        verify(userRepository, times(1)).findById(ID);
        verifyNoInteractions(userDtoBuilder);
    }

    @Test
    void deleteUsersTrainer_shouldDeleteUsersTrainer_whenExists() {
        User user = userWithTrainer();
        when(userRepository.findById(ID)).thenReturn(Optional.of(user));
        when(trainerService.trainerExists(ID)).thenReturn(true);

        service.deleteUsersTrainer(ID, ID);

        assertNull(user.getTrainer());
        verify(userRepository).findById(ID);
        verify(trainerService).trainerExists(ID);
    }

    @Test
    void deleteUsersTrainer_shouldNotDeleteUsersTrainer_whenDoesntExist() {
        User user = userWithTrainer();
        when(userRepository.findById(ID)).thenReturn(Optional.of(user));
        when(trainerService.trainerExists(ID)).thenReturn(false);

        service.deleteUsersTrainer(ID, ID);

        assertNotNull(user.getTrainer());
        assertEquals(ID, user.getTrainer().getId());
        verify(userRepository).findById(ID);
        verify(trainerService).trainerExists(ID);
    }
}