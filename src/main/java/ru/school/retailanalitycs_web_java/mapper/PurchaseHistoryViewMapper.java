package ru.school.retailanalitycs_web_java.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import ru.school.retailanalitycs_web_java.dto.viewDto.PurchaseHistoryViewDto;
import ru.school.retailanalitycs_web_java.entities.views.purchaseHistoryView.PurchaseHistoryView;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING, uses = {CustomerMapper.class, TransactionMapper.class})
public abstract class PurchaseHistoryViewMapper {

    @Mapping(target = "transaction", source = "id.transactionId")
    @Mapping(target = "customer", source = "id.customerId")
    public abstract PurchaseHistoryViewDto toDto(PurchaseHistoryView purchaseHistoryView);
}