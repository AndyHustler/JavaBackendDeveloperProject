package ru.project.jbd.controller;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import ru.project.jbd.domain.model.User;
import ru.project.jbd.domain.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/app/user-api")
@RequiredArgsConstructor
@Tag(name = "Контроллер пользователей", description = "API для выполнения операций с пользователями. Доступно только администратору")
public class UserController {

    private final UserService service;

    @GetMapping("/users-list")
    @Operation(summary = "Просмотр списка всех пользователей")
    public List<User> listUsers() {
        return service.listUsers();
    }

    @GetMapping("/user/{id}")
    @Operation(summary = "Поиск пользователя по ID")
    public User findUser(@PathVariable(name = "id") Long id) {
        return service.getById(id);
    }

    @PostMapping(value = "/delete")
    @Operation(summary = "Удаление пользователей")
    public void deleteUser(@RequestBody User user) {
        service.delete(user);
    }

    @PostMapping(value = "/userToAdmin")
    @Operation(summary = "Повышение пользователя до администратора")
    public User userToAdmin(@RequestBody User user) {
        return service.setRoleAdmin(user);
    }
}
