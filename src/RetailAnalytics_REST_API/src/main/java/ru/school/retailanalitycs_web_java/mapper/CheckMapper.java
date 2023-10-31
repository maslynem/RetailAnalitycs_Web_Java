package ru.school.retailanalitycs_web_java.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import ru.school.retailanalitycs_web_java.dto.entityDto.checkDto.CheckCreateDto;
import ru.school.retailanalitycs_web_java.dto.entityDto.checkDto.CheckReadDto;
import ru.school.retailanalitycs_web_java.entities.tables.Check;

@Mapper(componentModel = "spring", uses = {SkuMapper.class, TransactionMapper.class})
public abstract class CheckMapper {


    @Mapping(target = "transaction", source = "id.transaction.id")
    @Mapping(target = "sku", source = "id.sku.id")
    public abstract CheckReadDto toDto(Check entity);

    @Mapping(target = "transactionId", source = "id.transaction.id")
    @Mapping(target = "skuId", source = "id.sku.id")
    public abstract CheckCreateDto toCreateDto(Check check);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "id.transaction", source = "transactionId")
    @Mapping(target = "id.sku", source = "skuId")
    public abstract Check toEntity(CheckCreateDto dto);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "id.transaction", source = "transactionId")
    @Mapping(target = "id.sku", source = "skuId")
    public abstract Check merge(@MappingTarget Check check, CheckCreateDto dto);

}
