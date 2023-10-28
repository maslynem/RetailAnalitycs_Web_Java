package ru.school.retailanalitycs_web_java.controllers.functionController.requestEntity;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FrequencyOfVisitRequest {

    @NotNull(message = "First Date is mandatory")
    private LocalDateTime firstDate;

    @NotNull(message = "Last Date is mandatory")
    private LocalDateTime lastDate;

    @NotNull(message = "Added Transactions is mandatory")
    private Long addedTransactions;

    @NotNull(message = "Max Churn Index is mandatory")
    private Double maxChurnIndex;

    @NotNull(message = "Max Share Of Transactions is mandatory")
    private Double maxShareOfTransactions;

    @NotNull(message = "Allowable Margin Share is mandatory")
    private Double allowableMarginShare;
}
