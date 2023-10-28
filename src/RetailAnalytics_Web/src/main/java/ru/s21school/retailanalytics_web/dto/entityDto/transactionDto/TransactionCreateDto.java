package ru.s21school.retailanalytics_web.dto.entityDto.transactionDto;

import jakarta.validation.constraints.NotNull;
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
    private Long id;

    @NotNull(message = "Customer card is mandatory")
    private Long customerCard;

    @NotNull(message = "Transaction sum is mandatory")
    private Double transactionSum;

    @NotNull(message = "Transaction date time is mandatory")
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private LocalDateTime transactionDatetime;

    @NotNull(message = "Transaction store id is mandatory")
    private Long transactionStoreId;
}
