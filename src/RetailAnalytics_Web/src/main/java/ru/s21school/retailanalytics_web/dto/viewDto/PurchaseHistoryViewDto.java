package ru.s21school.retailanalytics_web.dto.viewDto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.s21school.retailanalytics_web.dto.entityDto.customerDto.CustomerDto;
import ru.s21school.retailanalytics_web.dto.entityDto.skuGroupDto.SkuGroupDto;
import ru.s21school.retailanalytics_web.dto.entityDto.transactionDto.TransactionReadDto;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
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