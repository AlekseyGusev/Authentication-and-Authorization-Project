package com.gusev.project.integration.mvc;


import com.gusev.project.exception.UsernameExistsException;
import com.gusev.project.service.GroupClassService;
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
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@AutoConfigureMockMvc
public class UserControllerIntegrationTest {

    @MockBean
    private UserService userService;
    @MockBean
    private GroupClassService groupClassService;

    @Autowired
    private MockMvc mockMvc;

    @Test
    void getAllUsers_shouldReturnUsersView() throws Exception {
        when(userService.findAllUsers()).thenReturn(userDtoSet());

        mockMvc
                .perform(get(USERS).with(ADMIN()))
                .andExpect(status().isOk())
                .andExpect(view().name(USERS_ALL_VIEW))
                .andReturn();
        mockMvc
                .perform(get(USERS).with(TRAINER()))
                .andExpect(status().isOk())
                .andExpect(view().name(USERS_ALL_VIEW))
                .andReturn();
        mockMvc
                .perform(get(USERS).with(USER()))
                .andExpect(status().isForbidden())
                .andReturn();

        verify(userService, times(2)).findAllUsers();
    }

    @Test
    void getUserInfo_shouldReturnUsersInfoById_whenExists() throws Exception {
        when(userService.findUserById(anyLong())).thenReturn(userDto());

        mockMvc
                .perform(get(USER_INFO).with(ADMIN()))
                .andExpect(status().isOk())
                .andExpect(view().name(USER_INFO_VIEW))
                .andReturn();
        mockMvc
                .perform(get(USER_INFO).with(TRAINER()))
                .andExpect(status().isOk())
                .andExpect(view().name(USER_INFO_VIEW))
                .andReturn();
        mockMvc
                .perform(get(USER_INFO).with(USER()))
                .andExpect(status().isOk())
                .andExpect(view().name(USER_INFO_VIEW))
                .andReturn();

        verify(userService, times(3)).findUserById(anyLong());
    }

    @Test
    void showUserAccount_shouldReturnUsersInfoByUsername_whenExists() throws Exception {
        when(userService.findUserByUsername(anyString())).thenReturn(userDto());

        mockMvc
                .perform(get(USER_ACCOUNT_INFO).with(ADMIN()))
                .andExpect(status().isOk())
                .andExpect(view().name(USER_INFO_VIEW))
                .andReturn();
        mockMvc
                .perform(get(USER_ACCOUNT_INFO).with(TRAINER()))
                .andExpect(status().isForbidden())
                .andReturn();
        mockMvc
                .perform(get(USER_ACCOUNT_INFO).with(USER()))
                .andExpect(status().isOk())
                .andExpect(view().name(USER_INFO_VIEW))
                .andReturn();

        verify(userService, times(2)).findUserByUsername(anyString());
    }

    @Test
    void createUser_shouldReturnUserFormView_whenGetMethodCalled() throws Exception {
        mockMvc
                .perform(get(USER_CREATE).with(ADMIN()))
                .andExpect(status().isOk())
                .andExpect(view().name(USER_CREATE_FORM_VIEW))
                .andReturn();
        mockMvc
                .perform(get(USER_CREATE).with(TRAINER()))
                .andExpect(status().isForbidden())
                .andReturn();
        mockMvc
                .perform(get(USER_CREATE).with(USER()))
                .andExpect(status().isForbidden())
                .andReturn();
    }

    @Test
    void createUser_shouldCreateUser_whenPostMethodCalled() throws Exception {
        doNothing().when(userService).saveUser(any());

        mockMvc
                .perform(
                        post(USER_CREATE)
                                .param("username", "test")
                                .param("firstName", "firstName")
                                .param("lastName", "lastName")
                                .param("password", "password")
                                .with(ADMIN()))
                .andExpect(status().is(HttpStatus.FOUND.value()))
                .andExpect(redirectedUrl(USERS))
                .andReturn();
        mockMvc
                .perform(get(USER_CREATE).with(TRAINER()))
                .andExpect(status().isForbidden())
                .andReturn();
        mockMvc
                .perform(get(USER_CREATE).with(USER()))
                .andExpect(status().isForbidden())
                .andReturn();

        verify(userService, times(1)).saveUser(any());
    }

