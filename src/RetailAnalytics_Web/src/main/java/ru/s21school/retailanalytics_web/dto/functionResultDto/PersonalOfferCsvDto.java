package ru.s21school.retailanalytics_web.dto.functionResultDto;

import com.opencsv.bean.CsvBindByName;
import com.opencsv.bean.CsvBindByPosition;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PersonalOfferCsvDto {
    @CsvBindByName(column = "customerId", required = true)
    @CsvBindByPosition(position = 0)
    private Long customerId;

    @CsvBindByName(column = "requiredCheckMeasure", required = true)
    @CsvBindByPosition(position = 1)
    private Double requiredCheckMeasure;

    @CsvBindByName(column = "groupName", required = true)
    @CsvBindByPosition(position = 2)
    private String groupName;

    @CsvBindByName(column = "offerDiscountDepth", required = true)
    @CsvBindByPosition(position = 3)
    private Double offerDiscountDepth;
}
