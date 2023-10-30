package ru.s21school.retailanalytics_web.dto.viewDto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.s21school.retailanalytics_web.dto.entityDto.customerDto.CustomerDto;
import ru.s21school.retailanalytics_web.dto.entityDto.skuGroupDto.SkuGroupDto;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GroupViewDto {
    private CustomerDto customer;
    private SkuGroupDto skuGroup;
    private Double groupAffinityIndex;
    private Double groupChurnRate;
    private Double groupStabilityIndex;
    private Double groupMargin;
    private Double groupDiscountShare;
    private Double groupMinimumDiscount;
    private Double groupAverageDiscount;
}