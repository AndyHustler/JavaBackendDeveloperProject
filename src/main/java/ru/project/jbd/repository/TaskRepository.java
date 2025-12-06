package ru.project.jbd.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import ru.project.jbd.domain.model.Task;

public interface TaskRepository extends JpaRepository<Task, Long>{

    List<Task> findAllByUserId(Long id);
    List<Task> findAllByTaskGroupId(Long id);
}
