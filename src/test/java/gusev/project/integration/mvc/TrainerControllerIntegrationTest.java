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
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@AutoConfigureMockMvc
public class TrainerControllerIntegrationTest {

    @MockBean
    private TrainerService trainerService;
    @MockBean
    private UserService userService;
    @MockBean
    private GroupClassService groupClassService;

    @Autowired
    private MockMvc mockMvc;

    @Test
    void getTrainers_shouldReturnTrainersView() throws Exception {
        when(trainerService.findAllTrainers()).thenReturn(trainerDtoSet());

        mockMvc
                .perform(get(TRAINERS).with(ADMIN()))
                .andExpect(status().isOk())
                .andExpect(view().name(TRAINERS_ALL_VIEW))
                .andReturn();
        mockMvc
                .perform(get(TRAINERS).with(TRAINER()))
                .andExpect(status().isOk())
                .andExpect(view().name(TRAINERS_ALL_VIEW))
                .andReturn();
        mockMvc
                .perform(get(TRAINERS).with(USER()))
                .andExpect(status().isOk())
                .andExpect(view().name(TRAINERS_ALL_VIEW))
                .andReturn();

        verify(trainerService, times(3)).findAllTrainers();
    }

    @Test
    void getTrainersUsers_shouldReturnTrainerUsers_whenCalled() throws Exception {
        when(trainerService.findTrainerDtoById(anyLong())).thenReturn(trainerDto());
        when(userService.findUsersByTrainerId(anyLong())).thenReturn(userDtoSet());

        mockMvc
                .perform(get(TRAINER_USERS).with(ADMIN()))
                .andExpect(status().isOk())
                .andExpect(view().name(TRAINER_USERS_VIEW))
                .andReturn();
        mockMvc
                .perform(get(TRAINER_USERS).with(TRAINER()))
                .andExpect(status().isOk())
                .andExpect(view().name(TRAINER_USERS_VIEW))
                .andReturn();
        mockMvc
                .perform(get(TRAINER_USERS).with(USER()))
                .andExpect(status().isForbidden())
                .andReturn();

        verify(trainerService, times(2)).findTrainerDtoById(anyLong());
        verify(userService, times(2)).findUsersByTrainerId(anyLong());
    }

    @Test
    void getTrainersGroupClasses_shouldReturnTrainerGroupClasses_whenCalled() throws Exception {
        when(trainerService.findTrainerDtoById(anyLong())).thenReturn(trainerDto());
        when(groupClassService.findAllTrainersGroupClasses(anyLong())).thenReturn(groupClassDtoSet());

        mockMvc
                .perform(get(TRAINER_GROUP_CLASSES).with(ADMIN()))
                .andExpect(status().isOk())
                .andExpect(view().name(TRAINER_GROUP_CLASSES_VIEW))
                .andReturn();
        mockMvc
                .perform(get(TRAINER_GROUP_CLASSES).with(TRAINER()))
                .andExpect(status().isOk())
                .andExpect(view().name(TRAINER_GROUP_CLASSES_VIEW))
                .andReturn();
        mockMvc
                .perform(get(TRAINER_GROUP_CLASSES).with(USER()))
                .andExpect(status().isForbidden())
                .andReturn();

        verify(trainerService, times(2)).findTrainerDtoById(anyLong());
        verify(groupClassService, times(2)).findAllTrainersGroupClasses(anyLong());
    }

    @Test
    void getAllUsers_shouldReturnAvailableUsers_whenCalled() throws Exception {
        when(userService.findAllUsersWithoutTrainer()).thenReturn(userDtoSet());

        mockMvc
                .perform(get(TRAINER_ADD_USER_ALL_AVAILABLE).with(ADMIN()))
                .andExpect(status().isOk())
                .andExpect(view().name(TRAINER_ADD_USER_VIEW))
                .andReturn();
        mockMvc
                .perform(get(TRAINER_ADD_USER_ALL_AVAILABLE).with(TRAINER()))
                .andExpect(status().isForbidden())
                .andReturn();
        mockMvc
                .perform(get(TRAINER_ADD_USER_ALL_AVAILABLE).with(USER()))
                .andExpect(status().isForbidden())
                .andReturn();

        verify(userService, times(1)).findAllUsersWithoutTrainer();
    }

    @Test
    void createTrainer_shouldReturnTrainerFormView_whenGetMethodCalled() throws Exception {
        mockMvc
                .perform(get(TRAINER_CREATE).with(ADMIN()))
                .andExpect(status().isOk())
                .andExpect(view().name(TRAINER_CREATE_VIEW))
                .andReturn();
        mockMvc
                .perform(get(TRAINER_CREATE).with(TRAINER()))
                .andExpect(status().isForbidden())
                .andReturn();
        mockMvc
                .perform(get(TRAINER_CREATE).with(USER()))
                .andExpect(status().isForbidden())
                .andReturn();
    }

