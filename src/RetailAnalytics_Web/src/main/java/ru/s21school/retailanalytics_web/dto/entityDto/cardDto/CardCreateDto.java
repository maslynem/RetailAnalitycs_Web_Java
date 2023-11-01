package ru.s21school.retailanalytics_web.dto.entityDto.cardDto;

import com.opencsv.bean.CsvBindByName;
import com.opencsv.bean.CsvBindByPosition;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CardCreateDto {
    @CsvBindByName(column = "customer_card_id", required = true)
    @CsvBindByPosition(position = 0)
    private Long id;
    @CsvBindByName(column = "customer_id", required = true)
    @CsvBindByPosition(position = 1)
    private Long customer;
}
