package ru.school.retailanalitycs_web_java.dto;

import com.opencsv.bean.CsvBindByName;
import com.opencsv.bean.CsvBindByPosition;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CardDto {

    @CsvBindByName(column = "customer_card_id", required = true)
    @CsvBindByPosition(position = 0)
    private Integer id;

    @NotNull(message = "Customer is mandatory")
    @CsvBindByName(column = "customer_id", required = true)
    @CsvBindByPosition(position = 1)
    private Integer customer;
}
