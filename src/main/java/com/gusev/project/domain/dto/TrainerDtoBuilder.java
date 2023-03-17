package com.gusev.project.domain.dto;

import com.gusev.project.domain.Trainer;
import com.gusev.project.domain.security.Role;
import org.springframework.stereotype.Component;

import java.util.LinkedHashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class TrainerDtoBuilder {
    
    public TrainerDto fromTrainerToDto(Trainer trainer) {
        if (trainer != null) {
            TrainerDto dto = new TrainerDto();
            dto.setId(trainer.getId());
            dto.setFirstName(trainer.getFirstName());
            dto.setLastName(trainer.getLastName());
            dto.setUsername(trainer.getUsername());
            dto.setSpecialties(trainer.getSpecialty());
            return dto;
        }
        return null;
    }

    public Trainer fromDtoToTrainer(TrainerDto dto) {
        Trainer trainer = new Trainer();
        trainer.setFirstName(dto.getFirstName());
        trainer.setLastName(dto.getLastName());
        trainer.setUsername(dto.getUsername());
        trainer.setPassword(dto.getPassword());
        trainer.setRole(Role.defaultTrainerRole());
        trainer.setEnabled(true);
        return trainer;
    }

    public Set<TrainerDto> fromTrainerSetToDtoSet(Set<Trainer> trainers) {
        return trainers.stream()
                .map(this::fromTrainerToDto)
                .collect(Collectors.toCollection(LinkedHashSet::new));
    }
}