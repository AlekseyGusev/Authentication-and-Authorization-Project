package com.gusev.project.repository;

import com.gusev.project.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.Set;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);
    Set<User> findAllByTrainerIsNull();
    Set<User> findAllByTrainerId(Long trainerId);
    boolean existsByUsername(String username);
    Set<User> findAllByGroupClassesId(Long groupClassId);
}
