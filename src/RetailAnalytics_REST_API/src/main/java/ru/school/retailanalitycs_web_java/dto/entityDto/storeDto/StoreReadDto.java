package ru.school.retailanalitycs_web_java.dto.entityDto.storeDto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.school.retailanalitycs_web_java.dto.entityDto.skuDto.SkuReadDto;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class StoreReadDto {
    private Long transactionStoreId;

    private SkuReadDto sku;

    private Double skuPurchasePrice;

    private Double skuRetailPrice;
}
