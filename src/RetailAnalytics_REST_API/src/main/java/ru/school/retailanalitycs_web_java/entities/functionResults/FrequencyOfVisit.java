package ru.school.retailanalitycs_web_java.entities.functionResults;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FrequencyOfVisit {
    private Long customerId;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private Double requiredTransactionsCount;
    private String groupName;
    private Double offerDiscountDepth;
}
