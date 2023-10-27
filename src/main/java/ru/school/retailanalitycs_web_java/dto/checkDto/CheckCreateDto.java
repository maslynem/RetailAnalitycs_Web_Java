package ru.school.retailanalitycs_web_java.dto.checkDto;

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
public class CheckCreateDto {

    @NotNull(message = "Transaction Id is mandatory")
    @Min(value = 0, message = "Transaction Id must be greater 0")
    @CsvBindByName(column = "transaction_id", required = true)
    @CsvBindByPosition(position = 1)
    private Integer transactionId;

    @NotNull(message = "Sku Id is mandatory")
    @Min(value = 0, message = "Sku Id must be greater 0")
    @CsvBindByName(column = "sku_id", required = true)
    @CsvBindByPosition(position = 2)
    private Integer skuId;

    @NotNull(message = "Sku Amount is mandatory")
    @Min(value = 0, message = "Sku Amount must be greater 0")
    @CsvBindByName(column = "sku_amount", required = true)
    @CsvBindByPosition(position = 3)
    private Double skuAmount;

    @NotNull(message = "Sku Sum is mandatory")
    @Min(value = 0, message = "Sku Sum must be greater 0")
    @CsvBindByName(column = "sku_summ", required = true)
    @CsvBindByPosition(position = 4)
    private Double skuSum;

    @NotNull(message = "Sku Sum Paid is mandatory")
    @Min(value = 0, message = "Sku Sum Paid must be greater 0")
    @CsvBindByName(column = "sku_summ_paid", required = true)
    @CsvBindByPosition(position = 5)
    private Double skuSumPaid;

    @NotNull(message = "Sku Discount is mandatory")
    @Min(value = 0, message = "Sku Discount must be greater 0")
    @CsvBindByName(column = "sku_discount", required = true)
    @CsvBindByPosition(position = 6)
    private Double skuDiscount;
}
