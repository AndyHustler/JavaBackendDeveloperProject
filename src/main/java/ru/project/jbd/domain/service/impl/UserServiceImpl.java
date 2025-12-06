package ru.project.jbd.domain.service.impl;

import java.util.List;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import lombok.AllArgsConstructor;
import ru.project.jbd.domain.model.ERole;
import ru.project.jbd.domain.model.User;
import ru.project.jbd.domain.service.UserService;
import ru.project.jbd.repository.UserRepository;

@Service
@AllArgsConstructor
public class UserServiceImpl implements UserService {
    
    private final UserRepository repository;
    
    /**
     * Сохранение пользователя
     * 
     * @param User
     * @return сохраненный пользователь
     */
    public User save(User user) {
        return repository.save(user);
    }


    /**
     * Добавление пользователю роли администратора
     *
     * @param User
     * @return обновленный пользователь
     */
    public User setRoleAdmin(User user) {
        if(user.hasRole(ERole.ROLE_ADMIN)) {
            user.setRoles(ERole.admin());
            return save(user);
        }
        return user;
    }

    /**
     * Получение пользователя по имени пользователя
     *
     * @param email email пользователя
     * @return пользователь
     */
    public User getByEmail(String email) {
        return repository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("Пользователь не найден"));

    }

    /**
     * Получение текущего пользователя
     *
     * @return текущий пользователь
     */
    public User getCurrentUser() {
        // Получение пользователя из контекста Spring Security
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        return getByEmail(username);
    }

    /**
     * Получение списка пользователей
     *
     * @return список пользователей
     */
    @Override
    public List<User> listUsers() {
        return repository.findAll();
    }

    /**
     * Поиск пользователя по ID
     *
     * @return пользователь
     * Если пользователь не найден, генерируется исключение
     * @exception UsernameNotFoundException
     */
    @Override
    public User getById(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new UsernameNotFoundException("Пользователь не найден"));
    }

    /**
     * Удаление пользователя
     *
     * @param User
     */
    @Override
    public void delete(User user) {
        repository.delete(user);
    }
}
