package ru.s21school.retailanalytics_web.services.viewServices;

import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import ru.s21school.retailanalytics_web.dto.PageDto;
import ru.s21school.retailanalytics_web.dto.viewDto.CustomerViewCsvDto;
import ru.s21school.retailanalytics_web.dto.viewDto.CustomerViewDto;
import ru.s21school.retailanalytics_web.exceptions.EmptyResponseBodyException;
import ru.s21school.retailanalytics_web.mappers.CustomerViewMapper;
import ru.s21school.retailanalytics_web.utils.CsvWriter;

import java.io.IOException;
import java.util.List;

@Service
@Slf4j
public class CustomerViewService {
    private static final String CUSTOMER_VIEW_API_URL = "http://retailAnalyticsApi/api/v1/views/customers";
    private final String PAGE_URL_TEMPLATE;

    private final RestTemplate restTemplate;
    private final CsvWriter<CustomerViewCsvDto> csvWriter;
    private final CustomerViewMapper customerViewMapper;

    @Autowired
    public CustomerViewService(RestTemplate restTemplate, CsvWriter<CustomerViewCsvDto> csvWriter, CustomerViewMapper customerViewMapper) {
        this.restTemplate = restTemplate;
        this.csvWriter = csvWriter;
        this.customerViewMapper = customerViewMapper;
        PAGE_URL_TEMPLATE = CUSTOMER_VIEW_API_URL + "?page=%d&size=%d";
    }

    public List<CustomerViewDto> performGetAll() {
        ResponseEntity<List<CustomerViewDto>> response =
                restTemplate.exchange(CUSTOMER_VIEW_API_URL,
                        HttpMethod.GET, null, new ParameterizedTypeReference<>() {
                        });
        List<CustomerViewDto> body = response.getBody();
        if (body == null) {
            log.error("Error after request to [{}]. Response status code is [{}]. But response body is null", CUSTOMER_VIEW_API_URL, response.getStatusCode());
            throw new EmptyResponseBodyException();
        }
        return body;
    }

    public PageDto<CustomerViewDto> performGetPageRequest(int page, int size) {
        final String URL_REQUEST = String.format(PAGE_URL_TEMPLATE, page, size);
        ResponseEntity<PageDto<CustomerViewDto>> response =
                restTemplate.exchange(URL_REQUEST,
                        HttpMethod.GET, null, new ParameterizedTypeReference<>() {
                        });
        PageDto<CustomerViewDto> body = response.getBody();
        if (body == null) {
            log.error("Error after request to [{}]. Response status code is [{}]. But response body is null", URL_REQUEST, response.getStatusCode());
            throw new EmptyResponseBodyException();
        }
        return body;
    }

    public void performExportToCsv(HttpServletResponse servletResponse) throws IOException {
        List<CustomerViewCsvDto> customers = performGetAll().stream().map(customerViewMapper::map).toList();
        servletResponse.setContentType("text/csv");
        servletResponse.addHeader("Content-Disposition", "attachment; filename=customers.csv");
        servletResponse.setCharacterEncoding("UTF-8");
        csvWriter.exportCsv(servletResponse.getWriter(), customers, CustomerViewCsvDto.class);
    }
}
