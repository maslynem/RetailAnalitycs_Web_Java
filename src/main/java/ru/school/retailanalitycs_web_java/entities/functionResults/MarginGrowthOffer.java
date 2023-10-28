package ru.school.retailanalitycs_web_java.entities.functionResults;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MarginGrowthOffer {
    private Long customerId;
    private String skuName;
    private Double offerDiscountDepth;
}
