package ru.s21school.retailanalytics_web.dto.entityDto.transactionDto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.s21school.retailanalytics_web.dto.entityDto.cardDto.CardReadDto;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TransactionReadDto {
    private Long id;

    private CardReadDto customerCard;

    private Double transactionSum;

    private LocalDateTime transactionDatetime;

    private Long transactionStoreId;
}
