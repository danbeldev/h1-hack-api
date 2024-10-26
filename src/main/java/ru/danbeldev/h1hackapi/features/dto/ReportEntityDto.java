package ru.danbeldev.h1hackapi.features.dto;

import lombok.Value;
import ru.danbeldev.h1hackapi.features.entities.ReportEntity;

import java.time.Instant;

/**
 * DTO for {@link ReportEntity}
 */
@Value
public class ReportEntityDto {
    Long id;
    String name;
    Instant dateCreation;
    Instant dateUpdated;
}