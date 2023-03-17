package com.gusev.project.domain.security;


import com.gusev.project.domain.entity.BaseEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import static com.epam.rd.capstoneproject.domain.security.Roles.*;

@Entity
@Table(name = "Roles")
public class Role extends BaseEntity {

    @Column(name = "role_name")
    private String ROLE_NAME;

    public Role() {
    }

    public String getName() {
        return ROLE_NAME;
    }

    public void setName(String ROLE_NAME) {
        this.ROLE_NAME = ROLE_NAME;
    }

    public static Role defaultUserRole() {
        Role role = new Role();
        role.setId(3L);
        role.setName(USER);
        return role;
    }

    public static Role defaultTrainerRole() {
        Role role = new Role();
        role.setId(2L);
        role.setName(TRAINER);
        return role;
    }
}
