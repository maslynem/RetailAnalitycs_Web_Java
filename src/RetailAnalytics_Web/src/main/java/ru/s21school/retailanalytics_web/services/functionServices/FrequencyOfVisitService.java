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
import ru.s21school.retailanalytics_web.controllers.operationsController.requestEntity.FrequencyOfVisitRequest;
import ru.s21school.retailanalytics_web.dto.functionResultDto.FrequencyOfVisitCsvDto;
import ru.s21school.retailanalytics_web.dto.functionResultDto.FrequencyOfVisitDto;
import ru.s21school.retailanalytics_web.exceptions.EmptyResponseBodyException;
import ru.s21school.retailanalytics_web.mappers.FrequencyOfVisitMapper;
import ru.s21school.retailanalytics_web.utils.CsvWriter;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class FrequencyOfVisitService {
    private static final String FUNCTIONS_API_URL = "http://localhost:8081/api/v1/functions/";

    private final RestTemplate restTemplate;
    private final FrequencyOfVisitMapper frequencyOfVisitMapper;
    private final CsvWriter<FrequencyOfVisitCsvDto> csvWriter;
    private List<FrequencyOfVisitDto> frequencyOfVisitDtoList = Collections.emptyList();

    public List<FrequencyOfVisitDto> getFrequencyOfVisit(FrequencyOfVisitRequest request) throws HttpClientErrorException {
        HttpEntity<FrequencyOfVisitRequest> httpEntity = new HttpEntity<>(request);
        ResponseEntity<List<FrequencyOfVisitDto>> response =
                restTemplate.exchange(FUNCTIONS_API_URL + "frequency-of-visits",
                        HttpMethod.POST, httpEntity, new ParameterizedTypeReference<>() {
                        });
        frequencyOfVisitDtoList = response.getBody();
        if (frequencyOfVisitDtoList == null) {
            log.error("Error after request to [{} frequency-of-visits]. Response status code is [{}]. But response body is null", FUNCTIONS_API_URL, response.getStatusCode());
            throw new EmptyResponseBodyException();
        }
        return frequencyOfVisitDtoList;
    }

    public void exportFrequencyOfVisit(HttpServletResponse servletResponse) throws IOException {
        List<FrequencyOfVisitCsvDto> frequencyOfVisitCsv = frequencyOfVisitDtoList.stream().map(frequencyOfVisitMapper::map).toList();
        servletResponse.setContentType("text/csv");
        servletResponse.addHeader("Content-Disposition", "attachment; filename=frequencyOfVisit.csv");
        servletResponse.setCharacterEncoding("UTF-8");
        csvWriter.exportCsv(servletResponse.getWriter(), frequencyOfVisitCsv, FrequencyOfVisitCsvDto.class);
    }

}
