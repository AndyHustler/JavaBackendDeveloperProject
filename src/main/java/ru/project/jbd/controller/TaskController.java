package ru.project.jbd.controller;

import java.util.List;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import ru.project.jbd.domain.dto.StatusCount;
import ru.project.jbd.domain.dto.TaskDto;
import ru.project.jbd.domain.service.TaskService;

@Controller
@RequestMapping("/app/task-api")
@RequiredArgsConstructor
@Tag(name = "Задачи", description = "API для выполнения операций с задачами")
public class TaskController {
    
    private final TaskService taskService;

    @GetMapping("/tasks-list")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Просмотр списка всех задач доступен авторизованным пользователям с ролью ADMIN")
    public List<TaskDto> listTasks() {
        return taskService.listAll();
    }

    @GetMapping("/task/{id}")
    @Operation(summary = "Поиск задачи по id доступен авторизованным пользователям")
    public TaskDto findTaskById(@PathVariable(name = "id") Long id) {
        return taskService.getById(id);
    }

    @GetMapping("/user/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Поиск задач по id пользователя доступен авторизованным пользователям с ролью ADMIN")
    public List<TaskDto> listTasksByUserId(@PathVariable(name = "id") Long id) {
        return taskService.listAllByUserId(id);
    }

    @GetMapping("/myTasks")
    @Operation(summary = "Поиск задач для текущего пользователя доступен авторизованным пользователям")
    public List<TaskDto> listCurentUserTasks() {
        return taskService.listCurentUserTasks();
    }

    @GetMapping("/group/{id}")
    @Operation(summary = "Поиск задач по id группы доступен авторизованным пользователям")
    public List<TaskDto> listTasksByGroupId(@PathVariable(name = "id") Long id) {
        return taskService.listAllByGroupId(id);
    }

    @GetMapping("/taskStatisticByStatus")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Получение статистики по статусам задач доступно авторизованным пользователям с ролью ADMIN")
    public StatusCount getStatisticByStatus() {
        return taskService.getStatisticByStatus();
    }

    @PostMapping("/create")
    @Operation(summary = "Созданение задачи")
    public TaskDto create(@Valid @RequestBody TaskDto taskDto) {
        return taskService.create(taskDto);
    }

    @PostMapping("/update")
    @Operation(summary = "Изменение задачи")
    public TaskDto update(@Valid @RequestBody TaskDto taskDto) {
        return taskService.update(taskDto);
    }

    @PostMapping("/update/{id}/grop/{grop}")
    @Operation(summary = "Изменение принадлежности к группе задач")
    public TaskDto setGroup(@PathVariable(name = "id") Long id, @PathVariable(name = "grop") String grop) {
        return taskService.setGroup(id, grop);
    }

    @PostMapping("/update/{id}/status/{status}")
    @Operation(summary = "Изменение статуса задачи")
    public TaskDto setStatus(@PathVariable(name = "id") Long id, @PathVariable(name = "status") String status) {
        return taskService.setStatus(id, status);
    }

    @PostMapping("/delete")
    @Operation(summary = "Удаление задачи")
    public void delete(@RequestBody TaskDto taskDto) {
        taskService.delete(taskDto);
    }
}
