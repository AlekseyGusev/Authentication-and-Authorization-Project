package com.gusev.project.unit.service;

import com.gusev.project.domain.GroupClass;
import com.gusev.project.domain.User;
import com.gusev.project.domain.dto.GroupClassDto;
import com.gusev.project.domain.dto.GroupClassDtoBuilder;
import com.gusev.project.domain.dto.UserDto;
import com.gusev.project.domain.dto.UserDtoBuilder;
import com.gusev.project.repository.GroupClassRepository;
import com.gusev.project.service.TrainerService;
import com.gusev.project.service.UserService;
import com.gusev.project.service.impl.DefaultGroupClassService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.LinkedList;
import java.util.Optional;
import java.util.Set;

import static com.gusev.project.util.Utils.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DefaultGroupClassServiceTest {

    @Mock
    private GroupClassRepository groupClassRepository;
    @Mock
    private TrainerService trainerService;
    @Mock
    private UserService userService;
    @Mock
    private GroupClassDtoBuilder groupClassDtoBuilder;
    @Mock
    private UserDtoBuilder userDtoBuilder;

    @InjectMocks
    private DefaultGroupClassService service;

    @Test
    void findGroupClassById_shouldFindGroupClass_whenExists() {
        GroupClassDto expected = groupClassDtoMock();
        GroupClass groupClass = groupClassMock();
        when(groupClassRepository.findById(ID)).thenReturn(Optional.of(groupClass));
        when(groupClassDtoBuilder.fromGroupClassToDto(groupClass)).thenReturn(expected);

        GroupClassDto actual = service.findGroupClassById(ID);

        assertNotNull(actual);
        assertEquals(expected, actual);
        verify(groupClassRepository).findById(ID);
        verify(groupClassDtoBuilder).fromGroupClassToDto(groupClass);
    }

    @Test
    void findGroupClassById_whenNotFindGroupClass_whenDoesntExist() {
        when(groupClassRepository.findById(ID)).thenReturn(Optional.empty());

        GroupClassDto actual = service.findGroupClassById(ID);

        assertNotNull(actual);
        assertNull(actual.getId());
        verify(groupClassRepository).findById(ID);
        verifyNoInteractions(groupClassDtoBuilder);
    }

    @Test
    void findGroupClasses_shouldFindAllGroupClassesOrderByDateAscAndUserIdAsc_whenExists() {
        Set<GroupClass> groupClassSet = groupClassSet();
        Set<GroupClassDto> expected = groupClassDtoSet();
        when(groupClassRepository.findAllByOrderByDateAscUsersAsc()).thenReturn(groupClassSet);
        when(groupClassDtoBuilder.fromGroupClassSetToDtoSet(groupClassSet)).thenReturn(expected);

        Set<GroupClassDto> actual = service.findGroupClasses();

        assertNotNull(actual);
        assertEquals(expected.size(), actual.size());

        LinkedList<GroupClassDto> actualList = new LinkedList<>(actual);

        LocalDateTime dateGC1 = toLocalDateTime(actualList.get(0).getDate());
        LocalDateTime dateGC2 = toLocalDateTime(actualList.get(1).getDate());
        LocalDateTime dateGC3 = toLocalDateTime(actualList.get(2).getDate());

        assertTrue(dateGC1.isBefore(dateGC2));
        assertTrue(dateGC2.isBefore(dateGC3));
        verify(groupClassRepository).findAllByOrderByDateAscUsersAsc();
        verify(groupClassDtoBuilder).fromGroupClassSetToDtoSet(groupClassSet);
    }

    @Test
    void findAllTrainersGroupClasses_shouldFindAllTrainersGroupClassesOrderByDateAsc_whenExists() {
        Set<GroupClassDto> expected = groupClassDtoSetWithSameTrainer();
        Set<GroupClass> groupClassSet = groupClassSet();
        when(groupClassRepository.findAllByTrainerIdOrderByDate(ID)).thenReturn(groupClassSet);
        when(groupClassDtoBuilder.fromGroupClassSetToDtoSet(groupClassSet)).thenReturn(expected);

        Set<GroupClassDto> actual = service.findAllTrainersGroupClasses(ID);

        assertNotNull(actual);
        assertEquals(expected.size(), actual.size());

        LinkedList<GroupClassDto> actualList = new LinkedList<>(actual);

        actualList.forEach(g -> assertEquals(g.getTrainer().getId(), ID));
        verify(groupClassRepository).findAllByTrainerIdOrderByDate(ID);
        verify(groupClassDtoBuilder).fromGroupClassSetToDtoSet(groupClassSet);
    }

    @Test
    void saveGroupClass_shouldSaveGroupClass_whenTrainerExists() {
        GroupClass groupClass = groupClass();
        GroupClassDto dto = groupClassDtoWithTrainer();
        when(trainerService.findTrainerById(ID)).thenReturn(Optional.of(trainerMock()));
        when(groupClassDtoBuilder.fromDtoToGroupClass(dto)).thenReturn(groupClass);

        service.saveGroupClass(dto);

        verify(trainerService).findTrainerById(ID);
        verify(groupClassDtoBuilder).fromDtoToGroupClass(dto);
        verify(groupClassRepository).save(groupClass);
    }

    @Test
    void saveGroupClass_shouldNotSaveGroupClass_whenTrainerDoesntExist() {
        when(trainerService.findTrainerById(ID)).thenReturn(Optional.empty());

        service.saveGroupClass(groupClassDtoWithTrainer());

        verify(trainerService).findTrainerById(ID);
        verifyNoInteractions(groupClassDtoBuilder);
        verifyNoInteractions(groupClassRepository);
    }

    @Test
    void registerUser_shouldRegisterUser_whenGroupClassExists() {
        GroupClass groupClass = groupClass();
        UserDto userDto = userDtoMock();
        User user = userTest();
        when(groupClassRepository.findById(ID)).thenReturn(Optional.of(groupClass));
        when(userService.userExistsByUserName(USERNAME)).thenReturn(true);
        when(userService.findUserByUsername(USERNAME)).thenReturn(userDto);
        when(userDtoBuilder.fromDtoToUser(userDto)).thenReturn(user);

        service.registerUser(ID, USERNAME);

        assertTrue(groupClass.getUsers().contains(user));
        assertTrue(user.getGroupClasses().contains(groupClass));
        verify(groupClassRepository).findById(ID);
        verify(userService).userExistsByUserName(USERNAME);
        verify(userService).findUserByUsername(USERNAME);
        verify(userDtoBuilder).fromDtoToUser(userDto);
    }

    @Test
    void registerUser_shouldNotRegisterUser_whenGroupClassDoesntExist() {
        when(groupClassRepository.findById(ID)).thenReturn(Optional.empty());

        service.registerUser(ID, USERNAME);

        verifyNoInteractions(userService, userDtoBuilder);
    }

    @Test
    void addUserToGroupClass_shouldAddUserToGroupClass_whenGroupClassExists() {
        User user = userMock();
        GroupClass groupClass = groupClass();
        when(userService.findById(ID)).thenReturn(Optional.of(user));
        when(groupClassRepository.findById(ID)).thenReturn(Optional.of(groupClass));

        service.addUserToGroupClass(ID, ID);

        assertTrue(groupClass.getUsers().contains(user));
        verify(userService).findById(ID);
        verify(groupClassRepository).findById(ID);
    }

    @Test
    void addUserToGroupClass_shouldNotAddUserToGroupClass_whenGroupClassDoesntExist() {
        GroupClass groupClass = groupClass();
        when(userService.findById(ID)).thenReturn(Optional.empty());
        when(groupClassRepository.findById(ID)).thenReturn(Optional.of(groupClass));

        service.addUserToGroupClass(ID, ID);

        assertTrue(groupClass.getUsers().isEmpty());
        verify(userService).findById(ID);
        verify(groupClassRepository).findById(ID);
    }

    @Test
    void deleteGroupClassById_shouldDeleteGroupClass_whenExists() {
        GroupClass groupClass = groupClassMock();
        when(groupClassRepository.findById(ID)).thenReturn(Optional.of(groupClass));

        service.deleteGroupClassById(ID);

        verify(groupClassRepository).findById(ID);
        verify(groupClassRepository).delete(groupClass);
    }

    @Test
    void deleteGroupClassById_shouldNotDeleteGroupClass_whenDoesntExist() {
        GroupClass groupClass = groupClass();
        User user = userTest();
        groupClass.addUser(user);
        when(groupClassRepository.findById(ID)).thenReturn(Optional.empty());

        service.deleteGroupClassById(ID);

        assertTrue(groupClass.getUsers().contains(user));
        verify(groupClassRepository).findById(ID);
        verify(groupClassRepository, never()).delete(groupClass);
    }

    @Test
    void cancelUserRegistration_shouldCancelUserRegistration_whenExists() {
        GroupClass groupClass = groupClass();
        User user = userTest();
        groupClass.addUser(user);
        UserDto userDto = userDto();
        userDto.setId(ID);
        when(groupClassRepository.findById(ID)).thenReturn(Optional.of(groupClass));
        when(userService.findUserById(ID)).thenReturn(userDto);

        service.cancelUserRegistration(ID, ID);

        assertFalse(groupClass.getUsers().contains(user));
        verify(groupClassRepository).findById(ID);
        verify(userService).findUserById(ID);
    }

    @Test
    void cancelUserRegistration_shouldNotCancelUserRegistration_whenGroupClassDoesntExists() {
        GroupClass groupClass = groupClass();
        User user = userTest();
        groupClass.addUser(user);
        when(groupClassRepository.findById(ID)).thenReturn(Optional.of(groupClass));
        when(userService.findUserById(ID)).thenReturn(userDtoMock());

        service.cancelUserRegistration(ID, ID);

        assertTrue(groupClass.getUsers().contains(user));
        verify(groupClassRepository).findById(ID);
        verify(userService).findUserById(ID);
    }
}