package com.gusev.project.service;


import com.gusev.project.domain.Trainer;
import com.gusev.project.domain.dto.TrainerDto;
import com.gusev.project.exception.UsernameExistsException;

import java.util.Optional;
import java.util.Set;

public interface TrainerService {
    TrainerDto findTrainerDtoById(Long trainerId);
    Optional<Trainer> findTrainerById(Long trainerId);
    Set<TrainerDto> findAllTrainers();
    boolean trainerExists(Long trainerId);

    boolean saveTrainer(TrainerDto trainerDto) throws UsernameExistsException;

    void deleteTrainerById(Long trainerId);
}
