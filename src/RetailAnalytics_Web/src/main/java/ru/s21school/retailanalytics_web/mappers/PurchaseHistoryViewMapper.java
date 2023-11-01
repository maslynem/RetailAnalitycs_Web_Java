package ru.s21school.retailanalytics_web.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.s21school.retailanalytics_web.dto.viewDto.PurchaseHistoryViewCsvDto;
import ru.s21school.retailanalytics_web.dto.viewDto.PurchaseHistoryViewDto;

@Mapper(componentModel = "spring")
public interface PurchaseHistoryViewMapper {
    @Mapping(target = "customerId", source = "customer.id")
    @Mapping(target = "transactionId", source = "transaction.id")
    @Mapping(target = "skuGroupId", source = "skuGroup.id")
    PurchaseHistoryViewCsvDto map(PurchaseHistoryViewDto dto);
}
