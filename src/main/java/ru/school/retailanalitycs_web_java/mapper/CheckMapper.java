package ru.school.retailanalitycs_web_java.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.school.retailanalitycs_web_java.dto.checkDto.CheckCreateDto;
import ru.school.retailanalitycs_web_java.dto.checkDto.CheckReadDto;
import ru.school.retailanalitycs_web_java.entities.tables.Check;
import ru.school.retailanalitycs_web_java.entities.tables.CheckId;

@Mapper(componentModel = "spring", uses = {SkuMapper.class, TransactionMapper.class})
public abstract class CheckMapper {

    public abstract CheckReadDto toDto(Check entity);

    @Mapping(target = "id", expression = "java(getCheckIdByDto(dto))")
    @Mapping(target = "transaction", source = "transactionId")
    @Mapping(target = "sku", source = "skuId")
    public abstract Check toEntity(CheckCreateDto dto);

    protected CheckId getCheckIdByDto(CheckCreateDto dto) {
        return new CheckId(dto.getTransactionId(), dto.getSkuId());
    }
}
