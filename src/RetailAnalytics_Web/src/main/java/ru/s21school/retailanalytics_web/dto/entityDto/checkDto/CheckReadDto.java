package ru.s21school.retailanalytics_web.dto.entityDto.checkDto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.s21school.retailanalytics_web.dto.entityDto.skuDto.SkuReadDto;
import ru.s21school.retailanalytics_web.dto.entityDto.transactionDto.TransactionReadDto;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CheckReadDto {
    private TransactionReadDto transaction;

    private SkuReadDto sku;

    private Double skuAmount;

    private Double skuSum;

    private Double skuSumPaid;

    private Double skuDiscount;
}
