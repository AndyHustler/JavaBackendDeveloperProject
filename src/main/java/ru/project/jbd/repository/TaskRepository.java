package ru.project.jbd.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import ru.project.jbd.domain.dto.StatusCount;
import ru.project.jbd.domain.model.Task;

public interface TaskRepository extends JpaRepository<Task, Long>{

    List<Task> findAllByUserId(Long id);
    
    @Query("SELECT t FROM Task t WHERE t.taskGroup.id = :id ")
    List<Task> getAllByTaskGroupId(@Param("id") Long id);

    @Query("""
            SELECT 
                COUNT(CASE WHEN e.taskStatus = 'ToDo' THEN 1 END) AS toDo,
                COUNT(CASE WHEN e.taskStatus = 'InProgress' THEN 1 END) AS inProgress,
                COUNT(CASE WHEN e.taskStatus = 'Done' THEN 1 END) AS done
            FROM Task e
            """)
    StatusCount getStatisticByStatus();
}
