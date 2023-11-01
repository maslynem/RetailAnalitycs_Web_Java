package ru.s21school.retailanalytics_web.dto.viewDto;

import com.opencsv.bean.CsvBindByName;
import com.opencsv.bean.CsvBindByPosition;
import com.opencsv.bean.CsvDate;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PeriodViewCsvDto {
    @CsvBindByName(column = "customerId", required = true)
    @CsvBindByPosition(position = 0)
    private Long customerId;

    @CsvBindByName(column = "skuGroupId", required = true)
    @CsvBindByPosition(position = 1)
    private Long skuGroupId;

    @CsvBindByName(column = "firstGroupPurchaseDate", required = true)
    @CsvBindByPosition(position = 2)
    @CsvDate(value = "dd.MM.yyyy H:mm:ss")
    private LocalDateTime firstGroupPurchaseDate;

    @CsvBindByName(column = "lastGroupPurchaseDate", required = true)
    @CsvBindByPosition(position = 3)
    @CsvDate(value = "dd.MM.yyyy H:mm:ss")
    private LocalDateTime lastGroupPurchaseDate;

    @CsvBindByName(column = "groupPurchase", required = true)
    @CsvBindByPosition(position = 4)
    private Long groupPurchase;

    @CsvBindByName(column = "groupFrequency", required = true)
    @CsvBindByPosition(position = 5)
    private Double groupFrequency;

    @CsvBindByName(column = "groupMinDiscount", required = true)
    @CsvBindByPosition(position = 6)
    private Double groupMinDiscount;
}