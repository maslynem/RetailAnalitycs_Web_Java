package ru.school.retailanalitycs_web_java.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.school.retailanalitycs_web_java.dto.entityDto.checkDto.CheckCreateDto;
import ru.school.retailanalitycs_web_java.dto.entityDto.checkDto.CheckReadDto;
import ru.school.retailanalitycs_web_java.entities.tables.Check;
import ru.school.retailanalitycs_web_java.entities.tables.CheckId;

@Mapper(componentModel = "spring", uses = {SkuMapper.class, TransactionMapper.class})
public abstract class CheckMapper {

    public abstract CheckReadDto toDto(Check entity);

    @Mapping(target = "transactionId", source = "transaction.id")
    @Mapping(target = "skuId", source = "sku.id")
    public abstract CheckCreateDto toCreateDto(Check check);

    @Mapping(target = "id", expression = "java(getCheckIdByDto(dto))")
    @Mapping(target = "transaction", source = "transactionId")
    @Mapping(target = "sku", source = "skuId")
    public abstract Check toEntity(CheckCreateDto dto);

    protected CheckId getCheckIdByDto(CheckCreateDto dto) {
        return new CheckId(dto.getTransactionId(), dto.getSkuId());
    }
}
