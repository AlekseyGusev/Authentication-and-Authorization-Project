package com.gusev.project.domain.dto;


import com.gusev.project.domain.Specialty;
import com.gusev.project.domain.entity.NamedEntity;
import com.gusev.project.domain.entity.PersonEntity;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static com.gusev.project.domain.Specialty.defaultTrainerSpecialty;

public class TrainerDto extends PersonEntity {
    private Set<Specialty> specialties = new HashSet<>();
    private Set<UserDto> users = new HashSet<>();
    private Set<GroupClassDto> groupClasses = new HashSet<>();

    public TrainerDto() {
    }

    public TrainerDto(String id) {
        if (id.isBlank()) {
            this.setId(null);
        } else {
            this.setId(Long.parseLong(id));
        }
    }

    public Set<Specialty> getSpecialties() {
        return specialties;
    }

    public void setSpecialties(Set<Specialty> specialties) {
        if (specialties.isEmpty()) {
            specialties = new HashSet<>(List.of(defaultTrainerSpecialty()));
        }
        this.specialties = specialties;
    }

    public Set<UserDto> getUsers() {
        return users;
    }

    public void setUsers(Set<UserDto> users) {
        this.users = users;
    }

    public Set<GroupClassDto> getGroupClasses() {
        return groupClasses;
    }

    public void setGroupClasses(Set<GroupClassDto> groupClasses) {
        this.groupClasses = groupClasses;
    }

    public String specialtiesToString() {
        return specialties.stream().map(NamedEntity::toString).collect(Collectors.joining(","));
    }

    @Override
    public String toString() {
        return getFirstName() + " " + getLastName();
    }
}
