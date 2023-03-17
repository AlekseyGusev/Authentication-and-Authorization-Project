package com.gusev.project.integration.mvc;

import com.gusev.project.service.GroupClassService;
import com.gusev.project.service.TrainerService;
import com.gusev.project.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.test.web.servlet.MockMvc;

import static com.gusev.project.util.UrlPath.*;
import static com.gusev.project.util.Utils.*;
import static com.gusev.project.util.View.*;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@AutoConfigureMockMvc
public class GroupClassControllerIntegrationTest {

    @MockBean
    private GroupClassService groupClassService;
    @MockBean
    private TrainerService trainerService;
    @MockBean
    private UserService userService;

    @Autowired
    private MockMvc mockMvc;

    @Test
    void getGroupClasses_shouldReturnGroupClassesView_whenCalled() throws Exception {
        when(groupClassService.findGroupClasses()).thenReturn(groupClassDtoSet());
        when(userService.findUserByUsername(anyString())).thenReturn(userDto());

        mockMvc
                .perform(get(GROUP_CLASSES).with(ADMIN()))
                .andExpect(status().isOk())
                .andExpect(view().name(GROUP_CLASSES_ALL_VIEW))
                .andReturn();
        mockMvc
                .perform(get(GROUP_CLASSES).with(TRAINER()))
                .andExpect(status().isOk())
                .andExpect(view().name(GROUP_CLASSES_ALL_VIEW))
                .andReturn();
        mockMvc
                .perform(get(GROUP_CLASSES).with(USER()))
                .andExpect(status().isOk())
                .andExpect(view().name(GROUP_CLASSES_ALL_VIEW))
                .andReturn();

        verify(groupClassService, times(3)).findGroupClasses();
        verify(userService, times(3)).findUserByUsername(anyString());
    }

    @Test
    void getGroupClassUsers_shouldReturnGroupClassUsers_whenCalled() throws Exception {
        when(groupClassService.findGroupClassById(anyLong())).thenReturn(groupClassDto());
        when(userService.findAllUsersByGroupClassId(anyLong())).thenReturn(userDtoSet());

        mockMvc
                .perform(get(GROUP_CLASS_USERS).with(ADMIN()))
                .andExpect(status().isOk())
                .andExpect(view().name(GROUP_CLASS_USERS_ALL_VIEW))
                .andReturn();
        mockMvc
                .perform(get(GROUP_CLASS_USERS).with(TRAINER()))
                .andExpect(status().isOk())
                .andExpect(view().name(GROUP_CLASS_USERS_ALL_VIEW))
                .andReturn();
        mockMvc
                .perform(get(GROUP_CLASS_USERS).with(USER()))
                .andExpect(status().isForbidden())
                .andReturn();

        verify(groupClassService, times(2)).findGroupClassById(anyLong());
        verify(userService, times(2)).findAllUsersByGroupClassId(anyLong());
    }

    @Test
    void getAllAvailableUsers_shouldReturnAvailableGroupClassUsers_whenCalled() throws Exception {
        when(groupClassService.findAllUsersNotInGroupClass(anyLong())).thenReturn(userDtoSet());

        mockMvc
                .perform(get(GROUP_CLASS_USERS_ALL_AVAILABLE).with(ADMIN()))
                .andExpect(status().isOk())
                .andExpect(view().name(GROUP_CLASS_USERS_ALL_AVAILABLE_VIEW))
                .andReturn();
        mockMvc
                .perform(get(GROUP_CLASS_USERS_ALL_AVAILABLE).with(TRAINER()))
                .andExpect(status().isForbidden())
                .andReturn();
        mockMvc
                .perform(get(GROUP_CLASS_USERS_ALL_AVAILABLE).with(USER()))
                .andExpect(status().isForbidden())
                .andReturn();

        verify(groupClassService, times(1)).findAllUsersNotInGroupClass(anyLong());
    }

    @Test
    void createGroupClass_shouldReturnGroupClassFormView_whenGetMethodCalled() throws Exception {
        when(trainerService.findAllTrainers()).thenReturn(trainerDtoSet());

        mockMvc
                .perform(get(GROUP_CLASS_CREATE).with(ADMIN()))
                .andExpect(status().isOk())
                .andExpect(view().name(GROUP_CLASS_CREATE_VIEW))
                .andReturn();
        mockMvc
                .perform(get(GROUP_CLASS_CREATE).with(TRAINER()))
                .andExpect(status().isForbidden())
                .andReturn();
        mockMvc
                .perform(get(GROUP_CLASS_CREATE).with(USER()))
                .andExpect(status().isForbidden())
                .andReturn();

        verify(trainerService, times(1)).findAllTrainers();
    }

