package ru.project.jbd.domain.dto;

public record StatusCount(
    Long toDo,
    Long inProgress,
    Long done
) {}
