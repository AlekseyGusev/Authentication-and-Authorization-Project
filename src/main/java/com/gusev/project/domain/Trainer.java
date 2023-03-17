package com.gusev.project.domain;


import com.gusev.project.domain.entity.PersonEntity;
import com.gusev.project.domain.security.Role;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Entity
@Table(name = "trainers")
public class Trainer extends PersonEntity {

    @OneToMany(mappedBy = "trainer", fetch = FetchType.EAGER)
    private Set<User> users;

    @OneToMany(mappedBy = "trainer", fetch = FetchType.EAGER)
    private Set<GroupClass> groupClasses;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "trainer_specialties",
                joinColumns = @JoinColumn(name = "trainer_id"),
                inverseJoinColumns = @JoinColumn(name = "specialty_id"))
    private Set<Specialty> specialties;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "role_name_id")
    private Role role;

    @Column(name = "enabled")
    private boolean enabled;

    public Trainer() {
    }

    public Set<Specialty> getSpecialty() {
        if (specialties == null) {
            specialties = new HashSet<>();
        }
        return this.specialties;
    }

    public void setSpecialty(Set<Specialty> specialties) {
        this.specialties = specialties;
    }

    public Set<User> getUsers() {
        if (users == null) {
            users = new HashSet<>();
        }
        return this.users;
    }

    public void setUsers(Set<User> users) {
        this.users = users;
    }

    public Set<GroupClass> getGroupClasses() {
        if (groupClasses == null) {
            groupClasses = new HashSet<>();
        }
        return this.groupClasses;
    }

    public void setGroupClasses(Set<GroupClass> groupClasses) {
        this.groupClasses = groupClasses;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public void removeUsers() {
        if (users != null) {
            if (!users.isEmpty()) {
                users.forEach(User::removeTrainer);
            }
            users.clear();
        }
    }

    public void removeUser(Long userId) {
    }

    public void removeTrainerFromGroupClasses() {
        if (groupClasses != null) {
            if (!groupClasses.isEmpty()) {
                groupClasses.forEach(GroupClass::removeTrainer);
            }
            groupClasses.clear();
        }
    }

    public void removeSpecialty() {
        specialties.clear();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Trainer trainer = (Trainer) o;
        return Objects.equals(this.getId(), trainer.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.getId());
    }
}
