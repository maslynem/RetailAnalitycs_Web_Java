package ru.school.retailanalitycs_web_java.entities.functionResults;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PersonalOffer {
    private Long customerId;
    private Double requiredCheckMeasure;
    private String groupName;
    private Double offerDiscountDepth;
}
