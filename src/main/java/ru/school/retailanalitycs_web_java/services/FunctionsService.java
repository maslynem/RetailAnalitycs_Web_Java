package ru.school.retailanalitycs_web_java.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.school.retailanalitycs_web_java.dao.FrequencyOfVisitProcedure;
import ru.school.retailanalitycs_web_java.entities.functionResults.FrequencyOfVisit;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class FunctionsService {
    private final FrequencyOfVisitProcedure frequencyOfVisitProcedure;

    public List<FrequencyOfVisit> getFrequencyOfVisit(LocalDateTime firstDate,
                                                      LocalDateTime lastDate,
                                                      Long addedTransactions,
                                                      Double maxChurnIndex,
                                                      Double maxShareOfTransactions,
                                                      Double allowableMarginShare) {
        return frequencyOfVisitProcedure.getFrequencyOfVisit(firstDate, lastDate, addedTransactions, maxChurnIndex, maxShareOfTransactions, allowableMarginShare);
    }
}
