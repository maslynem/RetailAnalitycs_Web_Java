package ru.school.retailanalitycs_web_java.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.school.retailanalitycs_web_java.controllers.functionController.requestEntity.FrequencyOfVisitRequest;
import ru.school.retailanalitycs_web_java.controllers.functionController.requestEntity.MarginGrowthOfferRequest;
import ru.school.retailanalitycs_web_java.controllers.functionController.requestEntity.PersonalOfferByDatesRequest;
import ru.school.retailanalitycs_web_java.controllers.functionController.requestEntity.PersonalOfferByTransactionsNumberRequest;
import ru.school.retailanalitycs_web_java.dao.FrequencyOfVisitProcedure;
import ru.school.retailanalitycs_web_java.dao.MarginGrowthOfferProcedure;
import ru.school.retailanalitycs_web_java.dao.PersonalOfferGrowthProcedure;
import ru.school.retailanalitycs_web_java.entities.functionResults.FrequencyOfVisit;
import ru.school.retailanalitycs_web_java.entities.functionResults.MarginGrowthOffer;
import ru.school.retailanalitycs_web_java.entities.functionResults.PersonalOffer;

import java.util.List;

@Service
@RequiredArgsConstructor
public class FunctionsService {
    private final FrequencyOfVisitProcedure frequencyOfVisitProcedure;
    private final PersonalOfferGrowthProcedure personalOfferGrowthProcedure;
    private final MarginGrowthOfferProcedure marginGrowthOfferProcedure;

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

    public List<MarginGrowthOffer> getMarginGrowthOffer(MarginGrowthOfferRequest request) {
        return marginGrowthOfferProcedure.getFrequencyOfVisit(
                request.getGroupsNumber(),
                request.getMaxChurnIndex(),
                request.getMaxStabilityIndex(),
                request.getMaxSkuShare(),
                request.getAllowableMarginShare());
    }

}
