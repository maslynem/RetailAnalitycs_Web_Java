package ru.s21school.retailanalytics_web.services.functionServices;

import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import ru.s21school.retailanalytics_web.controllers.operationsController.requestEntity.FrequencyOfVisitRequest;
import ru.s21school.retailanalytics_web.controllers.operationsController.requestEntity.MarginGrowthOfferRequest;
import ru.s21school.retailanalytics_web.controllers.operationsController.requestEntity.PersonalOfferByDatesRequest;
import ru.s21school.retailanalytics_web.controllers.operationsController.requestEntity.PersonalOfferByTransactionsNumberRequest;
import ru.s21school.retailanalytics_web.dto.functionResultDto.FrequencyOfVisitDto;
import ru.s21school.retailanalytics_web.dto.functionResultDto.MarginGrowthOfferDto;
import ru.s21school.retailanalytics_web.dto.functionResultDto.PersonalOfferDto;

import java.io.IOException;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class FunctionsService {
    private static final String FUNCTIONS_API_URL = "http://retailAnalyticsApi/api/v1/functions/";

    private final RestTemplate restTemplate;
    private final FrequencyOfVisitService frequencyOfVisitService;
    private final PersonalOfferService personalOfferService;
    private final MarginGrowthOfferService marginGrowthOfferService;

    public void updateDataAnalysis() {
        restTemplate.postForLocation(FUNCTIONS_API_URL + "update", null);
    }

    public List<FrequencyOfVisitDto> getFrequencyOfVisit(FrequencyOfVisitRequest request) throws HttpClientErrorException {
        return frequencyOfVisitService.getFrequencyOfVisit(request);
    }

    public void exportFrequencyOfVisit(HttpServletResponse servletResponse) throws IOException {
        frequencyOfVisitService.exportFrequencyOfVisit(servletResponse);
    }

    public List<PersonalOfferDto> getPersonalOfferByDates(PersonalOfferByDatesRequest request) throws HttpClientErrorException {
        return personalOfferService.getPersonalOfferByDates(request);
    }

    public List<PersonalOfferDto> getPersonalOfferByTransactionsNumber(PersonalOfferByTransactionsNumberRequest request) throws HttpClientErrorException {
        return personalOfferService.getPersonalOfferByTransactionsNumber(request);
    }

    public void exportPersonalOffer(HttpServletResponse servletResponse) throws IOException {
        personalOfferService.exportPersonalOffer(servletResponse);
    }

    public List<MarginGrowthOfferDto> getMarginGrowthOffer(MarginGrowthOfferRequest request) {
        return marginGrowthOfferService.getMarginGrowthOffer(request);
    }

    public void exportMarginGrowthOffer(HttpServletResponse servletResponse) throws IOException {
        marginGrowthOfferService.exportMarginGrowthOffer(servletResponse);
    }

}