    @Test
    void createGroupClass_shouldCreateGroupClass_whenPostMethodCalled() throws Exception {
        doNothing().when(groupClassService).saveGroupClass(any());

        mockMvc
                .perform(post(GROUP_CLASS_CREATE)
                        .param("name", "test")
                        .with(ADMIN()))
                .andExpect(status().is(HttpStatus.FOUND.value()))
                .andExpect(redirectedUrl(GROUP_CLASSES))
                .andReturn();
        mockMvc
                .perform(post(GROUP_CLASS_CREATE).with(TRAINER()))
                .andExpect(status().isForbidden())
                .andReturn();
        mockMvc
                .perform(post(GROUP_CLASS_CREATE).with(USER()))
                .andExpect(status().isForbidden())
                .andReturn();

        verify(groupClassService, times(1)).saveGroupClass(any());
    }

    @Test
    void createGroupClass_shouldNotCreateGroupClass_whenPostMethodCalled() throws Exception {
        when(trainerService.findAllTrainers()).thenReturn(trainerDtoSet());

        mockMvc
                .perform(post(GROUP_CLASS_CREATE).with(ADMIN()))
                .andExpect(status().isOk())
                .andExpect(view().name(GROUP_CLASS_CREATE_VIEW))
                .andReturn();
        mockMvc
                .perform(post(GROUP_CLASS_CREATE).with(TRAINER()))
                .andExpect(status().isForbidden())
                .andReturn();
        mockMvc
                .perform(post(GROUP_CLASS_CREATE).with(USER()))
                .andExpect(status().isForbidden())
                .andReturn();

        verify(trainerService, times(1)).findAllTrainers();
        verifyNoInteractions(groupClassService);
    }

    @Test
    void addUserToGroupClass_shouldAddUserToGroupClass_whenExists() throws Exception {
        doNothing().when(groupClassService).addUserToGroupClass(anyLong(), anyLong());

        mockMvc
                .perform(get(GROUP_CLASS_USER_REGISTRATION).with(ADMIN()))
                .andExpect(status().is(HttpStatus.FOUND.value()))
                .andExpect(redirectedUrl(GROUP_CLASS_USERS))
                .andReturn();
        mockMvc
                .perform(get(GROUP_CLASS_USER_REGISTRATION).with(TRAINER()))
                .andExpect(status().isForbidden())
                .andReturn();
        mockMvc
                .perform(get(GROUP_CLASS_USER_REGISTRATION).with(USER()))
                .andExpect(status().isForbidden())
                .andReturn();

        verify(groupClassService, times(1)).addUserToGroupClass(anyLong(), anyLong());
    }

    @Test
    void registerUser_shouldAddUserToGroupClass_whenExists() throws Exception {
        doNothing().when(groupClassService).registerUser(anyLong(), anyString());

        mockMvc
                .perform(get(GROUP_CLASS_USERS_USER_REGISTRATION).with(ADMIN()))
                .andExpect(status().is(HttpStatus.FOUND.value()))
                .andExpect(redirectedUrl(GROUP_CLASSES))
                .andReturn();
        mockMvc
                .perform(get(GROUP_CLASS_USERS_USER_REGISTRATION).with(TRAINER()))
                .andExpect(status().isForbidden())
                .andReturn();
        mockMvc
                .perform(get(GROUP_CLASS_USERS_USER_REGISTRATION).with(USER()))
                .andExpect(status().is(HttpStatus.FOUND.value()))
                .andExpect(redirectedUrl(GROUP_CLASSES))
                .andReturn();

        verify(groupClassService, times(2)).registerUser(anyLong(), anyString());
    }

    @Test
    void deleteGroupClass_shouldDeleteGroupClass_whenExists() throws Exception {
        doNothing().when(groupClassService).deleteGroupClassById(anyLong());

        mockMvc
                .perform(get(GROUP_CLASS_DELETE).with(ADMIN()))
                .andExpect(status().is(HttpStatus.FOUND.value()))
                .andExpect(redirectedUrl(GROUP_CLASSES))
                .andReturn();
        mockMvc
                .perform(get(GROUP_CLASS_DELETE).with(TRAINER()))
                .andExpect(status().isForbidden())
                .andReturn();
        mockMvc
                .perform(get(GROUP_CLASS_DELETE).with(USER()))
                .andExpect(status().isForbidden())
                .andReturn();

        verify(groupClassService, times(1)).deleteGroupClassById(anyLong());
    }

    @Test
    void deleteGroupClassesUser_shouldDeleteUserFromGroupClass_whenExists() throws Exception {
        doNothing().when(groupClassService).cancelUserRegistration(anyLong(), anyLong());

        mockMvc
                .perform(get(GROUP_CLASS_USERS_CANCEL_REGISTRATION).with(ADMIN()))
                .andExpect(status().is(HttpStatus.FOUND.value()))
                .andExpect(redirectedUrl(GROUP_CLASSES))
                .andReturn();
        mockMvc
                .perform(get(GROUP_CLASS_USERS_CANCEL_REGISTRATION).with(TRAINER()))
                .andExpect(status().isForbidden())
                .andReturn();
        mockMvc
                .perform(get(GROUP_CLASS_USERS_CANCEL_REGISTRATION).with(USER()))
                .andExpect(status().is(HttpStatus.FOUND.value()))
                .andExpect(redirectedUrl(GROUP_CLASSES))
                .andReturn();

        verify(groupClassService, times(2)).cancelUserRegistration(anyLong(), anyLong());
    }
}