    @Test
    void createTrainer_shouldCreateTrainer_whenPostMethodCalled() throws Exception {
        when(trainerService.saveTrainer(any())).thenReturn(true);

        mockMvc
                .perform(
                        post(TRAINER_CREATE)
                                .param("username", "username")
                                .param("firstName", "firstName")
                                .param("lastName", "lastName")
                                .param("password", "password")
                                .with(ADMIN()))
                .andExpect(status().is(HttpStatus.FOUND.value()))
                .andExpect(redirectedUrl(TRAINERS))
                .andReturn();
        mockMvc
                .perform(get(TRAINER_CREATE).with(TRAINER()))
                .andExpect(status().isForbidden())
                .andReturn();
        mockMvc
                .perform(get(TRAINER_CREATE).with(USER()))
                .andExpect(status().isForbidden())
                .andReturn();

        verify(trainerService, times(1)).saveTrainer(any());
    }

    @Test
    void createTrainer_shouldNotCreateTrainer_whenPostMethodCalled() throws Exception {
        mockMvc
                .perform(post(TRAINER_CREATE).with(ADMIN()))
                .andExpect(status().isOk())
                .andExpect(view().name(TRAINER_CREATE_VIEW))
                .andReturn();
        mockMvc
                .perform(get(TRAINER_CREATE).with(TRAINER()))
                .andExpect(status().isForbidden())
                .andReturn();
        mockMvc
                .perform(get(TRAINER_CREATE).with(USER()))
                .andExpect(status().isForbidden())
                .andReturn();

        verifyNoInteractions(trainerService);
    }

    @Test
    void createTrainer_shouldNotCreateTrainerWithError_whenPostMethodCalled() throws Exception {
        when(trainerService.saveTrainer(any())).thenReturn(false);

        mockMvc
                .perform(
                        post(TRAINER_CREATE)
                                .param("username", "username")
                                .param("firstName", "firstName")
                                .param("lastName", "lastName")
                                .param("password", "password")
                                .with(ADMIN()))
                .andExpect(status().is(HttpStatus.FOUND.value()))
                .andExpect(redirectedUrl(TRAINERS))
                .andReturn();
        mockMvc
                .perform(get(TRAINER_CREATE).with(TRAINER()))
                .andExpect(status().isForbidden())
                .andReturn();
        mockMvc
                .perform(get(TRAINER_CREATE).with(USER()))
                .andExpect(status().isForbidden())
                .andReturn();

        verify(trainerService, times(1)).saveTrainer(any());
    }

    @Test
    void addUserToTrainer_shouldAddUserToTrainer_whenExists() throws Exception {
        doNothing().when(userService).addTrainerToUser(anyLong(), anyLong());

        mockMvc
                .perform(get(TRAINER_ADD_USER).with(ADMIN()))
                .andExpect(status().is(HttpStatus.FOUND.value()))
                .andExpect(redirectedUrl(TRAINER_USERS))
                .andReturn();
        mockMvc
                .perform(get(TRAINER_ADD_USER).with(TRAINER()))
                .andExpect(status().isForbidden())
                .andReturn();
        mockMvc
                .perform(get(TRAINER_ADD_USER).with(USER()))
                .andExpect(status().isForbidden())
                .andReturn();

        verify(userService, times(1)).addTrainerToUser(anyLong(), anyLong());
    }

    @Test
    void deleteTrainer_shouldDeleteTrainer_whenExists() throws Exception {
        doNothing().when(trainerService).deleteTrainerById(anyLong());

        mockMvc
                .perform(get(TRAINER_DELETE).with(ADMIN()))
                .andExpect(status().is(HttpStatus.FOUND.value()))
                .andExpect(redirectedUrl(TRAINERS))
                .andReturn();
        mockMvc
                .perform(get(TRAINER_DELETE).with(TRAINER()))
                .andExpect(status().isForbidden())
                .andReturn();
        mockMvc
                .perform(get(TRAINER_DELETE).with(USER()))
                .andExpect(status().isForbidden())
                .andReturn();

        verify(trainerService, times(1)).deleteTrainerById(anyLong());
    }

    @Test
    void deleteTrainersUser_shouldDeleteTrainersUser_whenExists() throws Exception {
        doNothing().when(userService).deleteUsersTrainer(anyLong(), anyLong());

        mockMvc
                .perform(get(TRAINER_DELETE_USER).with(ADMIN()))
                .andExpect(status().is(HttpStatus.FOUND.value()))
                .andExpect(redirectedUrl(TRAINER_USERS))
                .andReturn();
        mockMvc
                .perform(get(TRAINER_DELETE).with(TRAINER()))
                .andExpect(status().isForbidden())
                .andReturn();
        mockMvc
                .perform(get(TRAINER_DELETE).with(USER()))
                .andExpect(status().isForbidden())
                .andReturn();

        verify(userService, times(1)).deleteUsersTrainer(anyLong(), anyLong());
    }
}
