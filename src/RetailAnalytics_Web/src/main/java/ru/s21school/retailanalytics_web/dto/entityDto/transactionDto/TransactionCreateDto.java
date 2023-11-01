package ru.s21school.retailanalytics_web.dto.entityDto.transactionDto;

import com.opencsv.bean.CsvBindByName;
import com.opencsv.bean.CsvBindByPosition;
import com.opencsv.bean.CsvDate;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TransactionCreateDto {
    @CsvBindByName(column = "transaction_id", required = true)
    @CsvBindByPosition(position = 0)
    private Long id;

    @CsvBindByName(column = "customer_card_id", required = true)
    @CsvBindByPosition(position = 1)
    private Long customerCard;

    @CsvBindByName(column = "transaction_summ", required = true)
    @CsvBindByPosition(position = 2)
    private Double transactionSum;

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    @CsvBindByName(column = "transaction_datetime", required = true)
    @CsvBindByPosition(position = 3)
    @CsvDate(value = "dd.MM.yyyy H:mm:ss")
    private LocalDateTime transactionDatetime;

    @CsvBindByName(column = "transaction_store_id", required = true)
    @CsvBindByPosition(position = 4)
    private Long transactionStoreId;
}
