package ru.s21school.retailanalytics_web.dto.functionResultDto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.s21school.retailanalytics_web.dto.entityDto.customerDto.CustomerDto;

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
