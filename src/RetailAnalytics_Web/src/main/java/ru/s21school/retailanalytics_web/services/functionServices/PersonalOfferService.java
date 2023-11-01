package ru.s21school.retailanalytics_web.services.functionServices;

import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import ru.s21school.retailanalytics_web.controllers.operationsController.requestEntity.PersonalOfferByDatesRequest;
import ru.s21school.retailanalytics_web.controllers.operationsController.requestEntity.PersonalOfferByTransactionsNumberRequest;
import ru.s21school.retailanalytics_web.dto.functionResultDto.PersonalOfferCsvDto;
import ru.s21school.retailanalytics_web.dto.functionResultDto.PersonalOfferDto;
import ru.s21school.retailanalytics_web.exceptions.EmptyResponseBodyException;
import ru.s21school.retailanalytics_web.mappers.PersonalOfferMapper;
import ru.s21school.retailanalytics_web.utils.CsvWriter;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class PersonalOfferService {
    private static final String FUNCTIONS_API_URL = "http://localhost:8081/api/v1/functions/";

    private final RestTemplate restTemplate;
    private final PersonalOfferMapper personalOfferMapper;
    private final CsvWriter<PersonalOfferCsvDto> csvWriter;
    private List<PersonalOfferDto> personalOfferDtoList = Collections.emptyList();

    public List<PersonalOfferDto> getPersonalOfferByDates(PersonalOfferByDatesRequest request) throws HttpClientErrorException {
        HttpEntity<PersonalOfferByDatesRequest> httpEntity = new HttpEntity<>(request);
        ResponseEntity<List<PersonalOfferDto>> response =
                restTemplate.exchange(FUNCTIONS_API_URL + "personal-offer-growth-by-dates",
                        HttpMethod.POST, httpEntity, new ParameterizedTypeReference<>() {
                        });
        personalOfferDtoList = response.getBody();
        if (personalOfferDtoList == null) {
            log.error("Error after request to [{} frequency-of-visits]. Response status code is [{}]. But response body is null", FUNCTIONS_API_URL, response.getStatusCode());
            throw new EmptyResponseBodyException();
        }
        return personalOfferDtoList;
    }

    public List<PersonalOfferDto> getPersonalOfferByTransactionsNumber(PersonalOfferByTransactionsNumberRequest request) throws HttpClientErrorException {
        HttpEntity<PersonalOfferByTransactionsNumberRequest> httpEntity = new HttpEntity<>(request);
        ResponseEntity<List<PersonalOfferDto>> response =
                restTemplate.exchange(FUNCTIONS_API_URL + "personal-offer-growth-by-transactions-number",
                        HttpMethod.POST, httpEntity, new ParameterizedTypeReference<>() {
                        });
        personalOfferDtoList = response.getBody();
        if (personalOfferDtoList == null) {
            log.error("Error after request to [{} frequency-of-visits]. Response status code is [{}]. But response body is null", FUNCTIONS_API_URL, response.getStatusCode());
            throw new EmptyResponseBodyException();
        }
        return personalOfferDtoList;
    }


    public void exportPersonalOffer(HttpServletResponse servletResponse) throws IOException {
        List<PersonalOfferCsvDto> personalOfferCsvDtoList = personalOfferDtoList.stream().map(personalOfferMapper::map).toList();
        servletResponse.setContentType("text/csv");
        servletResponse.addHeader("Content-Disposition", "attachment; filename=PersonalOffer.csv");
        servletResponse.setCharacterEncoding("UTF-8");
        csvWriter.exportCsv(servletResponse.getWriter(), personalOfferCsvDtoList, PersonalOfferCsvDto.class);
    }
}
