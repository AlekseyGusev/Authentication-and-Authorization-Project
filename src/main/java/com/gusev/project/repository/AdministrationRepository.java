package com.gusev.project.repository;

import com.gusev.project.domain.Administration;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AdministrationRepository extends JpaRepository<Administration, Long> {
    Optional<Administration> findByUsername(String username);
}
