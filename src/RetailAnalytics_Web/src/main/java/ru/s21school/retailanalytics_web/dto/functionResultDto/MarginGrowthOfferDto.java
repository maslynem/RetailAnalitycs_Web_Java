package ru.s21school.retailanalytics_web.dto.functionResultDto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.s21school.retailanalytics_web.dto.entityDto.customerDto.CustomerDto;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MarginGrowthOfferDto {
    private CustomerDto customer;
    private String skuName;
    private Double offerDiscountDepth;
}
