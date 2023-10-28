package ru.school.retailanalitycs_web_java.controllers.functionController.requestEntity;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MarginGrowthOfferRequest {
    @NotNull(message = "Group Number is mandatory")
    private Long groupsNumber;

    @NotNull(message = "Max Churn Index is mandatory")
    private Double maxChurnIndex;

    @NotNull(message = "Max Stability Index is mandatory")
    private Double maxStabilityIndex;

    @NotNull(message = "Max Sku Index is mandatory")
    private Double maxSkuShare;

    @NotNull(message = "Allowable Margin Share is mandatory")
    private Double allowableMarginShare;
}
