package ru.project.jbd.domain.mapper;

import java.util.List;
import java.util.ArrayList;
import org.springframework.stereotype.Component;

import ru.project.jbd.domain.dto.TaskGroupDto;
import ru.project.jbd.domain.model.TaskGroup;

@Component
public class TaskGroupMapper {

    public TaskGroupDto toDto(TaskGroup taskGroup) {
        TaskGroupDto dto =  new TaskGroupDto();
        dto.setId(taskGroup.getId());
        dto.setName(taskGroup.getName()); 
        dto.setDescription(taskGroup.getDescription()); 
        dto.setUser(taskGroup.getUser().getEmail());
        return dto;
    }

    public TaskGroup fromDto(TaskGroupDto dto) {
        TaskGroup taskGroup = new TaskGroup();
        taskGroup.setName(dto.getName());
        taskGroup.setDescription(dto.getDescription());
        return taskGroup;
    }

    public List<TaskGroupDto> toDtoList(Iterable<TaskGroup> tasks) {
        List<TaskGroupDto> tList = new ArrayList<>();
        for (TaskGroup task : tasks) {
            tList.add(toDto(task));
        }
        return tList;
    }
}
