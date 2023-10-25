package ru.school.retailanalitycs_web_java.mapper;

import org.mapstruct.Mapper;
import ru.school.retailanalitycs_web_java.dto.TransactionDto;
import ru.school.retailanalitycs_web_java.entities.tables.Transaction;

@Mapper(componentModel = "spring", uses = {CardMapper.class})
public abstract class TransactionMapper extends GenericMapper<Transaction, TransactionDto> {

}