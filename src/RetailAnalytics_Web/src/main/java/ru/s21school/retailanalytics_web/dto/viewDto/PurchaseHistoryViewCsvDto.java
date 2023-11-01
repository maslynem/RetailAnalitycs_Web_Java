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
public class PurchaseHistoryViewCsvDto {

    @CsvBindByName(column = "customerId", required = true)
    @CsvBindByPosition(position = 0)
    private Long customerId;

    @CsvBindByName(column = "transactionId", required = true)
    @CsvBindByPosition(position = 1)
    private Long transactionId;

    @CsvBindByName(column = "transactionDatetime", required = true)
    @CsvBindByPosition(position = 2)
    @CsvDate(value = "dd.MM.yyyy H:mm:ss")
    private LocalDateTime transactionDatetime;

    @CsvBindByName(column = "skuGroupId", required = true)
    @CsvBindByPosition(position = 3)
    private Long skuGroupId;

    @CsvBindByName(column = "groupCost", required = true)
    @CsvBindByPosition(position = 4)
    private Double groupCost;

    @CsvBindByName(column = "groupSumm", required = true)
    @CsvBindByPosition(position = 5)
    private Double groupSumm;

    @CsvBindByName(column = "groupSummPaid", required = true)
    @CsvBindByPosition(position = 6)
    private Double groupSummPaid;
}