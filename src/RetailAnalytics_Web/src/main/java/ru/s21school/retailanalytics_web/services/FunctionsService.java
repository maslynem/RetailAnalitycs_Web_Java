package ru.s21school.retailanalytics_web.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import ru.s21school.retailanalytics_web.controllers.functionsController.requestEntity.FrequencyOfVisitRequest;
import ru.s21school.retailanalytics_web.controllers.functionsController.requestEntity.MarginGrowthOfferRequest;
import ru.s21school.retailanalytics_web.controllers.functionsController.requestEntity.PersonalOfferByDatesRequest;
import ru.s21school.retailanalytics_web.controllers.functionsController.requestEntity.PersonalOfferByTransactionsNumberRequest;
import ru.s21school.retailanalytics_web.dto.functionResultDto.FrequencyOfVisitDto;
import ru.s21school.retailanalytics_web.dto.functionResultDto.MarginGrowthOfferDto;
import ru.s21school.retailanalytics_web.dto.functionResultDto.PersonalOfferDto;
import ru.s21school.retailanalytics_web.exceptions.EmptyResponseBodyException;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class FunctionsService {
    private static final String FUNCTIONS_API_URL = "http://localhost:8081/api/v1/functions/";

    private final RestTemplate restTemplate;

    public List<FrequencyOfVisitDto> getFrequencyOfVisit(FrequencyOfVisitRequest request) throws HttpClientErrorException {
        HttpEntity<FrequencyOfVisitRequest> httpEntity = new HttpEntity<>(request);
        ResponseEntity<List<FrequencyOfVisitDto>> response =
                restTemplate.exchange(FUNCTIONS_API_URL + "frequency-of-visits",
                        HttpMethod.POST, httpEntity, new ParameterizedTypeReference<>() {
                        });
        List<FrequencyOfVisitDto> body = response.getBody();
        if (body == null) {
            log.error("Error after request to [{} frequency-of-visits]. Response status code is [{}]. But response body is null", FUNCTIONS_API_URL, response.getStatusCode());
            throw new EmptyResponseBodyException();
        }
        return body;
    }

    public List<PersonalOfferDto> getPersonalOfferByDates(PersonalOfferByDatesRequest request) throws HttpClientErrorException {
        HttpEntity<PersonalOfferByDatesRequest> httpEntity = new HttpEntity<>(request);
        ResponseEntity<List<PersonalOfferDto>> response =
                restTemplate.exchange(FUNCTIONS_API_URL + "personal-offer-growth-by-dates",
                        HttpMethod.POST, httpEntity, new ParameterizedTypeReference<>() {
                        });
        List<PersonalOfferDto> body = response.getBody();
        if (body == null) {
            log.error("Error after request to [{} frequency-of-visits]. Response status code is [{}]. But response body is null", FUNCTIONS_API_URL, response.getStatusCode());
            throw new EmptyResponseBodyException();
        }
        return body;
    }

    public List<PersonalOfferDto> getPersonalOfferByTransactionsNumber(PersonalOfferByTransactionsNumberRequest request) throws HttpClientErrorException {
        HttpEntity<PersonalOfferByTransactionsNumberRequest> httpEntity = new HttpEntity<>(request);
        ResponseEntity<List<PersonalOfferDto>> response =
                restTemplate.exchange(FUNCTIONS_API_URL + "personal-offer-growth-by-transactions-number",
                        HttpMethod.POST, httpEntity, new ParameterizedTypeReference<>() {
                        });
        List<PersonalOfferDto> body = response.getBody();
        if (body == null) {
            log.error("Error after request to [{} frequency-of-visits]. Response status code is [{}]. But response body is null", FUNCTIONS_API_URL, response.getStatusCode());
            throw new EmptyResponseBodyException();
        }
        return body;
    }

    public List<MarginGrowthOfferDto> getMarginGrowthOffer(MarginGrowthOfferRequest request) {
        HttpEntity<MarginGrowthOfferRequest> httpEntity = new HttpEntity<>(request);
        ResponseEntity<List<MarginGrowthOfferDto>> response =
                restTemplate.exchange(FUNCTIONS_API_URL + "margin-growth-offer",
                        HttpMethod.POST, httpEntity, new ParameterizedTypeReference<>() {
                        });
        List<MarginGrowthOfferDto> body = response.getBody();
        if (body == null) {
            log.error("Error after request to [{} frequency-of-visits]. Response status code is [{}]. But response body is null", FUNCTIONS_API_URL, response.getStatusCode());
            throw new EmptyResponseBodyException();
        }
        return body;
    }
}
