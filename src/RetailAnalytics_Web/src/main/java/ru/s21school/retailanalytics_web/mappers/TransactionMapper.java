package ru.s21school.retailanalytics_web.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.s21school.retailanalytics_web.dto.entityDto.transactionDto.TransactionCreateDto;
import ru.s21school.retailanalytics_web.dto.entityDto.transactionDto.TransactionReadDto;

@Mapper(componentModel = "spring")
public interface TransactionMapper {
    @Mapping(target = "customerCard", source = "customerCard.id")
    TransactionCreateDto map(TransactionReadDto dto);
}
