package ru.s21school.retailanalytics_web.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.s21school.retailanalytics_web.dto.entityDto.storeDto.StoreCreateDto;
import ru.s21school.retailanalytics_web.dto.entityDto.storeDto.StoreReadDto;

@Mapper(componentModel = "spring")
public interface StoreMapper {
    @Mapping(target = "sku", source = "sku.id")
    StoreCreateDto map(StoreReadDto dto);
}
