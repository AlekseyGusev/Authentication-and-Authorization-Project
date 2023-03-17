package com.gusev.project.repository;


import com.gusev.project.domain.GroupClass;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Set;

public interface GroupClassRepository extends JpaRepository<GroupClass, Long> {
    Set<GroupClass> findAllByOrderByDateAscUsersAsc();
    Set<GroupClass> findAllByTrainerIdOrderByDate(Long trainerId);
}
