package ru.s21school.retailanalytics_web.dto.functionResultDto;

import com.opencsv.bean.CsvBindByName;
import com.opencsv.bean.CsvBindByPosition;
import com.opencsv.bean.CsvDate;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FrequencyOfVisitCsvDto {
    @CsvBindByName(column = "customerId", required = true)
    @CsvBindByPosition(position = 0)
    private Long customerId;

    @CsvBindByName(column = "startDate", required = true)
    @CsvBindByPosition(position = 1)
    @CsvDate(value = "dd.MM.yyyy H:mm:ss")
    private LocalDateTime startDate;

    @CsvBindByName(column = "endDate", required = true)
    @CsvBindByPosition(position = 2)
    @CsvDate(value = "dd.MM.yyyy H:mm:ss")
    private LocalDateTime endDate;

    @CsvBindByName(column = "requiredTransactionsCount", required = true)
    @CsvBindByPosition(position = 3)
    private Double requiredTransactionsCount;

    @CsvBindByName(column = "groupName", required = true)
    @CsvBindByPosition(position = 4)
    private String groupName;

    @CsvBindByName(column = "offerDiscountDepth", required = true)
    @CsvBindByPosition(position = 5)
    private Double offerDiscountDepth;
}
