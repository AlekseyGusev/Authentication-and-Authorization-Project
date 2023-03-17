package com.gusev.project.domain;


import com.gusev.project.domain.entity.PersonEntity;
import com.gusev.project.domain.security.Role;

import javax.persistence.*;
import java.util.*;

@Entity
@Table(name = "users")
public class User extends PersonEntity {

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "trainer_id")
    private Trainer trainer;

    @ManyToMany(mappedBy = "users", fetch = FetchType.EAGER)
    private Set<GroupClass> groupClasses = new HashSet<>();

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "role_name_id")
    private Role role;

    @Column(name = "enabled")
    private boolean enabled;

    public User() {
    }

    public Trainer getTrainer() {
        return trainer;
    }

    public void setTrainer(Trainer trainer) {
        this.trainer = trainer;
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

    public void addGroupClass(GroupClass groupClass) {
        groupClasses.add(groupClass);
    }

    public void removeGroupClass(GroupClass groupClass) {
        groupClass.getUsers().remove(this);
    }

    public void removeFromGroupClasses() {
        this.setGroupClasses(null);
    }

    public void removeTrainer() {
        setTrainer(null);
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return Objects.equals(this.getId(), user.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.getId());
    }
}
