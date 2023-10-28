package ru.school.retailanalitycs_web_java.dto.functionResultDto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.school.retailanalitycs_web_java.dto.entityDto.customerDto.CustomerDto;

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
