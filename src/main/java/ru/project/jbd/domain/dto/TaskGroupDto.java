package ru.project.jbd.domain.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TaskGroupDto {
    private Long id;
    private String name;
    private String description;
    private String user;
}
