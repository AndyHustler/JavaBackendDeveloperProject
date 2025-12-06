package ru.project.jbd.domain.service;

import java.util.List;

import ru.project.jbd.domain.dto.TaskDto;

public interface TaskService {

    List<TaskDto> listAll();

    List<TaskDto> listAllByUserId(Long id);

    List<TaskDto> listCurentUserTasks();

    List<TaskDto> listAllByGroupId(Long id);

    TaskDto getById(Long id);

    TaskDto create(TaskDto dto);

    TaskDto update(TaskDto dto);

    TaskDto setGroup(Long id, String group);

    TaskDto setStatus(Long id, String status);

    void delete(TaskDto dto);
}
