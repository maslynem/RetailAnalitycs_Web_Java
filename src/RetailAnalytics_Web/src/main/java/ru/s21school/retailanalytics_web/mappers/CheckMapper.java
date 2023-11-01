package ru.s21school.retailanalytics_web.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.s21school.retailanalytics_web.dto.entityDto.checkDto.CheckCreateDto;
import ru.s21school.retailanalytics_web.dto.entityDto.checkDto.CheckReadDto;

@Mapper(componentModel = "spring")
public interface CheckMapper {
    @Mapping(target = "transactionId", source = "transaction.id")
    @Mapping(target = "skuId", source = "sku.id")
    CheckCreateDto map(CheckReadDto dto);
}
