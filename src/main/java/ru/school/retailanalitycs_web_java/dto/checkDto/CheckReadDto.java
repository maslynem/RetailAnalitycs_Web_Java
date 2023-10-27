package ru.school.retailanalitycs_web_java.dto.checkDto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.school.retailanalitycs_web_java.dto.skuDto.SkuReadDto;
import ru.school.retailanalitycs_web_java.dto.transactionDto.TransactionReadDto;

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
