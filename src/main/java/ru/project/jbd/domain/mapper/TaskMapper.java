package ru.project.jbd.domain.mapper;

import java.util.List;
import java.util.ArrayList;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import ru.project.jbd.domain.dto.TaskDto;
import ru.project.jbd.domain.model.ETaskStatus;
import ru.project.jbd.domain.model.Task;
import ru.project.jbd.domain.service.TaskGroupService;

@Component
@RequiredArgsConstructor
public class TaskMapper {
    
    private final TaskGroupService taskGroupService;

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
        task.setTaskGroup(dto.getGroup() != null && dto.getUser() != null ? taskGroupService.getByNameAndUserEmail(dto.getGroup(), dto.getUser()) : null);
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
