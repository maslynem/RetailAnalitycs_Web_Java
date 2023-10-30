package ru.s21school.retailanalytics_web.controllers.functionsController.requestEntity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PersonalOfferByTransactionsNumberRequest {
    private Integer numberOfTransactions;

    private Double coefficientOfAverageCheckIncrease;

    private Double maxChurnIndex;

    private Integer maxShareOfTransactionsWithDiscount;

    private Integer allowableShareOfMargin;
}
