package ru.s21school.retailanalytics_web.dto.entityDto.checkDto;

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
public class CheckCreateDto {
    @CsvBindByName(column = "transaction_id", required = true)
    @CsvBindByPosition(position = 0)
    private Long transactionId;
    @CsvBindByName(column = "sku_id", required = true)
    @CsvBindByPosition(position = 1)
    private Long skuId;
    @CsvBindByName(column = "sku_amount", required = true)
    @CsvBindByPosition(position = 2)
    private Double skuAmount;
    @CsvBindByName(column = "sku_summ", required = true)
    @CsvBindByPosition(position = 3)
    private Double skuSum;
    @CsvBindByName(column = "sku_summ_paid", required = true)
    @CsvBindByPosition(position = 4)
    private Double skuSumPaid;
    @CsvBindByName(column = "sku_discount", required = true)
    @CsvBindByPosition(position = 5)
    private Double skuDiscount;
}
