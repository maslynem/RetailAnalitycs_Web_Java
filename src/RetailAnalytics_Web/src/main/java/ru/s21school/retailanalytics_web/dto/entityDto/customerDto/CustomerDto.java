package ru.s21school.retailanalytics_web.dto.entityDto.customerDto;

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
public class CustomerDto {
    @CsvBindByName(column = "customer_id", required = true)
    @CsvBindByPosition(position = 0)
    private Long id;

    @CsvBindByName(column = "customer_name", required = true)
    @CsvBindByPosition(position = 1)
    private String customerName;

    @CsvBindByName(column = "customer_surname", required = true)
    @CsvBindByPosition(position = 2)
    private String customerSurname;

    @CsvBindByName(column = "customer_primary_email", required = true)
    @CsvBindByPosition(position = 3)
    private String customerPrimaryEmail;

    @CsvBindByName(column = "customer_primary_phone", required = true)
    @CsvBindByPosition(position = 4)
    private String customerPrimaryPhone;
}
