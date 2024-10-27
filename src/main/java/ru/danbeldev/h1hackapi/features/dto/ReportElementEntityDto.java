package ru.danbeldev.h1hackapi.features.dto;

import lombok.Value;

import java.util.Map;

/**
 * DTO for {@link ru.danbeldev.h1hackapi.features.entities.ReportElementEntity}
 */
public record ReportElementEntityDto(
        Long id,
        String type,
        Map<String, Object> data,
        Map<String, Object> styles
) {}