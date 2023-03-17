package com.gusev.project.domain.dto;

import com.gusev.project.domain.GroupClass;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.LinkedHashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class GroupClassDtoBuilder {

    private final TrainerDtoBuilder trainerDtoBuilder;

    @Autowired
    public GroupClassDtoBuilder(TrainerDtoBuilder trainerDtoBuilder) {
        this.trainerDtoBuilder = trainerDtoBuilder;
    }

    public GroupClassDto fromGroupClassToDto(GroupClass groupClass) {
        GroupClassDto dto = new GroupClassDto();
        dto.setId(groupClass.getId());
        dto.setName(groupClass.getName());
        dto.setDate(groupClass.getDate().toString());
        dto.setTrainer(trainerDtoBuilder.fromTrainerToDto(groupClass.getTrainer()));
        return dto;
    }

    public GroupClass fromDtoToGroupClass(GroupClassDto dto) {
        GroupClass groupClass = new GroupClass();
        groupClass.setName(dto.getName());
        groupClass.setDate(dto.dateToLocalDateTime());
        groupClass.setTrainer(trainerDtoBuilder.fromDtoToTrainer(dto.getTrainer()));
        return groupClass;
    }

    public Set<GroupClassDto> fromGroupClassSetToDtoSet(Set<GroupClass> groupClasses) {
        return groupClasses.stream()
                .map(this::fromGroupClassToDto)
                .collect(Collectors.toCollection(LinkedHashSet::new));
    }
}
