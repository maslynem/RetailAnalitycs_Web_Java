package ru.s21school.retailanalytics_web.services.tableServices;

import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;
import ru.s21school.retailanalytics_web.controllers.tableControllers.ImportExportHandler;
import ru.s21school.retailanalytics_web.dto.entityDto.checkDto.CheckCreateDto;
import ru.s21school.retailanalytics_web.dto.entityDto.checkDto.CheckPageDto;
import ru.s21school.retailanalytics_web.dto.entityDto.checkDto.CheckReadDto;
import ru.s21school.retailanalytics_web.exceptions.EmptyResponseBodyException;

import java.io.IOException;

@Service
@Slf4j
public class CheckService {
    private static final String CHECKS_API_URL = "http://localhost:8081/api/v1/checks";
    private final ImportExportHandler importExportHandler;
    private final RestTemplate restTemplate;
    private final String PAGE_URL_TEMPLATE;
    private final String ID_URL_TEMPLATE;

    public CheckService(RestTemplate restTemplate) {
        this.importExportHandler = new ImportExportHandler(CHECKS_API_URL, "checks", restTemplate);
        this.restTemplate = restTemplate;
        PAGE_URL_TEMPLATE = CHECKS_API_URL + "?page=%d&size=%d";
        ID_URL_TEMPLATE = CHECKS_API_URL + "/%d/%d";
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
        importExportHandler.exportToCsv(servletResponse);
    }

    public void performImportFromCsv(MultipartFile file) {
        importExportHandler.importFromCsv(file);
    }
}
