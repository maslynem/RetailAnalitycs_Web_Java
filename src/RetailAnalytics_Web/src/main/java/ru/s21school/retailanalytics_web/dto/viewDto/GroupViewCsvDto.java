package ru.s21school.retailanalytics_web.dto.viewDto;

import com.opencsv.bean.CsvBindByName;
import com.opencsv.bean.CsvBindByPosition;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GroupViewCsvDto {
    @CsvBindByName(column = "customer_id", required = true)
    @CsvBindByPosition(position = 0)
    private Long customerId;

    @CsvBindByName(column = "skuGroupId", required = true)
    @CsvBindByPosition(position = 1)
    private Long skuGroupId;

    @CsvBindByName(column = "groupAffinityIndex", required = true)
    @CsvBindByPosition(position = 2)
    private Double groupAffinityIndex;

    @CsvBindByName(column = "groupChurnRate", required = true)
    @CsvBindByPosition(position = 3)
    private Double groupChurnRate;

    @CsvBindByName(column = "groupStabilityIndex", required = true)
    @CsvBindByPosition(position = 4)
    private Double groupStabilityIndex;

    @CsvBindByName(column = "groupMargin", required = true)
    @CsvBindByPosition(position = 5)
    private Double groupMargin;

    @CsvBindByName(column = "groupDiscountShare", required = true)
    @CsvBindByPosition(position = 6)
    private Double groupDiscountShare;

    @CsvBindByName(column = "groupMinimumDiscount", required = true)
    @CsvBindByPosition(position = 7)
    private Double groupMinimumDiscount;

    @CsvBindByName(column = "groupAverageDiscount", required = true)
    @CsvBindByPosition(position = 8)
    private Double groupAverageDiscount;
}