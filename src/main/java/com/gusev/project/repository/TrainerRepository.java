package com.gusev.project.repository;


import com.gusev.project.domain.Trainer;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.Set;

public interface TrainerRepository extends JpaRepository<Trainer, Long> {
    Optional<Trainer> findByUsername(String username);
    Set<Trainer> findAllByOrderById();
    boolean existsByUsername(String username);
}
