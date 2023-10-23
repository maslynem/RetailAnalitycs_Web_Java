package ru.school.retailanalitycs_web_java.dto;

import com.opencsv.bean.CsvBindByName;
import com.opencsv.bean.CsvBindByPosition;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class StoreDto extends BaseDto<Integer> {
    @CsvBindByName(column = "store_id", required = true)
    @CsvBindByPosition(position = 0)
    private Integer id;

    @CsvBindByName(column = "transaction_store_id", required = true)
    @CsvBindByPosition(position = 1)
    private Integer transactionStore;

    @CsvBindByName(column = "sku_id", required = true)
    @CsvBindByPosition(position = 2)
    private Integer sku;

    @CsvBindByName(column = "sku_purchase_price", required = true)
    @CsvBindByPosition(position = 3)
    private Float skuPurchasePrice;

    @CsvBindByName(column = "sku_retail_price", required = true)
    @CsvBindByPosition(position = 4)
    private Float skuRetailPrice;
}
