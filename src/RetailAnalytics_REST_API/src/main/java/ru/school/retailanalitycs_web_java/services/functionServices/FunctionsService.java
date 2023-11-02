package ru.school.retailanalitycs_web_java.services.functionServices;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.school.retailanalitycs_web_java.controllers.functionController.requestEntity.FrequencyOfVisitRequest;
import ru.school.retailanalitycs_web_java.controllers.functionController.requestEntity.PersonalOfferByDatesRequest;
import ru.school.retailanalitycs_web_java.controllers.functionController.requestEntity.PersonalOfferByTransactionsNumberRequest;
import ru.school.retailanalitycs_web_java.dao.FrequencyOfVisitProcedure;
import ru.school.retailanalitycs_web_java.dao.MaterializedViewsUpdateDao;
import ru.school.retailanalitycs_web_java.dao.PersonalOfferGrowthProcedure;
import ru.school.retailanalitycs_web_java.entities.functionResults.FrequencyOfVisit;
import ru.school.retailanalitycs_web_java.entities.functionResults.PersonalOffer;
import ru.school.retailanalitycs_web_java.entities.tables.DateOfAnalysisFormation;
import ru.school.retailanalitycs_web_java.services.entityServices.DateOfAnalysisFormationService;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class FunctionsService {
    private final DateOfAnalysisFormationService dateOfAnalysisFormationService;
    private final FrequencyOfVisitProcedure frequencyOfVisitProcedure;
    private final PersonalOfferGrowthProcedure personalOfferGrowthProcedure;
    private final MaterializedViewsUpdateDao materializedViewsUpdateDao;

    @Transactional
    public void updateAnalysisFormation() {
        materializedViewsUpdateDao.updateMaterializeViews();
        dateOfAnalysisFormationService.save(new DateOfAnalysisFormation(LocalDateTime.now()));
    }

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
