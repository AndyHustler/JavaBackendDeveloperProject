package ru.project.jbd.domain.service;

import java.util.List;

import ru.project.jbd.domain.model.User;

public interface UserService {

    User save(User user);
    User setRoleAdmin(User user);
    User getByEmail(String email);
    User getCurrentUser();
    List<User> listUsers();
    User getById(Long id);
    void delete(User user);
}
