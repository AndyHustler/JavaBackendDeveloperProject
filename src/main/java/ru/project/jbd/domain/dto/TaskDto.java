package ru.project.jbd.domain.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TaskDto {
    
    private Long id;

    @NotBlank
    private String name;

    private String description;

    private String user;

    private String group;
    
    private String status;
}
