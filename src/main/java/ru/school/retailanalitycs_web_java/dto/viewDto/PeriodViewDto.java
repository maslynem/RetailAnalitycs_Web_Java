package ru.school.retailanalitycs_web_java.dto.viewDto;

import lombok.Builder;
import lombok.Data;
import ru.school.retailanalitycs_web_java.dto.entityDto.customerDto.CustomerDto;
import ru.school.retailanalitycs_web_java.dto.entityDto.skuGroupDto.SkuGroupDto;

import java.time.LocalDateTime;

@Data
@Builder
public class PeriodViewDto {
    private CustomerDto customer;
    private SkuGroupDto group;
    private LocalDateTime firstGroupPurchaseDate;
    private LocalDateTime lastGroupPurchaseDate;
    private Long groupPurchase;
    private Double groupFrequency;
    private Double groupMinDiscount;
}