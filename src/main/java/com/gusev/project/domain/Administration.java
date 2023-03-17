package com.gusev.project.domain;


import com.gusev.project.domain.entity.PersonEntity;
import com.gusev.project.domain.security.Role;

import javax.persistence.*;

@Entity
@Table(name = "administrations")
public class Administration extends PersonEntity {

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "role_name_id")
    private Role role;

    @Column(name = "enabled")
    private boolean enabled;

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
}
