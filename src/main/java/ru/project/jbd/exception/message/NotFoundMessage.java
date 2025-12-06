package ru.project.jbd.exception.message;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum NotFoundMessage {
    TASK("Задача не найдена."),
    TASK_GROUP("Группа задач не найдена.");

    private final String message;
}
