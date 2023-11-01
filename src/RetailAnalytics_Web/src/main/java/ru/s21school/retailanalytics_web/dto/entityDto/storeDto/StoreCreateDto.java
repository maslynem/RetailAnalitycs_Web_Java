package ru.s21school.retailanalytics_web.dto.entityDto.storeDto;

import com.opencsv.bean.CsvBindByName;
import com.opencsv.bean.CsvBindByPosition;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class StoreCreateDto {
    @CsvBindByName(column = "transaction_store_id", required = true)
    @CsvBindByPosition(position = 0)
    private Long transactionStoreId;
    @CsvBindByName(column = "sku_id", required = true)
    @CsvBindByPosition(position = 1)
    private Long sku;
    @CsvBindByName(column = "sku_purchase_price", required = true)
    @CsvBindByPosition(position = 2)
    private Double skuPurchasePrice;
    @CsvBindByName(column = "sku_retail_price", required = true)
    @CsvBindByPosition(position = 3)
    private Double skuRetailPrice;
}
