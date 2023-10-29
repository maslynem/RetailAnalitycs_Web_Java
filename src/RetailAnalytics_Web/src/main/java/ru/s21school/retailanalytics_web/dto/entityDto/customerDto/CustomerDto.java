package ru.s21school.retailanalytics_web.dto.entityDto.customerDto;

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

    private String customerName;

    private String customerSurname;

    private String customerPrimaryEmail;

    private String customerPrimaryPhone;
}
