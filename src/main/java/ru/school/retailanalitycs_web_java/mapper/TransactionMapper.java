package ru.school.retailanalitycs_web_java.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.school.retailanalitycs_web_java.dto.transactionDto.TransactionCreateDto;
import ru.school.retailanalitycs_web_java.dto.transactionDto.TransactionReadDto;
import ru.school.retailanalitycs_web_java.entities.tables.Transaction;

@Mapper(componentModel = "spring", uses = {CardMapper.class})
public abstract class TransactionMapper {

    @Mapping(target = "customerCard.customer", ignore = true)
    public abstract TransactionReadDto toDto(Transaction transaction);


    @Mapping(target = "checks", ignore = true)
    public abstract Transaction toEntity(TransactionCreateDto transactionDto);

}