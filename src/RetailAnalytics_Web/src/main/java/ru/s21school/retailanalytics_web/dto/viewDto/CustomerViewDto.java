package ru.s21school.retailanalytics_web.dto.viewDto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.s21school.retailanalytics_web.dto.entityDto.customerDto.CustomerDto;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CustomerViewDto {
    private CustomerDto customer;
    private Double customerAverageCheck;
    private String customerAverageCheckSegment;
    private Double customerFrequency;
    private String customerFrequencySegment;
    private Double customerInactivePeriod;
    private Double customerChurnRate;
    private String customerChurnSegment;
    private Integer customerSegment;
    private Long customerPrimaryStore;
}