package ru.project.jbd.domain.mapper;

import java.util.List;
import java.util.ArrayList;
import org.springframework.stereotype.Component;

import ru.project.jbd.domain.dto.TaskDto;
import ru.project.jbd.domain.model.ETaskStatus;
import ru.project.jbd.domain.model.Task;

@Component
public class TaskMapper {

    public TaskDto toDto(Task task) {
        TaskDto dto = new TaskDto();
        dto.setId(task.getId());
        dto.setName(task.getName()); 
        dto.setDescription(task.getDescription()); 
        dto.setUser(task.getUser().getEmail()); 
        dto.setGroup(task.getTaskGroup().getName()); 
        dto.setStatus(task.getTaskStatus().name());
        return dto;
    }

    public Task fromDto(TaskDto dto) {
        Task task = new Task();
        task.setName(dto.getName());
        task.setDescription(dto.getDescription());
        task.setTaskStatus(dto.getStatus() == null ? ETaskStatus.ToDo : ETaskStatus.valueOf(dto.getStatus()));
        return task;
    }

    public List<TaskDto> toDtoList(Iterable<Task> tasks) {
        List<TaskDto> tList = new ArrayList<>();
        for (Task task : tasks) {
            tList.add(toDto(task));
        }
        return tList;
    }
}
