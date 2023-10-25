package ru.school.retailanalitycs_web_java.dto;

import com.opencsv.bean.CsvBindByName;
import com.opencsv.bean.CsvBindByPosition;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.*;

@Data
@Builder
@EqualsAndHashCode(callSuper = false)
@AllArgsConstructor
@NoArgsConstructor
public class CustomerDto extends BaseDto<Integer> {

    @CsvBindByName(column = "customer_id", required = true)
    @CsvBindByPosition(position = 0)
    private Integer id;

    @NotBlank(message = "Name is mandatory")
    @CsvBindByName(column = "customer_name", required = true)
    @CsvBindByPosition(position = 1)
    private String customerName;

    @NotBlank(message = "Surname is mandatory")
    @CsvBindByName(column = "customer_surname", required = true)
    @CsvBindByPosition(position = 2)
    private String customerSurname;

    @NotBlank(message = "Email is mandatory")
    @Email(message = "Email is not valid")
    @CsvBindByName(column = "customer_primary_email", required = true)
    @CsvBindByPosition(position = 3)
    private String customerPrimaryEmail;

    @NotBlank(message = "Phone number is mandatory")
    @Pattern(regexp="^\\+7\\d{10}", message="Phone number should contains +7 and 10 Arabic numerals")
    @CsvBindByName(column = "customer_primary_phone", required = true)
    @CsvBindByPosition(position = 4)
    private String customerPrimaryPhone;
}
