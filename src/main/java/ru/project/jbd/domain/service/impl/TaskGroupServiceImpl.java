package ru.project.jbd.domain.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import ru.project.jbd.domain.dto.TaskGroupDto;
import ru.project.jbd.domain.mapper.TaskGroupMapper;
import ru.project.jbd.domain.model.TaskGroup;
import ru.project.jbd.domain.model.User;
import ru.project.jbd.domain.service.TaskGroupService;
import ru.project.jbd.domain.service.UserService;
import ru.project.jbd.exception.AccessDeniedException;
import ru.project.jbd.exception.ResourceNotFoundException;
import ru.project.jbd.exception.message.NotFoundMessage;
import ru.project.jbd.repository.TaskGroupRepository;

@Service
@RequiredArgsConstructor
public class TaskGroupServiceImpl implements TaskGroupService{

    private final TaskGroupRepository repository;
    private final UserService userService;
    private final TaskGroupMapper mapper;

    @Override
    public List<TaskGroupDto> listAll() {
        return mapper.toDtoList(repository.findAll());
    }

    @Override
    public List<TaskGroupDto> listCurentUserTaskGroups() {
        return listAllByUserId(getCurrentUser().getId());
    }

    @Override
    public List<TaskGroupDto> listAllByUserId(Long id) {
        return mapper.toDtoList(repository.findAllByUserId(id));
    }

    @Override
    public TaskGroupDto getById(Long id) {
        return mapper.toDto(getTaskGroupById(id));
    }

    @Override
    public TaskGroupDto create(TaskGroupDto dto) {
        TaskGroup tg = mapper.fromDto(dto);
        tg.setUser(getCurrentUser());
        return mapper.toDto(repository.save(tg));
    }

    @Override
    public TaskGroupDto update(TaskGroupDto dto) {
        TaskGroup tg = getTaskGroupById(dto.getId());
        isOwner(tg);
        tg.setName(dto.getName());
        tg.setDescription(dto.getDescription());
        return mapper.toDto(repository.save(tg));
    }

    @Override
    public void delete(TaskGroupDto dto) {
        TaskGroup tg = getTaskGroupById(dto.getId());
        if(isOwner(tg)) repository.delete(tg);
    }

    @Override
    public TaskGroup getByNameAndUserEmail(String name, String userEmail) {
        return repository.findByNameAndUserEmail(name, userEmail)
                .orElseThrow(() -> new ResourceNotFoundException(NotFoundMessage.TASK_GROUP.getMessage()));
    }

    private TaskGroup getTaskGroupById(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(NotFoundMessage.TASK_GROUP.getMessage()));
    }

    private User getCurrentUser() {
        return userService.getCurrentUser();
    }

    private boolean isOwner(TaskGroup tg) {
        if(getCurrentUser().getId() == tg.getUser().getId()) {
            return true;
        } else throw new AccessDeniedException("Отказано в доступе");
    }
}
