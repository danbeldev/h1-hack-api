package ru.danbeldev.h1hackapi.features.dto;

import lombok.Value;

/**
 * DTO for {@link ru.danbeldev.h1hackapi.features.entities.ProjectEntity}
 */
@Value
public class ProjectEntityDto {
    Long id;
    String name;
}