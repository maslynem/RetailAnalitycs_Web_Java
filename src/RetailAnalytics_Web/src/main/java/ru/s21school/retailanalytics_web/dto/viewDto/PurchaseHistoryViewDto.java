package ru.s21school.retailanalytics_web.dto.viewDto;

import lombok.Builder;
import lombok.Data;
import ru.s21school.retailanalytics_web.dto.entityDto.customerDto.CustomerDto;
import ru.s21school.retailanalytics_web.dto.entityDto.transactionDto.TransactionReadDto;

import java.time.LocalDateTime;

@Data
@Builder
public class PurchaseHistoryViewDto {
    private CustomerDto customer;
    private TransactionReadDto transaction;
    private LocalDateTime transactionDatetime;
    private Long groupId;
    private Double groupCost;
    private Double groupSumm;
    private Double groupSummPaid;
}