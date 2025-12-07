package ru.project.jbd.domain.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import ru.project.jbd.domain.dto.StatusCount;
import ru.project.jbd.domain.dto.TaskDto;
import ru.project.jbd.domain.mapper.TaskMapper;
import ru.project.jbd.domain.model.ETaskStatus;
import ru.project.jbd.domain.model.Task;
import ru.project.jbd.domain.model.TaskGroup;
import ru.project.jbd.domain.model.User;
import ru.project.jbd.domain.service.TaskGroupService;
import ru.project.jbd.domain.service.TaskService;
import ru.project.jbd.domain.service.UserService;
import ru.project.jbd.exception.ResourceNotFoundException;
import ru.project.jbd.exception.message.NotFoundMessage;
import ru.project.jbd.repository.TaskRepository;
import ru.project.jbd.exception.AccessDeniedException;

@RequiredArgsConstructor
@Service
public class TaskServiceImpl implements TaskService{

    private final TaskRepository repository;
    private final TaskMapper taskMapper;
    private final UserService userService;
    private final TaskGroupService taskGroupService;

    /**
     * Получение списка задач
     * 
     * @param id ID пользователя
     * @return List<TaskDto> - список задач
     */
    @Override
    public List<TaskDto> listAll() {
        return taskMapper.toDtoList(repository.findAll());
    }

    /**
     * Получение списка задач по ID пользователя
     * 
     * @param id ID пользователя
     * @return List<TaskDto> - список задач
     * @exception AccessDeniedException Отказано в доступе: вы неможете получить список задач по ID
     */
    @Override
    public List<TaskDto> listAllByUserId(Long id) {
        return taskMapper.toDtoList(repository.findAllByUserId(id));
    }

    @Override
    public List<TaskDto> listCurentUserTasks() {
        User user = userService.getCurrentUser();
        return listAllByUserId(user.getId());
    }

    @Override
    public List<TaskDto> listAllByGroupId(Long id) {
        return taskMapper.toDtoList(repository.getAllByTaskGroupId(id));
    }

    @Override
    public TaskDto getById(Long id) {
        return taskMapper.toDto(getTaskById(id));
    }

    @Override
    public TaskDto create(TaskDto dto) {
        Task task = taskMapper.fromDto(dto);
        task.setUser(getCurrentUser());
        return taskMapper.toDto(repository.save(task));
    }

     @Override
    public TaskDto update(TaskDto dto) {
        Task task = getTaskById(dto.getId());
        isOwner(task);
        task.setName(dto.getName());
        task.setDescription(dto.getDescription());
        return taskMapper.toDto(repository.save(task));
    }

    @Override
    public void delete(TaskDto dto) {
        Task task = getTaskById(dto.getId());
        if(isOwner(task)) repository.delete(task);
    }

    private User getCurrentUser() {
        return userService.getCurrentUser();
    }

    private Task getTaskById(Long id) {
        return repository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException(NotFoundMessage.TASK.getMessage())
            );
    }

    private boolean isOwner(Task tg) {
        if(getCurrentUser().getId() == tg.getUser().getId()) {
            return true;
        } else throw new AccessDeniedException("Отказано в доступе");
    }

    @Override
    public TaskDto setGroup(Long id, String group) {
        TaskGroup taskGroup = taskGroupService.getByNameAndUserEmail(group, getCurrentUser().getEmail());
        Task task = getTaskById(id);
        task.setTaskGroup(taskGroup);
        return taskMapper.toDto(repository.save(task));
    }

    @Override
    public TaskDto setStatus(Long id, String status) {
        Task task = getTaskById(id);
        task.setTaskStatus(ETaskStatus.valueOf(status));
        return taskMapper.toDto(repository.save(task));
    }

    @Override
    public StatusCount getStatisticByStatus() {
        return repository.getStatisticByStatus();
    }
}
