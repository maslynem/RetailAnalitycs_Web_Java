package ru.school.retailanalitycs_web_java.dto.functionResultDto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.school.retailanalitycs_web_java.dto.entityDto.customerDto.CustomerDto;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MarginGrowthOfferDto {
    private CustomerDto customer;
    private String skuName;
    private Double offerDiscountDepth;
}
