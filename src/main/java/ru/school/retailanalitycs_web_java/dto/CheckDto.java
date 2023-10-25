package ru.school.retailanalitycs_web_java.dto;

import com.opencsv.bean.CsvBindByName;
import com.opencsv.bean.CsvBindByPosition;
import lombok.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CheckDto {
    @CsvBindByName(column = "checks_id", required = true)
    @CsvBindByPosition(position = 0)
    private Long id;

    @CsvBindByName(column = "transaction_id", required = true)
    @CsvBindByPosition(position = 1)
    private Long transaction;

    @CsvBindByName(column = "sku_id", required = true)
    @CsvBindByPosition(position = 2)
    private Long sku;

    @CsvBindByName(column = "sku_amount", required = true)
    @CsvBindByPosition(position = 3)
    private Double skuAmount;

    @CsvBindByName(column = "sku_summ", required = true)
    @CsvBindByPosition(position = 4)
    private Double skuSum;

    @CsvBindByName(column = "sku_summ_paid", required = true)
    @CsvBindByPosition(position = 5)
    private Double skuSumPaid;

    @CsvBindByName(column = "sku_discount", required = true)
    @CsvBindByPosition(position = 6)
    private Double skuDiscount;
}
