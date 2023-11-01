package ru.s21school.retailanalytics_web.services.tableServices;

import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import ru.s21school.retailanalytics_web.dto.entityDto.checkDto.CheckCreateDto;
import ru.s21school.retailanalytics_web.dto.entityDto.checkDto.CheckPageDto;
import ru.s21school.retailanalytics_web.dto.entityDto.checkDto.CheckReadDto;
import ru.s21school.retailanalytics_web.exceptions.EmptyResponseBodyException;
import ru.s21school.retailanalytics_web.mappers.CheckMapper;
import ru.s21school.retailanalytics_web.utils.CsvReader;
import ru.s21school.retailanalytics_web.utils.CsvWriter;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

@Service
@Slf4j
public class CheckService {
    private static final String CHECKS_API_URL = "http://localhost:8081/api/v1/checks";
    private final RestTemplate restTemplate;
    private final String PAGE_URL_TEMPLATE;
    private final String ID_URL_TEMPLATE;
    private final CheckMapper checkMapper;
    private final CsvReader<CheckCreateDto> csvReader;
    private final CsvWriter<CheckCreateDto> csvWriter;

    public CheckService(RestTemplate restTemplate, CheckMapper checkMapper, CsvReader<CheckCreateDto> csvReader, CsvWriter<CheckCreateDto> csvWriter) {
        this.restTemplate = restTemplate;
        this.checkMapper = checkMapper;
        this.csvReader = csvReader;
        this.csvWriter = csvWriter;
        PAGE_URL_TEMPLATE = CHECKS_API_URL + "?page=%d&size=%d";
        ID_URL_TEMPLATE = CHECKS_API_URL + "/%d/%d";
    }

    public List<CheckReadDto> performGetAll() {
        ResponseEntity<List<CheckReadDto>> response =
                restTemplate.exchange(CHECKS_API_URL,
                        HttpMethod.GET, null, new ParameterizedTypeReference<>() {
                        });
        List<CheckReadDto> body = response.getBody();
        if (body == null) {
            log.error("Error after request to [{}]. Response status code is [{}]. But response body is null", CHECKS_API_URL, response.getStatusCode());
            throw new EmptyResponseBodyException();
        }
        return body;
    }

    public CheckPageDto performGetPageRequest(int page, int size) {
        final String URL_REQUEST = String.format(PAGE_URL_TEMPLATE, page, size);
        ResponseEntity<CheckPageDto> response =
                restTemplate.exchange(URL_REQUEST,
                        HttpMethod.GET, null, new ParameterizedTypeReference<>() {
                        });
        CheckPageDto body = response.getBody();
        if (body == null) {
            log.error("Error after request to [{}]. Response status code is [{}]. But response body is null", URL_REQUEST, response.getStatusCode());
            throw new EmptyResponseBodyException();
        }
        return body;
    }

    public CheckReadDto performFindByIdRequest(Long trId, Long skuId) {
        final String URL_REQUEST = String.format(ID_URL_TEMPLATE, trId, skuId);
        ResponseEntity<CheckReadDto> response =
                restTemplate.exchange(URL_REQUEST,
                        HttpMethod.GET, null, new ParameterizedTypeReference<>() {
                        });
        CheckReadDto body = response.getBody();
        if (body == null) {
            log.error("Error after request to [{}]. Response status code is [{}]. But response body is null", URL_REQUEST, response.getStatusCode());
            throw new EmptyResponseBodyException();
        }
        return body;
    }

    public void performSaveCheckRequest(CheckCreateDto check) {
        HttpEntity<CheckCreateDto> httpEntity = new HttpEntity<>(check);
        ResponseEntity<CheckReadDto> response =
                restTemplate.exchange(CHECKS_API_URL,
                        HttpMethod.POST, httpEntity, new ParameterizedTypeReference<>() {
                        });
        CheckReadDto body = response.getBody();
        if (body == null) {
            log.error("Error after request to [{}]. Response status code is [{}]. But response body is null", CHECKS_API_URL, response.getStatusCode());
            throw new EmptyResponseBodyException();
        }
        log.info("New check was saved: {}", body);
    }

    public void performSaveChecksRequest(List<CheckCreateDto> checks) {
        HttpEntity<List<CheckCreateDto>> httpEntity = new HttpEntity<>(checks);
        restTemplate.exchange(CHECKS_API_URL + "/all",
                HttpMethod.POST, httpEntity, new ParameterizedTypeReference<>() {
                });
        log.info("Checks was saved. Size: {}", checks.size());
    }


    public void performUpdateCheckRequest(Long trId, Long skuId, CheckCreateDto check) {
        final String URL_REQUEST = String.format(ID_URL_TEMPLATE, trId, skuId);
        HttpEntity<CheckCreateDto> httpEntity = new HttpEntity<>(check);
        ResponseEntity<CheckReadDto> response =
                restTemplate.exchange(URL_REQUEST,
                        HttpMethod.PUT, httpEntity, new ParameterizedTypeReference<>() {
                        });
        CheckReadDto body = response.getBody();
        if (body == null) {
            log.error("Error after request to [{}]. Response status code is [{}]. But response body is null", URL_REQUEST, response.getStatusCode());
            throw new EmptyResponseBodyException();
        }
        log.info("Check was updated.{}", body);
    }

    public void performDeleteByIdRequest(Long trId, Long skuId) {
        restTemplate.delete(String.format(ID_URL_TEMPLATE, trId, skuId));
    }

    public void performExportToCsv(HttpServletResponse servletResponse) throws IOException {
        List<CheckCreateDto> checks = performGetAll().stream().map(checkMapper::map).toList();
        servletResponse.setContentType("text/csv");
        servletResponse.addHeader("Content-Disposition", "attachment; filename=checks.csv");
        servletResponse.setCharacterEncoding("UTF-8");
        csvWriter.exportCsv(servletResponse.getWriter(), checks, CheckCreateDto.class);
    }

    public void performImportFromCsv(InputStream is) {
        List<CheckCreateDto> customers = csvReader.importCsv(is, CheckCreateDto.class);
        performSaveChecksRequest(customers);
    }
}
