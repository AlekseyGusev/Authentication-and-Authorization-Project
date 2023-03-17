package com.gusev.project.service.impl;

import com.gusev.project.domain.Trainer;
import com.gusev.project.domain.dto.TrainerDto;
import com.gusev.project.domain.dto.TrainerDtoBuilder;
import com.gusev.project.repository.TrainerRepository;
import com.gusev.project.service.TrainerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.Set;

@Service
@Transactional(readOnly = true)
public class DefaultTrainerService implements TrainerService {

    private final TrainerRepository trainerRepository;
    private final TrainerDtoBuilder trainerDtoBuilder;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public DefaultTrainerService(TrainerRepository trainerRepository, TrainerDtoBuilder trainerDtoBuilder, PasswordEncoder passwordEncoder) {
        this.trainerRepository = trainerRepository;
        this.trainerDtoBuilder = trainerDtoBuilder;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public TrainerDto findTrainerDtoById(Long trainerId) {
        return trainerRepository.findById(trainerId).map(trainerDtoBuilder::fromTrainerToDto).orElseGet(TrainerDto::new);
    }

    @Override
    public Optional<Trainer> findTrainerById(Long trainerId) {
        return trainerRepository.findById(trainerId);
    }

    @Override
    public Set<TrainerDto> findAllTrainers() {
        return trainerDtoBuilder.fromTrainerSetToDtoSet(trainerRepository.findAllByOrderById());
    }

    @Override
    public boolean trainerExists(Long trainerId) {
        return trainerRepository.existsById(trainerId);
    }

    @Override
    @Transactional
    public boolean saveTrainer(TrainerDto dto) {
        if (trainerRepository.existsByUsername(dto.getUsername())) {
            return false;
        }
        dto.setPassword(passwordEncoder.encode(dto.getPassword()));
        trainerRepository.save(trainerDtoBuilder.fromDtoToTrainer(dto));
        return true;
    }

    @Override
    @Transactional
    public void deleteTrainerById(Long trainerId) {
        Optional<Trainer> trainerOpt = trainerRepository.findById(trainerId);
        if (trainerOpt.isPresent()) {
            Trainer trainer = trainerOpt.get();
            trainer.removeUsers();
            trainer.removeTrainerFromGroupClasses();
            trainerRepository.delete(trainer);
        }
    }
}
