package ru.school.retailanalitycs_web_java.dto.viewDto;

import lombok.Builder;
import lombok.Data;
import ru.school.retailanalitycs_web_java.dto.entityDto.customerDto.CustomerDto;

@Data
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