package ru.school.retailanalitycs_web_java.dto.entityDto.cardDto;

import com.opencsv.bean.CsvBindByName;
import com.opencsv.bean.CsvBindByPosition;
import jakarta.validation.constraints.NotNull;
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

    @NotNull(message = "Customer is mandatory")
    @CsvBindByName(column = "customer_id", required = true)
    @CsvBindByPosition(position = 1)
    private Long customer;
}
