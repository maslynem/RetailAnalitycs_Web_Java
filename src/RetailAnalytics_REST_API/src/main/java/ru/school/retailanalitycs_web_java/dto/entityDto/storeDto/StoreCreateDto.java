package ru.school.retailanalitycs_web_java.dto.entityDto.storeDto;

import com.opencsv.bean.CsvBindByName;
import com.opencsv.bean.CsvBindByPosition;
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
    @CsvBindByName(column = "transaction_store_id", required = true)
    @CsvBindByPosition(position = 0)
    private Long transactionStoreId;

    @NotNull(message = "Sku Id is mandatory")
    @Min(value = 0, message = "Sku Id must be greater 0")
    @CsvBindByName(column = "sku_id", required = true)
    @CsvBindByPosition(position = 1)
    private Long sku;

    @Min(value = 0, message = "Sku Purchase Price must be greater 0")
    @NotNull(message = "Sku Purchase Price is mandatory")
    @CsvBindByName(column = "sku_purchase_price", required = true)
    @CsvBindByPosition(position = 2)
    private Double skuPurchasePrice;

    @Min(value = 0, message = "Sku Id must be greater 0")
    @NotNull(message = "Sku Retail Price is mandatory")
    @CsvBindByName(column = "sku_retail_price", required = true)
    @CsvBindByPosition(position = 3)
    private Double skuRetailPrice;
}
