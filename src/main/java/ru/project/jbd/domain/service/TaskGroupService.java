package ru.project.jbd.domain.service;

import java.util.List;

import ru.project.jbd.domain.dto.TaskGroupDto;
import ru.project.jbd.domain.model.TaskGroup;

public interface TaskGroupService {

    List<TaskGroupDto> listAll();

    List<TaskGroupDto> listCurentUserTaskGroups();

    List<TaskGroupDto> listAllByUserId(Long id);

    TaskGroupDto getById(Long id);

    TaskGroup getByNameAndUserEmail(String name, String userEmail);

    TaskGroupDto create(TaskGroupDto taskGroup);

    TaskGroupDto update(TaskGroupDto taskGroup);

    void delete(TaskGroupDto taskGroup);
}
