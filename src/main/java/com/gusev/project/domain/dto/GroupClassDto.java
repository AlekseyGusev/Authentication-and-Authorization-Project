package com.gusev.project.domain.dto;

import com.gusev.project.domain.entity.NamedEntity;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashSet;
import java.util.Set;

public class GroupClassDto extends NamedEntity {
    private String date;
    private TrainerDto trainer = new TrainerDto();
    private Set<UserDto> users = new HashSet<>();
    private final String DATE_TIME_PATTERN = "yyyy-MM-dd HH:mm";

    public GroupClassDto() {
        this.date = LocalDateTime.now().format(DateTimeFormatter.ofPattern(DATE_TIME_PATTERN));
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = LocalDateTime.parse(date).format(DateTimeFormatter.ofPattern(DATE_TIME_PATTERN));
    }

    public TrainerDto getTrainer() {
        return trainer;
    }

    public void setTrainer(TrainerDto trainer) {
        this.trainer = trainer;
    }

    public Set<UserDto> getUsers() {
        return users;
    }

    public void setUsers(Set<UserDto> users) {
        this.users = users;
    }

    public LocalDateTime dateToLocalDateTime() {
        return LocalDateTime.from(DateTimeFormatter.ofPattern(DATE_TIME_PATTERN).parse(date));
    }
}
