package ru.s21school.retailanalytics_web.dto.viewDto;

import lombok.Builder;
import lombok.Data;
import ru.s21school.retailanalytics_web.dto.entityDto.customerDto.CustomerDto;
import ru.s21school.retailanalytics_web.dto.entityDto.skuGroupDto.SkuGroupDto;

import java.time.LocalDateTime;

@Data
@Builder
public class PeriodViewDto {
    private CustomerDto customer;
    private SkuGroupDto skuGroup;
    private LocalDateTime firstGroupPurchaseDate;
    private LocalDateTime lastGroupPurchaseDate;
    private Long groupPurchase;
    private Double groupFrequency;
    private Double groupMinDiscount;
}