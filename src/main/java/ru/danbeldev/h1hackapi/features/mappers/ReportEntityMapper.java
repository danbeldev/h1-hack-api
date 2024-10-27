package ru.danbeldev.h1hackapi.features.mappers;

import org.mapstruct.*;
import ru.danbeldev.h1hackapi.features.dto.ReportEntityDto;
import ru.danbeldev.h1hackapi.features.entities.ReportEntity;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING)
public interface ReportEntityMapper {
    ReportEntity toEntity(ReportEntityDto reportEntityDto);

    ReportEntityDto toDto(ReportEntity reportEntity);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    ReportEntity partialUpdate(ReportEntityDto reportEntityDto, @MappingTarget ReportEntity reportEntity);
}