    @Test
    void editUser_shouldReturnEditUserFormView_whenExists() throws Exception {
        when(userService.findUserById(anyLong())).thenReturn(userDto());

        mockMvc
                .perform(get(USER_EDIT).with(ADMIN()))
                .andExpect(status().isOk())
                .andExpect(view().name(USER_EDIT_VIEW))
                .andReturn();
        mockMvc
                .perform(get(USER_EDIT).with(TRAINER()))
                .andExpect(status().isForbidden())
                .andReturn();
        mockMvc
                .perform(get(USER_EDIT).with(USER()))
                .andExpect(status().isForbidden())
                .andReturn();

        verify(userService, times(1)).findUserById(anyLong());
    }

    @Test
    void updateUser_shouldUpdateUser_whenExists() throws Exception {
        doNothing().when(userService).updateUser(anyLong(), any());

        mockMvc
                .perform(
                        post(USER_UPDATE)
                                .param("username", "username")
                                .param("firstName", "firstName")
                                .param("lastName", "lastName")
                                .param("password", "password")
                                .with(ADMIN()))
                .andExpect(status().is(HttpStatus.FOUND.value()))
                .andExpect(redirectedUrl(USERS))
                .andReturn();
        mockMvc
                .perform(get(USER_UPDATE).with(TRAINER()))
                .andExpect(status().isForbidden())
                .andReturn();
        mockMvc
                .perform(get(USER_UPDATE).with(USER()))
                .andExpect(status().isForbidden())
                .andReturn();

        verify(userService, times(1)).updateUser(anyLong(), any());
    }

    @Test
    void updateUser_shouldNotUpdateUserWithError_whenCalled() throws Exception {
        mockMvc
                .perform(
                        post(USER_UPDATE)
                                .param("username", "username")
                                .with(ADMIN()))
                .andExpect(status().isOk())
                .andExpect(view().name(USER_EDIT_VIEW))
                .andReturn();
        mockMvc
                .perform(get(USER_UPDATE).with(TRAINER()))
                .andExpect(status().isForbidden())
                .andReturn();
        mockMvc
                .perform(get(USER_UPDATE).with(USER()))
                .andExpect(status().isForbidden())
                .andReturn();

        verifyNoInteractions(userService);
    }

    @Test
    void updateUser_shouldNotUpdateUserWithException_whenCalled() throws Exception {
        doThrow(UsernameExistsException.class).when(userService).updateUser(anyLong(), any());

        mockMvc
                .perform(
                        post(USER_UPDATE)
                                .param("username", "username")
                                .param("firstName", "firstName")
                                .param("lastName", "lastName")
                                .param("password", "password")
                                .with(ADMIN()))
                .andExpect(status().isOk())
                .andExpect(view().name(USER_EDIT_VIEW))
                .andReturn();
        mockMvc
                .perform(get(USER_UPDATE).with(TRAINER()))
                .andExpect(status().isForbidden())
                .andReturn();
        mockMvc
                .perform(get(USER_UPDATE).with(USER()))
                .andExpect(status().isForbidden())
                .andReturn();

        verify(userService, times(1)).updateUser(anyLong(), any());
    }

    @Test
    void deleteUser_shouldDeleteUser_whenExists() throws Exception {
        doNothing().when(userService).deleteUserById(anyLong());

        mockMvc
                .perform(get(USER_DELETE).with(ADMIN()))
                .andExpect(status().is(HttpStatus.FOUND.value()))
                .andExpect(redirectedUrl(USERS))
                .andReturn();
        mockMvc
                .perform(get(USER_DELETE).with(TRAINER()))
                .andExpect(status().isForbidden())
                .andReturn();
        mockMvc
                .perform(get(USER_DELETE).with(USER()))
                .andExpect(status().isForbidden())
                .andReturn();

        verify(userService, times(1)).deleteUserById(anyLong());
    }

    @Test
    void cancelGroupClassRegistration_shouldDeleteUserFromGroupClass_whenExists() throws Exception {
        doNothing().when(groupClassService).cancelUserRegistration(anyLong(), anyLong());

        mockMvc
                .perform(get(USER_CANCEL_REGISTRATION).with(ADMIN()))
                .andExpect(status().is(HttpStatus.FOUND.value()))
                .andExpect(redirectedUrl(USER_INFO))
                .andReturn();
        mockMvc
                .perform(get(USER_CANCEL_REGISTRATION).with(TRAINER()))
                .andExpect(status().isForbidden())
                .andReturn();
        mockMvc
                .perform(get(USER_CANCEL_REGISTRATION).with(USER()))
                .andExpect(status().is(HttpStatus.FOUND.value()))
                .andExpect(redirectedUrl(USER_INFO))
                .andReturn();

        verify(groupClassService, times(2)).cancelUserRegistration(anyLong(), anyLong());
    }
}
