package ru.school.retailanalitycs_web_java.dto;

import com.opencsv.bean.CsvBindByName;
import com.opencsv.bean.CsvBindByPosition;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@Builder
@EqualsAndHashCode(callSuper = false)
public class CustomerDto extends BaseDto<Integer> {

    @CsvBindByName(column = "customer_id", required = true)
    @CsvBindByPosition(position = 0)
    private Integer id;

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
