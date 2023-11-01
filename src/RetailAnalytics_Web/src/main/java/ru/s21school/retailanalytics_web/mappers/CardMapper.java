package ru.s21school.retailanalytics_web.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.s21school.retailanalytics_web.dto.entityDto.cardDto.CardCreateDto;
import ru.s21school.retailanalytics_web.dto.entityDto.cardDto.CardReadDto;

@Mapper(componentModel = "spring")
public interface CardMapper {
    @Mapping(target = "customer", source = "customer.id")
    CardCreateDto map(CardReadDto dto);
}
