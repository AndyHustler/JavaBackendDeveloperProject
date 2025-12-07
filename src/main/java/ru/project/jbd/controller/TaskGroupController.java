package ru.project.jbd.controller;

import java.util.List;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import ru.project.jbd.domain.dto.TaskGroupDto;
import ru.project.jbd.domain.service.TaskGroupService;

@RestController
@RequestMapping("/app/task-group-api")
@RequiredArgsConstructor
@Tag(name = "Группы задач", description = "API для выполнения операций с группами задач")
public class TaskGroupController {
    
    private final TaskGroupService service;

    @GetMapping("/groups-list")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Просмотр списка всех групп задач доступен авторизованным пользователям с ролью ADMIN")
    public List<TaskGroupDto> listAllTasks() {
        return service.listAll();
    }

    @GetMapping("/curent-user-groups-list")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Просмотр списка всех групп задач доступен авторизованным пользователям с ролью ADMIN")
    public List<TaskGroupDto> listCurentUserTaskGroups() {
        return service.listCurentUserTaskGroups();
    }

    @PostMapping("/create")
    @Operation(summary = "Созранение группы задач")
    public TaskGroupDto create(@RequestBody TaskGroupDto dto) {
        return service.create(dto);
    }

    @PostMapping("/update")
    @Operation(summary = "Изменение группы задач")
    public TaskGroupDto update(@RequestBody TaskGroupDto dto) {
        return service.update(dto);
    }

    @PostMapping("/delete")
    @Operation(summary = "Удаление группы задач")
    public void delete(@RequestBody TaskGroupDto dto) {
        service.delete(dto);
    }
}