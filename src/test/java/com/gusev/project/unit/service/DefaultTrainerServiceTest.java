package com.gusev.project.unit.service;

import com.gusev.project.domain.Trainer;
import com.gusev.project.domain.dto.TrainerDto;
import com.gusev.project.domain.dto.TrainerDtoBuilder;
import com.gusev.project.repository.TrainerRepository;
import com.gusev.project.service.impl.DefaultTrainerService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.LinkedList;
import java.util.Optional;
import java.util.Set;

import static com.gusev.project.util.Utils.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DefaultTrainerServiceTest {

    @Mock
    private TrainerRepository trainerRepository;
    @Mock
    private TrainerDtoBuilder trainerDtoBuilder;
    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private DefaultTrainerService service;

    @Test
    void findTrainerDtoById_shouldFindTrainerById_whenExists() {
        Trainer trainer = trainer();
        TrainerDto trainerDto = trainerDto();
        trainerDto.setId(ID);
        when(trainerRepository.findById(ID)).thenReturn(Optional.of(trainer));
        when(trainerDtoBuilder.fromTrainerToDto(trainer)).thenReturn(trainerDto);

        TrainerDto actual = service.findTrainerDtoById(ID);

        assertNotNull(actual);
        assertEquals(ID, actual.getId());
        verify(trainerRepository).findById(ID);
        verify(trainerDtoBuilder).fromTrainerToDto(trainer);
    }

    @Test
    void findTrainerDtoById_shouldNotFindTrainerById_whenDoesntExist() {
        when(trainerRepository.findById(ID)).thenReturn(Optional.empty());

        TrainerDto actual = service.findTrainerDtoById(ID);

        assertNotNull(actual);
        assertNull(actual.getId());
        verify(trainerRepository).findById(ID);
        verifyNoInteractions(trainerDtoBuilder);
    }

    @Test
    void findAllTrainers_shouldFindAllTrainersOrderById_whenCalled() {
        Set<Trainer> trainers = trainerSet();
        Set<TrainerDto> expected = trainerDtoSet();
        when(trainerRepository.findAllByOrderById()).thenReturn(trainers);
        when(trainerDtoBuilder.fromTrainerSetToDtoSet(trainers)).thenReturn(expected);

        Set<TrainerDto> actual = service.findAllTrainers();

        assertNotNull(actual);
        assertEquals(expected.size(), actual.size());

        LinkedList<TrainerDto> list = new LinkedList<>(actual);

        assertTrue(list.get(0).getId() < list.get(1).getId());
        verify(trainerRepository).findAllByOrderById();
        verify(trainerDtoBuilder).fromTrainerSetToDtoSet(trainers);
    }

    @Test
    void saveTrainer_shouldSaveTrainer_whenCalled() {
        TrainerDto trainerDto = trainerDto();
        Trainer trainer = trainer();
        when(trainerRepository.existsByUsername(USERNAME)).thenReturn(false);
        when(passwordEncoder.encode(PASSWORD)).thenReturn(ENCODED_PASSWORD);
        when(trainerDtoBuilder.fromDtoToTrainer(trainerDto)).thenReturn(trainer);

        boolean actual = service.saveTrainer(trainerDto);

        assertTrue(actual);
        assertEquals(ENCODED_PASSWORD, trainerDto.getPassword());
        verify(trainerRepository).existsByUsername(USERNAME);
        verify(passwordEncoder).encode(PASSWORD);
        verify(trainerDtoBuilder).fromDtoToTrainer(trainerDto);
    }

    @Test
    void saveTrainer_shouldNotSaveTrainer_whenExists() {
        TrainerDto trainerDto = trainerDto();
        when(trainerRepository.existsByUsername(USERNAME)).thenReturn(true);

        boolean actual = service.saveTrainer(trainerDto);

        assertFalse(actual);
        verifyNoInteractions(passwordEncoder, trainerDtoBuilder);
        verifyNoMoreInteractions(trainerRepository);
    }

    @Test
    void deleteTrainerById_shouldDeleteTrainerById_whenExists() {
        Trainer trainer = trainer();
        when(trainerRepository.findById(ID)).thenReturn(Optional.of(trainer));

        service.deleteTrainerById(ID);

        assertTrue(trainer.getUsers().isEmpty());
        assertTrue(trainer.getGroupClasses().isEmpty());
        verify(trainerRepository).delete(trainer);
    }

    @Test
    void deleteTrainerById_shouldNotDeleteTrainerById_whenDoesntExist() {
        when(trainerRepository.findById(ID)).thenReturn(Optional.empty());

        service.deleteTrainerById(ID);

        verify(trainerRepository, only()).findById(ID);
    }
}