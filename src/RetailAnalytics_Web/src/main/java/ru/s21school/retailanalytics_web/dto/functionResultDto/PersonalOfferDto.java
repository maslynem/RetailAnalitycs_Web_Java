package ru.s21school.retailanalytics_web.dto.functionResultDto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.s21school.retailanalytics_web.dto.entityDto.customerDto.CustomerDto;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PersonalOfferDto {
    private CustomerDto customer;
    private Double requiredCheckMeasure;
    private String groupName;
    private Double offerDiscountDepth;
}
