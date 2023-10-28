package ru.s21school.retailanalytics_web.dto.entityDto.checkDto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CheckCreateDto {

    @NotNull(message = "Transaction Id is mandatory")
    @Min(value = 0, message = "Transaction Id must be greater 0")
    private Long transactionId;

    @NotNull(message = "Sku Id is mandatory")
    @Min(value = 0, message = "Sku Id must be greater 0")
    private Long skuId;

    @NotNull(message = "Sku Amount is mandatory")
    @Min(value = 0, message = "Sku Amount must be greater 0")
    private Double skuAmount;

    @NotNull(message = "Sku Sum is mandatory")
    @Min(value = 0, message = "Sku Sum must be greater 0")
    private Double skuSum;

    @NotNull(message = "Sku Sum Paid is mandatory")
    @Min(value = 0, message = "Sku Sum Paid must be greater 0")
    private Double skuSumPaid;

    @NotNull(message = "Sku Discount is mandatory")
    @Min(value = 0, message = "Sku Discount must be greater 0")
    private Double skuDiscount;
}
