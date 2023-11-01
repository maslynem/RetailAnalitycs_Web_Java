package ru.s21school.retailanalytics_web.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.s21school.retailanalytics_web.dto.entityDto.skuDto.SkuCreateDto;
import ru.s21school.retailanalytics_web.dto.entityDto.skuDto.SkuReadDto;

@Mapper(componentModel = "spring")
public interface SkuMapper {
    @Mapping(target = "skuGroup", source = "skuGroup.id")
    SkuCreateDto map(SkuReadDto dto);
}
