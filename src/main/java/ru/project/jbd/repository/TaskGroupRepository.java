package ru.project.jbd.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import ru.project.jbd.domain.model.TaskGroup;

public interface TaskGroupRepository extends JpaRepository<TaskGroup, Long> {
    List<TaskGroup> findAllByUserId(Long id);
    Optional<TaskGroup> findByNameAndUserEmail(String name, String userEmail);
}
