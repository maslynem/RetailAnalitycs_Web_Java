package ru.school.retailanalitycs_web_java.dto.transactionDto;

import com.opencsv.bean.CsvBindByName;
import com.opencsv.bean.CsvBindByPosition;
import com.opencsv.bean.CsvDate;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import ru.school.retailanalitycs_web_java.dto.cardDto.CardReadDto;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TransactionReadDto {
    @CsvBindByName(column = "transaction_id", required = true)
    @CsvBindByPosition(position = 0)
    private Integer id;

    @NotNull(message = "Customer card is mandatory")
    @CsvBindByName(column = "customer_card_id", required = true)
    @CsvBindByPosition(position = 1)
    private CardReadDto customerCard;

    @NotNull(message = "Transaction sum is mandatory")
    @CsvBindByName(column = "transaction_summ", required = true)
    @CsvBindByPosition(position = 2)
    private Double transactionSum;

    @NotNull(message = "Transaction date time is mandatory")
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    @CsvBindByName(column = "transaction_datetime", required = true)
    @CsvBindByPosition(position = 3)
    @CsvDate(value = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime transactionDatetime;

    @NotNull(message = "Transaction store id is mandatory")
    @CsvBindByName(column = "transaction_store_id", required = true)
    @CsvBindByPosition(position = 4)
    private Integer transactionStoreId;
}
