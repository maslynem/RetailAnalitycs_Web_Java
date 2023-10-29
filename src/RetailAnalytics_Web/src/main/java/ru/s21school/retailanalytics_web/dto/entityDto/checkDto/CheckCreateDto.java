package ru.s21school.retailanalytics_web.dto.entityDto.checkDto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CheckCreateDto {

    private Long transactionId;

    private Long skuId;

    private Double skuAmount;

    private Double skuSum;

    private Double skuSumPaid;

    private Double skuDiscount;
}
