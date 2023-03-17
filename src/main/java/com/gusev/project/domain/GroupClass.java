package com.gusev.project.domain;

import com.gusev.project.domain.entity.NamedEntity;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

@Entity
@Table(name = "group_classes")
public class GroupClass extends NamedEntity {

    @Column(name = "date")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm")
    private LocalDateTime date;

    @ManyToOne
    @JoinColumn(name = "trainer_id")
    private Trainer trainer;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "user_group_classes",
            joinColumns = @JoinColumn(name = "group_class_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id")
    )
    private Set<User> users = new HashSet<>();

    public GroupClass() {
    }

    public LocalDateTime getDate() {
        return date;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }

    public Trainer getTrainer() {
        return trainer;
    }

    public void setTrainer(Trainer trainer) {
        this.trainer = trainer;
    }

    public Set<User> getUsers() {
        if (this.users == null) {
            this.users = new HashSet<>();
        }
        return this.users;
    }

    public void addUser(User user) {
        getUsers().add(user);
    }

    public void setUsers(Set<User> users) {
        this.users = users;
    }

    public void removeTrainer() {
        this.trainer = null;
    }

    public void removeUsers() {
        users.clear();
    }

    public void removeUser(Long userId) {
        Optional<User> user = users.stream().filter(u -> u.getId().equals(userId)).findAny();
        user.ifPresent(u -> users.remove(u));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        GroupClass that = (GroupClass) o;
        return Objects.equals(this.getId(), that.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.getId());
    }
}
