package ru.s21school.retailanalytics_web.dto.entityDto.storeDto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class StoreCreateDto {
    private Long transactionStoreId;
    private Long sku;
    private Double skuPurchasePrice;
    private Double skuRetailPrice;
}
