package ru.school.retailanalitycs_web_java.dto.functionResultDto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.school.retailanalitycs_web_java.dto.entityDto.customerDto.CustomerDto;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FrequencyOfVisitDto {
    private CustomerDto customer;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private Double requiredTransactionsCount;
    private String groupName;
    private Double offerDiscountDepth;
}
