package ru.s21school.retailanalytics_web.dto.entityDto.storeDto;

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
public class StoreCreateDto {
    @NotNull(message = "Transaction Store Id is mandatory")
    @Min(value = 0, message = "Transaction Store Id must be greater 0")
    private Long transactionStoreId;

    @NotNull(message = "Sku Id is mandatory")
    @Min(value = 0, message = "Sku Id must be greater 0")
    private Long sku;

    @Min(value = 0, message = "Sku Purchase Price must be greater 0")
    @NotNull(message = "Sku Purchase Price is mandatory")
    private Double skuPurchasePrice;

    @Min(value = 0, message = "Sku Id must be greater 0")
    @NotNull(message = "Sku Retail Price is mandatory")
    private Double skuRetailPrice;
}
