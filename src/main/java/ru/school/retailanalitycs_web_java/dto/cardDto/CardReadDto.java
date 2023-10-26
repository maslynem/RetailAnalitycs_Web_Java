package ru.school.retailanalitycs_web_java.dto.cardDto;

import com.opencsv.bean.CsvBindByName;
import com.opencsv.bean.CsvBindByPosition;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.school.retailanalitycs_web_java.dto.customerDto.CustomerDto;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CardReadDto {

    @CsvBindByName(column = "customer_card_id", required = true)
    @CsvBindByPosition(position = 0)
    private Integer id;

    @NotNull(message = "Customer is mandatory")
    @CsvBindByName(column = "customer_id", required = true)
    @CsvBindByPosition(position = 1)
    private CustomerDto customer;
}
