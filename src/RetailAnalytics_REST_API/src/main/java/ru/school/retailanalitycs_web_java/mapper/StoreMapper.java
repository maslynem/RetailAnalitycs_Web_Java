package ru.school.retailanalitycs_web_java.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.school.retailanalitycs_web_java.dto.entityDto.storeDto.StoreCreateDto;
import ru.school.retailanalitycs_web_java.dto.entityDto.storeDto.StoreReadDto;
import ru.school.retailanalitycs_web_java.entities.tables.Store;
import ru.school.retailanalitycs_web_java.entities.tables.StoreId;

@Mapper(componentModel = "spring", uses = {SkuMapper.class})
public abstract class StoreMapper {
    @Mapping(target = "transactionStoreId", source = "id.transactionStoreId")
    @Mapping(target = "sku.skuGroup", ignore = true)
    public abstract StoreReadDto toDto(Store entity);

    @Mapping(target = "sku", source = "sku.id")
    @Mapping(target = "transactionStoreId", source = "id.transactionStoreId")
    public abstract StoreCreateDto toCreateDto(Store store);

    @Mapping(target = "id", expression = "java(getStoreIdByDto(dto))")
    public abstract Store toEntity(StoreCreateDto dto);

    protected StoreId getStoreIdByDto(StoreCreateDto dto) {
        return new StoreId(dto.getTransactionStoreId(), dto.getSku());
    }
}
