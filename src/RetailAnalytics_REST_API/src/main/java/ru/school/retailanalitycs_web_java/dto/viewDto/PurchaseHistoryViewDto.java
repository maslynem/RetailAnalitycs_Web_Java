package ru.school.retailanalitycs_web_java.dto.viewDto;

import lombok.Builder;
import lombok.Data;
import ru.school.retailanalitycs_web_java.dto.entityDto.customerDto.CustomerDto;
import ru.school.retailanalitycs_web_java.dto.entityDto.skuGroupDto.SkuGroupDto;
import ru.school.retailanalitycs_web_java.dto.entityDto.transactionDto.TransactionReadDto;

import java.time.LocalDateTime;

@Data
@Builder
public class PurchaseHistoryViewDto {
    private CustomerDto customer;
    private TransactionReadDto transaction;
    private LocalDateTime transactionDatetime;
    private SkuGroupDto skuGroup;
    private Double groupCost;
    private Double groupSumm;
    private Double groupSummPaid;
}