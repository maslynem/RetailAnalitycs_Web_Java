package ru.school.retailanalitycs_web_java.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.school.retailanalitycs_web_java.controllers.functionController.requestEntity.FrequencyOfVisitRequest;
import ru.school.retailanalitycs_web_java.controllers.functionController.requestEntity.PersonalOfferByDatesRequest;
import ru.school.retailanalitycs_web_java.controllers.functionController.requestEntity.PersonalOfferByTransactionsNumberRequest;
import ru.school.retailanalitycs_web_java.dao.FrequencyOfVisitProcedure;
import ru.school.retailanalitycs_web_java.dao.PersonalOfferGrowthProcedure;
import ru.school.retailanalitycs_web_java.entities.functionResults.FrequencyOfVisit;
import ru.school.retailanalitycs_web_java.entities.functionResults.PersonalOffer;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class FunctionsService {
    private final FrequencyOfVisitProcedure frequencyOfVisitProcedure;
    private final PersonalOfferGrowthProcedure personalOfferGrowthProcedure;

    public List<FrequencyOfVisit> getFrequencyOfVisit(FrequencyOfVisitRequest request) {
        return frequencyOfVisitProcedure.getFrequencyOfVisit(
                request.getFirstDate(),
                request.getLastDate(),
                request.getAddedTransactions(),
                request.getMaxChurnIndex(),
                request.getMaxShareOfTransactions(),
                request.getAllowableMarginShare());
    }

    public List<PersonalOffer> getPersonalOfferGrowthOfTheAverageCheck(PersonalOfferByDatesRequest request) {
        return personalOfferGrowthProcedure.getPersonalOfferGrowthOfTheAverageCheck(
                request.getFirstDate(),
                request.getLastDate(),
                request.getCoefficientOfAverageCheckIncrease(),
                request.getMaxChurnIndex(),
                request.getMaxShareOfTransactionsWithDiscount(),
                request.getAllowableShareOfMargin());
    }

    public List<PersonalOffer> getPersonalOfferGrowthOfTheAverageCheck(PersonalOfferByTransactionsNumberRequest request) {
        return personalOfferGrowthProcedure.getPersonalOfferGrowthOfTheAverageCheck(
                request.getNumberOfTransactions(),
                request.getCoefficientOfAverageCheckIncrease(),
                request.getMaxChurnIndex(),
                request.getMaxShareOfTransactionsWithDiscount(),
                request.getAllowableShareOfMargin());
    }

}
