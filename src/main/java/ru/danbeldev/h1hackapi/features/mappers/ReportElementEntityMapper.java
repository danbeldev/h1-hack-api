package ru.danbeldev.h1hackapi.features.mappers;

import org.mapstruct.*;
import ru.danbeldev.h1hackapi.features.dto.ReportElementEntityDto;
import ru.danbeldev.h1hackapi.features.entities.ReportElementEntity;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING)
public interface ReportElementEntityMapper {
    ReportElementEntity toEntity(ReportElementEntityDto reportElementEntityDto);

    ReportElementEntityDto toDto(ReportElementEntity reportElementEntity);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    ReportElementEntity partialUpdate(ReportElementEntityDto reportElementEntityDto, @MappingTarget ReportElementEntity reportElementEntity);
}