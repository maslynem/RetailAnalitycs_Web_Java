package ru.school.retailanalitycs_web_java.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.beans.factory.annotation.Autowired;
import ru.school.retailanalitycs_web_java.dto.entityDto.transactionDto.TransactionCreateDto;
import ru.school.retailanalitycs_web_java.dto.entityDto.transactionDto.TransactionReadDto;
import ru.school.retailanalitycs_web_java.entities.tables.Transaction;
import ru.school.retailanalitycs_web_java.exceptions.notFoundExceptions.TransactionNotFoundException;
import ru.school.retailanalitycs_web_java.services.entityServices.TransactionService;

@Mapper(componentModel = "spring", uses = {CardMapper.class})
public abstract class TransactionMapper {
    @Autowired
    private TransactionService transactionService;

    public abstract TransactionReadDto toDto(Transaction transaction);

    @Mapping(target = "customerCard", source = "customerCard.id")
    public abstract TransactionCreateDto toCreateDto(Transaction transaction);

    @Mapping(target = "checks", ignore = true)
    public abstract Transaction toEntity(TransactionCreateDto transactionDto);

    public Transaction toDtoById(Long id) {
        return transactionService.findById(id).orElseThrow(() -> new TransactionNotFoundException(id));
    }
}