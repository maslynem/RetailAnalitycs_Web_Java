package ru.s21school.retailanalytics_web.dto.entityDto.customerDto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CustomerDto {

    private Long id;

    @NotBlank(message = "Name is mandatory")
    private String customerName;

    @NotBlank(message = "Surname is mandatory")
    private String customerSurname;

    @NotBlank(message = "Email is mandatory")
    @Email(message = "Email is not valid")
    private String customerPrimaryEmail;

    @NotBlank(message = "Phone number is mandatory")
    @Pattern(regexp = "^\\+7\\d{10}", message = "Phone number should contains +7 and 10 Arabic numerals")
    private String customerPrimaryPhone;
}
