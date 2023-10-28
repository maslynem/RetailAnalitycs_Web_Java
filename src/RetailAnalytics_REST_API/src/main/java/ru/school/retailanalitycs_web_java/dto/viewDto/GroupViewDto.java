package ru.school.retailanalitycs_web_java.dto.viewDto;

import lombok.Builder;
import lombok.Data;
import ru.school.retailanalitycs_web_java.dto.entityDto.customerDto.CustomerDto;
import ru.school.retailanalitycs_web_java.dto.entityDto.skuGroupDto.SkuGroupDto;

@Data
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