package ru.school.retailanalitycs_web_java.dto.viewDto;

import lombok.Builder;
import lombok.Data;
import ru.school.retailanalitycs_web_java.dto.entityDto.customerDto.CustomerDto;
import ru.school.retailanalitycs_web_java.dto.entityDto.transactionDto.TransactionReadDto;
import ru.school.retailanalitycs_web_java.entities.views.purchaseHistoryView.PurchaseHistoryView;

import java.time.LocalDateTime;

/**
 * A DTO for the {@link PurchaseHistoryView} entity
 */
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