package ru.school.retailanalitycs_web_java.controllers.functionController.requestEntity;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PersonalOfferByTransactionsNumberRequest {
    @NotNull(message = "Number Of Transactions is mandatory")
    private Integer numberOfTransactions;

    @NotNull(message = "Coefficient Of Average Check Increase is mandatory")
    private Double coefficientOfAverageCheckIncrease;

    @NotNull(message = "Max Churn Index is mandatory")
    private Double maxChurnIndex;

    @NotNull(message = "Max Share Of Transactions With Discount is mandatory")
    private Integer maxShareOfTransactionsWithDiscount;

    @NotNull(message = "Allowable Share Of Margin is mandatory")
    private Integer allowableShareOfMargin;
}
