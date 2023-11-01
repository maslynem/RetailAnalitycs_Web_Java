package ru.s21school.retailanalytics_web.dto.viewDto;

import com.opencsv.bean.CsvBindByName;
import com.opencsv.bean.CsvBindByPosition;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CustomerViewCsvDto {
    @CsvBindByName(column = "customer_id", required = true)
    @CsvBindByPosition(position = 0)
    private Long customerId;

    @CsvBindByName(column = "customerAverageCheck", required = true)
    @CsvBindByPosition(position = 1)
    private Double customerAverageCheck;

    @CsvBindByName(column = "customerAverageCheckSegment", required = true)
    @CsvBindByPosition(position = 2)
    private String customerAverageCheckSegment;

    @CsvBindByName(column = "customerFrequency", required = true)
    @CsvBindByPosition(position = 3)
    private Double customerFrequency;

    @CsvBindByName(column = "customerFrequencySegment", required = true)
    @CsvBindByPosition(position = 4)
    private String customerFrequencySegment;

    @CsvBindByName(column = "customerInactivePeriod", required = true)
    @CsvBindByPosition(position = 5)
    private Double customerInactivePeriod;

    @CsvBindByName(column = "customerChurnRate", required = true)
    @CsvBindByPosition(position = 6)
    private Double customerChurnRate;

    @CsvBindByName(column = "customerChurnSegment", required = true)
    @CsvBindByPosition(position = 7)
    private String customerChurnSegment;

    @CsvBindByName(column = "customerSegment", required = true)
    @CsvBindByPosition(position = 8)
    private Integer customerSegment;

    @CsvBindByName(column = "customerPrimaryStore", required = true)
    @CsvBindByPosition(position = 9)
    private Long customerPrimaryStore;
}