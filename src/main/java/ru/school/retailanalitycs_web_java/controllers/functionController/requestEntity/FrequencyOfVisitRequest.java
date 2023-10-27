package ru.school.retailanalitycs_web_java.controllers.functionController.requestEntity;

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
    private LocalDateTime firstDate;
    private LocalDateTime lastDate;
    private Long addedTransactions;
    private Double maxChurnIndex;
    private Double maxShareOfTransactions;
    private Double allowableMarginShare;
}
