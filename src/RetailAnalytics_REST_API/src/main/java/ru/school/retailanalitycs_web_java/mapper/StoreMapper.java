package ru.school.retailanalitycs_web_java.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.school.retailanalitycs_web_java.dto.entityDto.storeDto.StoreCreateDto;
import ru.school.retailanalitycs_web_java.dto.entityDto.storeDto.StoreReadDto;
import ru.school.retailanalitycs_web_java.entities.tables.Store;

@Mapper(componentModel = "spring", uses = {SkuMapper.class, TransactionMapper.class})
public abstract class StoreMapper {
    @Mapping(target = "sku", source = "id.sku")
    @Mapping(target = "transactionStoreId", source = "id.transactionStoreId")
    public abstract StoreReadDto toDto(Store entity);

    @Mapping(target = "sku", source = "id.sku")
    @Mapping(target = "transactionStoreId", source = "id.transactionStoreId")
    public abstract StoreCreateDto toCreateDto(Store store);

    @Mapping(target = "id.sku", source = "sku")
    @Mapping(target = "id.transactionStoreId", source = "transactionStoreId")
    public abstract Store toEntity(StoreCreateDto dto);

//    protected StoreId getStoreIdByDto(StoreCreateDto dto) {
//        return new StoreId(dto.getTransactionStoreId(), dto.getSku());
//    }
}
