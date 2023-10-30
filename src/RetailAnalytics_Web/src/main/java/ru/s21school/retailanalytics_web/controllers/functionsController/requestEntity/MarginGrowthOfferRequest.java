package ru.s21school.retailanalytics_web.controllers.functionsController.requestEntity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MarginGrowthOfferRequest {
    private Long groupsNumber;
    private Double maxChurnIndex;
    private Double maxStabilityIndex;
    private Double maxSkuShare;
    private Double allowableMarginShare;
}
