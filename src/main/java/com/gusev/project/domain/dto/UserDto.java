package com.gusev.project.domain.dto;

import com.gusev.project.domain.entity.PersonEntity;

import java.util.HashSet;
import java.util.Set;

public class UserDto extends PersonEntity {
    private TrainerDto trainer;
    private Set<GroupClassDto> groupClasses = new HashSet<>();

    public TrainerDto getTrainer() {
        return trainer;
    }

    public void setTrainer(TrainerDto trainer) {
        this.trainer = trainer;
    }

    public Set<GroupClassDto> getGroupClasses() {
        return groupClasses;
    }

    public void setGroupClasses(Set<GroupClassDto> groupClasses) {
        this.groupClasses = groupClasses;
    }

    public boolean contains(String groupClassName) {
        return groupClasses.stream().anyMatch(g -> g.getName().equals(groupClassName));
    }
}
