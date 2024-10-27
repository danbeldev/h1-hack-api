package ru.danbeldev.h1hackapi.features.mappers;

import org.mapstruct.*;
import ru.danbeldev.h1hackapi.features.dto.ProjectEntityDto;
import ru.danbeldev.h1hackapi.features.entities.ProjectEntity;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING)
public interface ProjectEntityMapper {
    ProjectEntity toEntity(ProjectEntityDto projectEntityDto);

    ProjectEntityDto toDto(ProjectEntity projectEntity);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    ProjectEntity partialUpdate(ProjectEntityDto projectEntityDto, @MappingTarget ProjectEntity projectEntity);
}