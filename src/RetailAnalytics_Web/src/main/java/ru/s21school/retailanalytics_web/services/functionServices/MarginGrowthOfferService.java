package ru.s21school.retailanalytics_web.services.functionServices;

import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import ru.s21school.retailanalytics_web.controllers.operationsController.requestEntity.MarginGrowthOfferRequest;
import ru.s21school.retailanalytics_web.dto.functionResultDto.MarginGrowthOfferCsvDto;
import ru.s21school.retailanalytics_web.dto.functionResultDto.MarginGrowthOfferDto;
import ru.s21school.retailanalytics_web.exceptions.EmptyResponseBodyException;
import ru.s21school.retailanalytics_web.mappers.MarginGrowthOfferMapper;
import ru.s21school.retailanalytics_web.utils.CsvWriter;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class MarginGrowthOfferService {
    private static final String FUNCTIONS_API_URL = "http://localhost:8081/api/v1/functions/";

    private final RestTemplate restTemplate;
    private final CsvWriter<MarginGrowthOfferCsvDto> csvWriter;
    private final MarginGrowthOfferMapper marginGrowthOfferMapper;
    private List<MarginGrowthOfferDto> marginGrowthOfferDtoList = Collections.emptyList();

    public List<MarginGrowthOfferDto> getMarginGrowthOffer(MarginGrowthOfferRequest request) {
        HttpEntity<MarginGrowthOfferRequest> httpEntity = new HttpEntity<>(request);
        ResponseEntity<List<MarginGrowthOfferDto>> response =
                restTemplate.exchange(FUNCTIONS_API_URL + "margin-growth-offer",
                        HttpMethod.POST, httpEntity, new ParameterizedTypeReference<>() {
                        });
        marginGrowthOfferDtoList = response.getBody();
        if (marginGrowthOfferDtoList == null) {
            log.error("Error after request to [{} frequency-of-visits]. Response status code is [{}]. But response body is null", FUNCTIONS_API_URL, response.getStatusCode());
            throw new EmptyResponseBodyException();
        }
        return marginGrowthOfferDtoList;
    }

    public void exportMarginGrowthOffer(HttpServletResponse servletResponse) throws IOException {
        List<MarginGrowthOfferCsvDto> marginGrowthOfferCsvDtoList = marginGrowthOfferDtoList.stream().map(marginGrowthOfferMapper::map).toList();
        servletResponse.setContentType("text/csv");
        servletResponse.addHeader("Content-Disposition", "attachment; filename=MarginGrowthOffer.csv");
        servletResponse.setCharacterEncoding("UTF-8");
        csvWriter.exportCsv(servletResponse.getWriter(), marginGrowthOfferCsvDtoList, MarginGrowthOfferCsvDto.class);
    }
}
