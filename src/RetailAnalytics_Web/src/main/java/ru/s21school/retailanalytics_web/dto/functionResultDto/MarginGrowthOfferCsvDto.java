package ru.s21school.retailanalytics_web.dto.functionResultDto;

import com.opencsv.bean.CsvBindByName;
import com.opencsv.bean.CsvBindByPosition;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MarginGrowthOfferCsvDto {
    @CsvBindByName(column = "customerId", required = true)
    @CsvBindByPosition(position = 0)
    private Long customerId;

    @CsvBindByName(column = "skuName", required = true)
    @CsvBindByPosition(position = 1)
    private String skuName;

    @CsvBindByName(column = "offerDiscountDepth", required = true)
    @CsvBindByPosition(position = 2)
    private Double offerDiscountDepth;
}
