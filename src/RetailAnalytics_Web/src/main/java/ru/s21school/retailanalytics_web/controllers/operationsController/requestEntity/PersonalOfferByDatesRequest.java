package ru.s21school.retailanalytics_web.controllers.operationsController.requestEntity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PersonalOfferByDatesRequest {
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate firstDate;

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate lastDate;

    private Double coefficientOfAverageCheckIncrease;

    private Double maxChurnIndex;

    private Integer maxShareOfTransactionsWithDiscount;

    private Integer allowableShareOfMargin;
}
