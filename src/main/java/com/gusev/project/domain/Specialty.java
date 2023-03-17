package com.gusev.project.domain;

import com.gusev.project.domain.entity.NamedEntity;

import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "specialties")
public class Specialty extends NamedEntity {
    public static Specialty defaultTrainerSpecialty() {
        Specialty specialty = new Specialty();
        specialty.setName("PERSONAL");
        return specialty;
    }
}
